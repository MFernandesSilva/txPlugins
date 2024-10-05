package tx.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Particula {
    private static Map<UUID, BukkitTask> tasks = new HashMap<>();

    public static void criarParticula(Player player, Effect effect, int quantidade, double raio, double velocidade) {
        Location loc = player.getLocation();
        double i;
        for (i = 0.0D; i < 6.283185307179586D; i += 0.39269908169872414D) {
            double x = loc.getX() + raio * Math.cos(i);
            double z = loc.getZ() + raio * Math.sin(i);
            playEffect(loc.getWorld(), effect, new Location(loc.getWorld(), x, loc.getY() + 1.0D, z), quantidade);
        }
    }

    public static void criarParticulaEnvolta(JavaPlugin plugin, Player player, Runnable particleCreator, int quantidade, double raio, double velocidade, long tick) {
        UUID uuid = player.getUniqueId();
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!player.isOnline()) {
                Particula.removerParticulaEnvolta(player);
                return;
            }
            particleCreator.run();
        }, 0L, tick);
        tasks.put(uuid, task);
    }

    public static void removerParticulaEnvolta(Player player) {
        BukkitTask task = tasks.remove(player.getUniqueId());
        if (task != null)
            task.cancel();
    }

    private static void playEffect(World world, Effect effect, Location location, int amount) {
        if (effect != null)
            for (int i = 0; i < amount; i++)
                world.playEffect(location, effect, 0);
    }

    private static Effect getEffect(Effect effect) {
        return effect;
    }

    public static void criarParticulaKaioken(Player player, int quantidade, double raio1, double raio2) {
        Location loc = player.getLocation();
        double i;
        for (i = 0.0D; i < 6.283185307179586D; i += 0.39269908169872414D) {
            double x1 = loc.getX() + raio1 * Math.cos(i);
            double z1 = loc.getZ() + raio1 * Math.sin(i);
            playEffect(loc.getWorld(), Effect.MOBSPAWNER_FLAMES, new Location(loc.getWorld(), x1, loc.getY() + 1.0D, z1), quantidade);
            double x2 = loc.getX() + raio2 * Math.cos(i);
            double z2 = loc.getZ() + raio2 * Math.sin(i);
            playEffect(loc.getWorld(), Effect.POTION_SWIRL, new Location(loc.getWorld(), x2, loc.getY() + 1.0D, z2), quantidade / 2);
            playEffect(loc.getWorld(), Effect.SMOKE, new Location(loc.getWorld(), x2, loc.getY() + 1.0D, z2), quantidade / 4);
        }
    }
}
