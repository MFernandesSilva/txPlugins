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
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        txRPG.getInstance().db().carregarDadosJogadorAsync(player);
        txRPG.getInstance().runasDB().carregarDadosJogadorAsync(player);
        player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Reequipe sua armadura para ter seus atributos."));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        PlayerData playerData = txRPG.getInstance().getPlayerData().get(player.getUniqueId());
        RunasPlayerData runasPlayerData = txRPG.getInstance().getRunasPlayerData().get(player.getUniqueId());

        if (playerData != null){
            txRPG.getInstance().db().salvarDadosJogadorAsync(playerData);
            txRPG.getInstance().runasDB().salvarDadosJogadorAsync(runasPlayerData);
            txRPG.getInstance().getPlayerData().remove(player.getUniqueId());
            txRPG.getInstance().getRunasPlayerData().remove(player.getUniqueId());
        }
    }
}
