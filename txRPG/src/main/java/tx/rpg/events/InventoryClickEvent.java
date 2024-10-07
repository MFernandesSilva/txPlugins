package tx.rpg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import tx.api.Mensagem;

public class InventoryClickEvent implements Listener {

    @EventHandler
    public void onPlayerClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        // Obtém o jogador que clicou no inventário
        Player player = (Player) event.getWhoClicked();
        // Obtém o inventário que foi clicado
        Inventory inv = event.getInventory();

        // Verifica se o nome do inventário é "Atributos"
        if (inv.getName().equals(Mensagem.formatar("&7Atributos"))) {
            // Cancela o evento se o inventário for "Atributos"
            event.setCancelled(true);
        }
    }
}
