package tx.api;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Som {
    public static void tocarSom(Player player, Sound som, float volume, float pitch) {
        player.playSound(player.getLocation(), som, volume, pitch);
    }

    public static void tocarSomGlobal(Sound som, float volume, float pitch) {
        for (Player player : Bukkit.getOnlinePlayers())
            tocarSom(player, som, volume, pitch);
    }
}
