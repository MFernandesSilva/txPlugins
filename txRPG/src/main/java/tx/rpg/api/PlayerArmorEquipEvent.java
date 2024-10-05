package tx.rpg.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerArmorEquipEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final ItemStack armor;
    private final ArmorAction action;

    public enum ArmorAction {
        EQUIP, UNEQUIP
    }

    public PlayerArmorEquipEvent(Player player, ItemStack armor, ArmorAction action) {
        this.player = player;
        this.armor = armor;
        this.action = action;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getArmor() {
        return armor;
    }

    public ArmorAction getAction() {
        return action;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
