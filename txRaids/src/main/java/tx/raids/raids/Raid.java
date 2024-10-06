package tx.raids.raids;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import tx.raids.data.PlayerData;
import tx.raids.data.PlayerDataManager;

import java.util.HashMap;
import java.util.Map;

public class Raid {
    private JavaPlugin plugin;
    private PlayerDataManager playerDataManager;
    private String level; // Nível da raid (FÁCIL, MÉDIO, DIFÍCIL, PESADLO)
    private int tempo; // Tempo total da raid
    private Map<Player, Location> playersInRaid; // Mapeia jogadores às suas localizações na raid

    public Raid(JavaPlugin plugin, PlayerDataManager playerDataManager, String level) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
        this.level = level;
        this.playersInRaid = new HashMap<>();
        this.tempo = 0; // Tempo inicial
    }

    // Método para iniciar a raid
    public void startRaid(Player player) {
        if (playersInRaid.containsKey(player)) {
            player.sendMessage("&cVocê já está em uma raid!");
            return;
        }

        // Adiciona o jogador à raid
        playersInRaid.put(player, player.getLocation());
        player.sendMessage("&a[RAID] &7A raid começou! Nível: &e" + level);

        // Adiciona o jogador aos dados
        playerDataManager.addPlayerData(player);

        // Inicia contagem do tempo da raid
        startRaidTimer(player);
    }

    // Método para finalizar a raid
    public void finishRaid(Player player) {
        if (!playersInRaid.containsKey(player)) {
            player.sendMessage("&cVocê não está em uma raid!");
            return;
        }

        // Atualizar dados do jogador
        PlayerData playerData = playerDataManager.getPlayerData(player);
        playerData.incrementTotalRaids();

        // Exemplo de como incrementar a contagem dependendo do nível da raid
        if (this.level.equals("FÁCIL")) {
            playerData.incrementEasyRaids();
        } else if (this.level.equals("MÉDIO")) {
            playerData.incrementMediumRaids();
        } else if (this.level.equals("DIFÍCIL")) {
            playerData.incrementHardRaids();
        } else if (this.level.equals("PESADELO")) {
            playerData.incrementNightmareRaids();
        }

        // Salvar dados do jogador
        playerDataManager.savePlayerData();

        // Enviar mensagem para o jogador
        player.sendMessage("&a[RAID] &7Parabéns por completar a &aRAID! &7Tempo: &e" + tempo + "&7.");

        // Remove o jogador da lista de jogadores na raid
        playersInRaid.remove(player);
    }

    // Método para iniciar o timer da raid
    private void startRaidTimer(Player player) {
        // Um exemplo básico que incrementa o tempo a cada segundo
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (playersInRaid.containsKey(player)) {
                tempo++; // Incrementa o tempo
                player.sendMessage("&a[RAID] &7Tempo decorrido: &e" + tempo + " segundos");
            } else {
                // Se o jogador não está mais na raid, parar o timer
                Bukkit.getScheduler().cancelTasks(plugin);
            }
        }, 20L, 20L); // 20L = 20 ticks = 1 segundo
    }

    // Getters e Setters
    public String getLevel() {
        return level;
    }

    public int getTempo() {
        return tempo;
    }
}