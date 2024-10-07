package tx.rpg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import tx.api.Mensagem;
import tx.rpg.data.PlayerData;
import tx.rpg.data.RunasPlayerData;
import tx.rpg.txRPG;

import java.util.UUID;

public class PlayerJoinAndQuit implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        // Carrega os dados do jogador de forma assíncrona ao entrar
        txRPG.getInstance().db().carregarDadosJogadorAsync(player);
        txRPG.getInstance().runasDB().carregarDadosJogadorAsync(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        PlayerData playerData = txRPG.getInstance().getPlayerData().get(playerUUID);
        RunasPlayerData runasPlayerData = txRPG.getInstance().getRunasPlayerData().get(playerUUID);

        if (playerData != null) {
            // Salva os dados do jogador de forma assíncrona ao sair
            txRPG.getInstance().db().salvarDadosJogadorAsync(playerData);
            txRPG.getInstance().runasDB().salvarDadosJogadorAsync(runasPlayerData);
            txRPG.getInstance().getPlayerData().remove(playerUUID);
            txRPG.getInstance().getRunasPlayerData().remove(playerUUID);
        }
    }
}
