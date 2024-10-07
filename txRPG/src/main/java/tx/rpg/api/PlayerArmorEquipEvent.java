package tx.rpg.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

// Classe que representa o evento de equipar/desequipar armaduras
public class PlayerArmorEquipEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player; // Jogador que está equipando/desequipando a armadura
    private final ItemStack armor; // Armadura que está sendo equipada/desequipada
    private final ArmorAction action; // Ação que está sendo realizada (equipar ou desequipar)

    // Enum que representa as ações de armadura
    public enum ArmorAction {
        EQUIP, UNEQUIP // Equipar e desequipar
    }

    // Construtor do evento
    public PlayerArmorEquipEvent(Player player, ItemStack armor, ArmorAction action) {
        this.player = player;
        this.armor = armor;
        this.action = action;
    }

    // Método para obter o jogador
    public Player getPlayer() {
        return player;
    }

    // Método para obter a armadura
    public ItemStack getArmor() {
        return armor;
    }

    // Método para obter a ação realizada
    public ArmorAction getAction() {
        return action;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS; // Retorna a lista de manipuladores do evento
    }

    public static HandlerList getHandlerList() {
        return HANDLERS; // Retorna a lista de manipuladores do evento
    }
}
