package tx.rpg.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import tx.rpg.api.PlayerArmorEquipEvent;

public class ArmorEvent implements Listener {

    @EventHandler
    public void onArmorClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();

            // Verifica se o jogador está interagindo com um slot de armadura
            if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                ItemStack currentItem = event.getCurrentItem(); // Item no slot de armadura (desequipar)
                ItemStack cursorItem = event.getCursor();      // Item no cursor (equipar)

                // Equipar
                if (cursorItem != null && cursorItem.getItemMeta() != null) {
                    PlayerArmorEquipEvent equipEvent = new PlayerArmorEquipEvent(player, cursorItem, PlayerArmorEquipEvent.ArmorAction.EQUIP);
                    Bukkit.getServer().getPluginManager().callEvent(equipEvent);
                }

                // Desequipar
                if (currentItem != null && currentItem.getItemMeta() != null) {
                    PlayerArmorEquipEvent unequipEvent = new PlayerArmorEquipEvent(player, currentItem, PlayerArmorEquipEvent.ArmorAction.UNEQUIP);
                    Bukkit.getServer().getPluginManager().callEvent(unequipEvent);
                }
            }
        }
    }
}
