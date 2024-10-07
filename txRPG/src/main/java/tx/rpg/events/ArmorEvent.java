package tx.rpg.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
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

            // Verifica se o jogador está no modo de sobrevivência e se está usando Shift
            if (player.getGameMode() == GameMode.SURVIVAL && !event.isShiftClick()) {
                // Verifica se o jogador está interagindo com um slot de armadura
                if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                    ItemStack currentItem = event.getCurrentItem(); // Item no slot de armadura (desequipar)
                    ItemStack cursorItem = event.getCursor();      // Item no cursor (equipar)

                    // Permitir desequipar sem checar o slot
                    desequiparArmadura(player, currentItem);

                    // Verifica se o item no cursor é válido para o slot antes de equipar
                    equiparArmadura(player, cursorItem, event);
                }
            } else if (event.isShiftClick()) {
                // Cancela o evento se o jogador tentar equipar a armadura com Shift
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Você não pode equipar armadura segurando Shift.");
            }
        }
    }

    private void desequiparArmadura(Player player, ItemStack currentItem) {
        if (currentItem != null && currentItem.getItemMeta() != null) {
            PlayerArmorEquipEvent unequipEvent = new PlayerArmorEquipEvent(player, currentItem, PlayerArmorEquipEvent.ArmorAction.UNEQUIP);
            Bukkit.getServer().getPluginManager().callEvent(unequipEvent);
        }
    }

    private void equiparArmadura(Player player, ItemStack cursorItem, InventoryClickEvent event) {
        if (cursorItem != null && cursorItem.getItemMeta() != null) {
            if (!isSlotCorretoParaArmadura(cursorItem, event)) {
                player.sendMessage(ChatColor.RED + "Você não pode equipar esse item nesse slot.");
                event.setCancelled(true); // Cancela a ação de equipar
                return;
            }

            PlayerArmorEquipEvent equipEvent = new PlayerArmorEquipEvent(player, cursorItem, PlayerArmorEquipEvent.ArmorAction.EQUIP);
            Bukkit.getServer().getPluginManager().callEvent(equipEvent);
        }
    }

    private boolean isSlotCorretoParaArmadura(ItemStack item, InventoryClickEvent event) {
        if (item == null) {
            return false;
        }

        // Verifica se o item é um capacete e se está sendo colocado no slot 39
        if (item.getType().name().contains("HELMET") && event.getSlot() == 39) {
            return true;
        }
        // Verifica se o item é um peitoral e se está sendo colocado no slot 38
        else if (item.getType().name().contains("CHESTPLATE") && event.getSlot() == 38) {
            return true;
        }
        // Verifica se o item são calças e se está sendo colocado no slot 37
        else if (item.getType().name().contains("LEGGINGS") && event.getSlot() == 37) {
            return true;
        }
        // Verifica se o item são botas e se está sendo colocado no slot 36
        else if (item.getType().name().contains("BOOTS") && event.getSlot() == 36) {
            return true;
        }

        return false;
    }

    // se o player colocar a armadura com o shift cancele o evento
}
