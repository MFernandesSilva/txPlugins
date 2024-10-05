package tx.rpg;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import tx.api.DBC;
import tx.api.DM;
import tx.rpg.commands.AtributosCommand;
import tx.rpg.commands.CriarEquipamentosCommand;
import tx.rpg.commands.RompimentoRunasCommand;
import tx.rpg.commands.RunasCommand;
import tx.rpg.config.Config;
import tx.rpg.data.DatabaseManager;
import tx.rpg.data.PlayerData;
import tx.rpg.data.RunasDatabaseManager;
import tx.rpg.data.RunasPlayerData;
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
    private HashMap<UUID, Integer> vidaJogadores = new HashMap<>();
    private Config config;
    private final DatabaseManager databaseManager;
    private final RunasDatabaseManager runasDatabaseManager;

    public txRPG(){
        this.databaseManager = new DatabaseManager();
        this.runasDatabaseManager = new RunasDatabaseManager();
    }

    @Override
    public void onEnable() {
        instance = this;

        this.config = new Config();

        config.loadConfiguration();

        try {
            databaseManager.conectar();
            runasDatabaseManager.conectar();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao conectar ao banco de dados: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        registerCommands();
        registerEvents();


        DM.onEnable(this);

        Bukkit.getScheduler().runTaskTimer(this, this::atualizarAtributos, 20L, 20L);

        new BukkitRunnable(){
            @Override
            public void run(){
                for (Player player : getServer().getOnlinePlayers()){
                    int vidaAtual = DBC.getHealth(player);
                    vidaJogadores.put(player.getUniqueId(), vidaAtual);
                }
            }
        }.runTaskTimer(this, 0L, 1L);
    }

    @Override
    public void onDisable() {
        databaseManager.desconectar();
        runasDatabaseManager.desconectar();

        DM.onDisable(this);
    }

    private void registerCommands(){
        getCommand("atributos").setExecutor(new AtributosCommand());
        getCommand("criarequipamento").setExecutor(new CriarEquipamentosCommand());
        getCommand("runas").setExecutor(new RunasCommand());
        getCommand("rompimento").setExecutor(new RompimentoRunasCommand());
    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new PlayerJoinAndQuit(), this);
        getServer().getPluginManager().registerEvents(new RangeEvent(), this);
        getServer().getPluginManager().registerEvents(new ItemHeld(), this);
        getServer().getPluginManager().registerEvents(new DamageEvent(), this);
        getServer().getPluginManager().registerEvents(new ArmorEvent(), this);
        getServer().getPluginManager().registerEvents(new ArmorEquipEvent(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickEvent(), this);
        getServer().getPluginManager().registerEvents(new UseRunas(), this);
    }

    public static txRPG getInstance() { return instance; }

    public Config getConfiguracao() {
        return config;
    }

    public DatabaseManager db() {
        return databaseManager;
    }
    public RunasDatabaseManager runasDB(){
        return runasDatabaseManager;
    }

    public Map<UUID, PlayerData> getPlayerData() {
        return playerData;
    }

    public Map<UUID, RunasPlayerData> getRunasPlayerData(){
        return runasPlayerData;
    }

    private void atualizarAtributos() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = getPlayerData().get(player.getUniqueId());
            RunasPlayerData runasPlayerData = getRunasPlayerData().get(player.getUniqueId());
            if (playerData != null && runasPlayerData != null) {
                CalcularStatus.calcularAtributos(playerData, runasPlayerData);
            }
        }
    }

    public int getVidaArmazenada(Player player) {
        return vidaJogadores.getOrDefault(player.getUniqueId(), 0);
    }
}
