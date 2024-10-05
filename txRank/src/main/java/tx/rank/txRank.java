package tx.rank;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import tx.api.DM;
import tx.rank.commands.RankCommand;
import tx.rank.commands.XPCommand;
import tx.rank.events.PlayerAttackEvent;
import tx.rank.events.PlayerDeathXPHandler;
import tx.rank.events.XPEvent;

public class txRank extends JavaPlugin {

    private static txRank instance;
    private final RankSystem rankSystem = new RankSystem();

    @Override
    public void onEnable() {
        instance = this;

        registerCommands();
        registerEvents();


        DM.onEnable(this);
    }

    @Override
    public void onDisable() {
        DM.onDisable(this);
    }

    private void registerCommands(){
        getCommand("exp").setExecutor(new XPCommand());
        getCommand("rank").setExecutor(new RankCommand());
    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new XPEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathXPHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerAttackEvent(), this);
    }

    public static txRank getInstance() {
        return instance;
    }

    private void iniciarVerificacaoRank() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    rankSystem.verificarRank(player);
                }
            }
        }.runTaskTimer(this, 0L, 200L);
    }
}
