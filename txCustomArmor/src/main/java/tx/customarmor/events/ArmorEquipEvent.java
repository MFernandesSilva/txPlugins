package tx.customarmor.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArmorEquipEvent implements Listener {

    @EventHandler
    public void onArmorEquip(PlayerArmorEquipEvent event) {
        if (event.getAction() == PlayerArmorEquipEvent.ArmorAction.EQUIP) {
            event.getPlayer().sendMessage(ChatColor.GREEN + "Você equipou a armadura: " + event.getArmor().getItemMeta().getDisplayName());
        } else if (event.getAction() == PlayerArmorEquipEvent.ArmorAction.UNEQUIP) {
            event.getPlayer().sendMessage(ChatColor.RED + "Você desequipou a armadura: " + event.getArmor().getItemMeta().getDisplayName());
        }
    }
}