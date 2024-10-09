package tx.rpg;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import tx.api.DBC;
import tx.api.DM;
import tx.api.NBT;
import tx.rpg.commands.*;
import tx.rpg.config.Config;
import tx.rpg.data.*;
import tx.rpg.events.*;
import tx.rpg.utils.CalcularStatus;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class txRPG extends JavaPlugin {

    private static txRPG instance;
    private Map<UUID, PlayerData> playerData = new HashMap<>();
    private Map<UUID, RunasPlayerData> runasPlayerData = new HashMap<>();
    private Map<UUID, ReinosPlayerData> reinosPlayerData = new HashMap<>();
    private HashMap<UUID, Integer> vidaJogadores = new HashMap<>();
    private Config config;
    private final DatabaseManager databaseManager;
    private final RunasDatabaseManager runasDatabaseManager;
    private final ReinosDatabaseManager reinosDatabaseManager;

    // Construtor da classe
    public txRPG() {
        this.databaseManager = new DatabaseManager();
        this.runasDatabaseManager = new RunasDatabaseManager();
        this.reinosDatabaseManager = new ReinosDatabaseManager();
    }

    @Override
    public void onEnable() {
        instance = this;

        // Carregar configuração
        this.config = new Config();
        config.loadConfiguration();

        // Conectar ao banco de dados
        try {
            databaseManager.conectar();
            runasDatabaseManager.conectar();
            reinosDatabaseManager.conectar();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao conectar ao banco de dados: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Registrar comandos e eventos
        registerCommands();
        registerEvents();
        DM.onEnable(this);

        // Agendar tarefas periódicas
        Bukkit.getScheduler().runTaskTimer(this, this::atualizarAtributos, 20L, 20L);
        Bukkit.getScheduler().runTaskTimer(this, this::atualizarAtributosAtrasado, 20L, 20L);
        Bukkit.getScheduler().runTaskTimer(this, this::regen, 100L, 100L);
        Bukkit.getScheduler().runTaskTimer(this, this::saveHealth, 1L, 1L);

    }

    @Override
    public void onDisable() {
        // Desconectar do banco de dados
        databaseManager.desconectar();
        runasDatabaseManager.desconectar();
        reinosDatabaseManager.desconectar();
        DM.onDisable(this);
    }

    private void registerCommands() {
        // Registrar comandos do plugin
        getCommand("atributos").setExecutor(new AtributosCommand());
        getCommand("criarequipamento").setExecutor(new CriarEquipamentosCommand());
        getCommand("runas").setExecutor(new RunasCommand());
        getCommand("reinos").setExecutor(new ReinosCommand());
        getCommand("rompimentorunas").setExecutor(new RompimentoRunasCommand());
        getCommand("rompimentoreinos").setExecutor(new RompimentoReinoCommand());
        getCommand("infoatributos").setExecutor(new AtributosInfoCommand());
        getCommand("resetatributos").setExecutor(new ResetAtributosCommand());
    }

    private void registerEvents() {
        // Registrar eventos do plugin
        getServer().getPluginManager().registerEvents(new PlayerJoinAndQuit(), this);
        getServer().getPluginManager().registerEvents(new RangeEvent(), this);
        getServer().getPluginManager().registerEvents(new ItemHeld(), this);
        getServer().getPluginManager().registerEvents(new DamageEvent(), this);
        getServer().getPluginManager().registerEvents(new ArmorEvent(), this);
        getServer().getPluginManager().registerEvents(new ArmorEquipEvent(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickEvent(), this);
        getServer().getPluginManager().registerEvents(new UseRunas(), this);
        getServer().getPluginManager().registerEvents(new InfoAtributosInvEvent(), this);
        getServer().getPluginManager().registerEvents(new LightningEvent(), this);
    }

    // Método para obter a instância do plugin
    public static txRPG getInstance() {
        return instance;
    }

    // Método para obter a configuração
    public Config getConfiguracao() {
        return config;
    }

    // Métodos para acessar os gerenciadores de banco de dados
    public DatabaseManager db() {
        return databaseManager;
    }

    public RunasDatabaseManager runasDB() {
        return runasDatabaseManager;
    }

    public ReinosDatabaseManager reinosDB(){
        return reinosDatabaseManager;
    }

    // Métodos para acessar os dados dos jogadores
    public Map<UUID, PlayerData> getPlayerData() {
        return playerData;
    }

    public Map<UUID, RunasPlayerData> getRunasPlayerData() {
        return runasPlayerData;
    }

    public Map<UUID, ReinosPlayerData> getReinosPlayerData(){
        return reinosPlayerData;
    }

    private void atualizarAtributos() {
        // Atualizar atributos dos jogadores
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = getPlayerData().get(player.getUniqueId());
            RunasPlayerData runasPlayerData = getRunasPlayerData().get(player.getUniqueId());
            ReinosPlayerData reinosPlayerData = getReinosPlayerData().get(player.getUniqueId());
            CalcularStatus.calcularAtributos(playerData, runasPlayerData, reinosPlayerData);
        }
    }

    private void atualizarAtributosAtrasado(){
        for (Player player : Bukkit.getOnlinePlayers()){
            UUID playerUUID = player.getUniqueId();
            PlayerData playerData = getPlayerData().get(player.getUniqueId());
            RunasPlayerData runasPlayerData = getRunasPlayerData().get(player.getUniqueId());
            ReinosPlayerData reinosPlayerData = getReinosPlayerData().get(player.getUniqueId());

            if (playerData != null) {
                db().salvarDadosJogadorAsync(playerData);
                runasDB().salvarDadosJogadorAsync(runasPlayerData);
                reinosDB().salvarDadosJogadorAsync(reinosPlayerData);
                getPlayerData().remove(playerUUID);
                getRunasPlayerData().remove(playerUUID);
                getReinosPlayerData().remove(playerUUID);
                db().carregarDadosJogadorAsync(player);
                runasDB().carregarDadosJogadorAsync(player);
                reinosDB().carregarDadosJogadorAsync(player);
                getPlayerData().put(playerUUID, playerData);
                getRunasPlayerData().put(playerUUID, runasPlayerData);
                getReinosPlayerData().put(playerUUID, reinosPlayerData);
            }
        }
    }

    private void regen() {
        // Regenerar vida dos jogadores
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = getPlayerData().get(player.getUniqueId());
            if (playerData != null) {
                if (getVidaArmazenada(player) < DBC.getMaxHealth(player)) {
                    int vida = getVidaArmazenada(player) + playerData.getRegenVida();
                    DBC.setHealthCapped(player, vida);
                }
            }
        }
    }

    public int getVidaArmazenada(Player player) {
        // Obter vida armazenada do jogador
        return vidaJogadores.getOrDefault(player.getUniqueId(), 0);
    }

    private void saveHealth(){
        for (Player player : getServer().getOnlinePlayers()) {
            int vidaAtual = DBC.getHealth(player);
            vidaJogadores.put(player.getUniqueId(), vidaAtual);
        }
    }

}
