package tx.rpg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import tx.api.Mensagem;

public class InventoryClickEvent implements Listener {

    @EventHandler
    public void onPlayerClick(org.bukkit.event.inventory.InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        if (inv.getName().equals(Mensagem.formatar("&7Atributos"))){
            event.setCancelled(true);
        }
    }
}
