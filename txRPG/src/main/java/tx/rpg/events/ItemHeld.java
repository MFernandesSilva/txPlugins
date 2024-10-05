package tx.rpg.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import tx.api.Mensagem;
import tx.api.NBT;
import tx.rpg.data.PlayerData;
import tx.rpg.data.RunasPlayerData;
import tx.rpg.txRPG;
import tx.rpg.utils.CalcularStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemHeld implements Listener {

    private final Map<UUID, PlayerData> originalAttributes = new HashMap<>();

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());

        PlayerData playerData = txRPG.getInstance().getPlayerData().get(playerUUID);
        RunasPlayerData runasPlayerData = txRPG.getInstance().getRunasPlayerData().get(playerUUID);
        if (playerData == null) return;

        if (newItem != null && newItem.getType() == Material.DIAMOND_SWORD && isEquipamentoPersonalizado(newItem)){
            handleEquipamentChange(player, playerUUID, playerData, newItem, previousItem);
        } else if (originalAttributes.containsKey(playerUUID)){
            restaurarAtributosOriginais(playerUUID, playerData, runasPlayerData);
        }
    }

    private boolean isEquipamentoPersonalizado(ItemStack item){
        return NBT.hasNBTKey(item, "dano") && NBT.hasNBTKey(item, "defesa") && NBT.hasNBTKey(item, "intel")
                && NBT.hasNBTKey(item, "ampCombate") && NBT.hasNBTKey(item, "alcance") && NBT.hasNBTKey(item,"penDefesa")
                && NBT.hasNBTKey(item, "bloqueio") && NBT.hasNBTKey(item, "rouboVida")
                && NBT.hasNBTKey(item, "regenVida") && NBT.hasNBTKey(item,"regenMana") && NBT.hasNBTKey(item, "sorte");
    }

    private void aplicarAtributosItem(PlayerData playerData, double dano, double defesa, int intel,
                                      int ampCombate, int alcance, double penDefesa, int bloqueio,
                                      int rouboVida, int regenVida, int regenMana, int sorte){
        playerData.setDano(playerData.getDano() + dano);
        playerData.setDefesa(playerData.getDefesa() + defesa);
        playerData.setIntel(playerData.getIntel() + intel);
        playerData.setAmpCombate((playerData.getAmpCombate() + ampCombate));
        playerData.setAlcance(playerData.getAlcance() + alcance);
        playerData.setPenDefesa(playerData.getPenDefesa() + penDefesa);
        playerData.setBloqueio(playerData.getBloqueio() + bloqueio);
        playerData.setRouboVida(playerData.getRouboVida() + rouboVida);
        playerData.setRegenVida(playerData.getRegenVida() + regenVida);
        playerData.setRegenMana(playerData.getRegenMana() + regenMana);
        playerData.setSorte(playerData.getSorte() + sorte);
    }

    private void restaurarAtributosOriginais(UUID playerUUID, PlayerData playerData, RunasPlayerData runasPlayerData) {
        PlayerData originalData = originalAttributes.remove(playerUUID);

        playerData.setDano(originalData.getDano());
        playerData.setDefesa(originalData.getDefesa());
        playerData.setIntel(originalData.getIntel());
        playerData.setAmpCombate(originalData.getAmpCombate());
        playerData.setAlcance(originalData.getAlcance());
        playerData.setPenDefesa(originalData.getPenDefesa());
        playerData.setBloqueio(originalData.getBloqueio());
        playerData.setRouboVida(originalData.getRouboVida());
        playerData.setRegenVida(originalData.getRegenVida());
        playerData.setRegenMana(originalData.getRegenMana());
        playerData.setSorte(originalData.getSorte());

        CalcularStatus.calcularAtributos(playerData, runasPlayerData);
    }

    private void handleEquipamentChange(Player player, UUID playerUUID, PlayerData playerData, ItemStack newItem, ItemStack previousItem) {
        try {
            double dano = NBT.getNBT(newItem, "dano", Double.class);
            double defesa = NBT.getNBT(newItem, "defesa", Double.class);
            int intel = NBT.getNBT(newItem, "intel", Integer.class);
            int ampCombate = NBT.getNBT(newItem, "ampCombate", Integer.class);
            int alcance = NBT.getNBT(newItem, "alcance", Integer.class);
            double penDefesa = NBT.getNBT(newItem, "penDefesa", Double.class);
            int bloqueio = NBT.getNBT(newItem, "bloqueio", Integer.class);
            int rouboVida = NBT.getNBT(newItem, "rouboVida", Integer.class);
            int regenVida = NBT.getNBT(newItem, "regenVida", Integer.class);
            int regenMana = NBT.getNBT(newItem, "regenMana", Integer.class);
            int sorte = NBT.getNBT(newItem, "sorte", Integer.class);

            if (!originalAttributes.containsKey(playerUUID)) {
                originalAttributes.put(playerUUID, playerData.clone());
            } else {
                PlayerData originalData = originalAttributes.get(playerUUID);
                playerData.setDano(originalData.getDano());
                playerData.setDefesa(originalData.getDefesa());
                playerData.setIntel(originalData.getIntel());
                playerData.setAmpCombate(originalData.getAmpCombate());
                playerData.setAlcance(originalData.getAlcance());
                playerData.setPenDefesa(originalData.getPenDefesa());
                playerData.setBloqueio(originalData.getBloqueio());
                playerData.setRouboVida(originalData.getRouboVida());
                playerData.setRegenVida(originalData.getRegenVida());
                playerData.setRegenMana(originalData.getRegenMana());
                playerData.setSorte(originalData.getSorte());

            }
            aplicarAtributosItem(playerData, dano, defesa, intel, ampCombate, alcance, penDefesa, bloqueio, rouboVida, regenVida, regenMana, sorte);

        } catch (NumberFormatException e) {
            player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cEste item não é um equipamento válido."));
        }
    }

}
