package tx.mines.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().getWorld().getName().equals("mina")) {
            ItemStack[] itens = event.getPlayer().getInventory().getContents();
            for (ItemStack item : itens) {
                if (item != null && item.getType() == Material.DIAMOND_PICKAXE) {
                    event.getPlayer().getInventory().remove(item);
                }
            }
        }
    }
}
