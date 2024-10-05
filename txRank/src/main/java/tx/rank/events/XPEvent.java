package tx.rank.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.entity.Player;
import tx.rank.RankSystem;

public class XPEvent implements Listener {

    private final int xpPorLevel = 1000;
    private final RankSystem rankSystem = new RankSystem();

    @EventHandler
    public void onPlayerGainXp(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        int xpGanho = event.getAmount();

        adicionarXP(player, xpGanho);
        rankSystem.verificarRank(player);

        event.setAmount(0);
    }

    private void adicionarXP(Player player, int quantidade) {
        int xpAtual = calcularXPAtual(player);
        int novoXP = xpAtual + quantidade;
        definirXP(player, novoXP);
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

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        event.setDroppedExp(0);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setExpToDrop(0);
    }
}
