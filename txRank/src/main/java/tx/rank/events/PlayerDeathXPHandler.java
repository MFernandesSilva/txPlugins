package tx.rank.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathXPHandler implements Listener {

    private final int xpPorLevel = 1000;

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        int xpAtual = calcularXPAtual(player);
        int xpPerdido = (int) (xpAtual * 0.10);
        int novoXP = xpAtual - xpPerdido;

        definirXP(player, novoXP);
        event.setDroppedExp(0);
    }

    private void definirXP(Player player, int quantidade) {
        int novoNivel = quantidade / xpPorLevel;
        int xpRestante = quantidade % xpPorLevel;

        player.setLevel(novoNivel);
        player.setExp(xpRestante / (float) xpPorLevel);
    }

    private int calcularXPAtual(Player player) {
        int nivelAtual = player.getLevel();
        float progressoNoNivel = player.getExp();
        return (nivelAtual * xpPorLevel) + Math.round(progressoNoNivel * xpPorLevel);
    }
}
