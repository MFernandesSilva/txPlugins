package tx.mines.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerTeleport implements Listener {
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!event.getFrom().getWorld().getName().equals("mina") && event.getTo().getWorld().getName().equals("mina")) {
            return;
        }

        if (event.getFrom().getWorld().getName().equals("mina")) {
            ItemStack[] itens = event.getPlayer().getInventory().getContents();
            for (ItemStack item : itens) {
                if (item != null && item.getType() == Material.DIAMOND_PICKAXE) {
                    event.getPlayer().getInventory().remove(item);
                }
            }
        }
    }
}
