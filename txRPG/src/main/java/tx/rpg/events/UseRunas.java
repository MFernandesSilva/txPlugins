package tx.rpg.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import tx.api.Mensagem;
import tx.api.NBT;
import tx.rpg.data.RunasPlayerData;
import tx.rpg.runas.Runa;
import tx.rpg.runas.RunaAPI;
import tx.rpg.runas.TipoRuna;
import tx.rpg.txRPG;

public class UseRunas implements Listener {

    private static final String PREFIX = txRPG.getInstance().getConfiguracao().getPrefix();


    @EventHandler
    public void onRunaUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        //ItemStack itemUsado = player.getItemInHand();
        ItemStack itemUsado = e.getItem();

        if (itemUsado != null && itemUsado.getType() != Material.AIR &&
                (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {

            String tipoRuna = NBT.getNBT(itemUsado, "tipo", String.class);

            if (tipoRuna != null && tipoRuna.startsWith("runa")) {
                usarRuna(player, itemUsado, tipoRuna);
                /*
                if (itemUsado.getAmount() > 1) {
                    itemUsado.setAmount(itemUsado.getAmount() - 1);
                } else {
                    player.getInventory().removeItem(itemUsado);
                } */
            } else if (tipoRuna != null && tipoRuna.startsWith("rompimento")) {
                romperRuna(player, itemUsado, tipoRuna);
            }
        }
    }

    private void usarRuna(Player player, ItemStack itemUsado, String tipoRuna) {
        String[] partes = tipoRuna.split("Lvl");
        TipoRuna tipo = TipoRuna.valueOf(partes[0].replace("runa", "").toUpperCase());
        int nivelRunaUsada = Integer.parseInt(partes[1]);

        RunasPlayerData playerData = txRPG.getInstance().getRunasPlayerData().get(player.getUniqueId());
        if (playerData == null) {
            player.sendMessage(Mensagem.formatar(PREFIX + "&cErro ao carregar seus dados."));
            return;
        }

        Runa runaPlayer = playerData.getRunas().get(tipo);
        if (runaPlayer == null) {
            player.sendMessage(Mensagem.formatar(PREFIX + "&cVocê não possui essa runa."));
            return;
        }

        if (runaPlayer.getNivel() == nivelRunaUsada && temRompimento(player, tipo, nivelRunaUsada)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "runas upgrade " + tipo + " " + player.getName());
            if (itemUsado.getAmount() > 1){
                itemUsado.setAmount(itemUsado.getAmount() - 1);
            } else {
                player.getInventory().remove(itemUsado);
            }

        } else {
            player.sendMessage(Mensagem.formatar(PREFIX + "&cVocê precisa do rompimento correto ou não possui o nível necessário para upar essa runa."));
        }
    }

    private void romperRuna(Player player, ItemStack itemUsado, String tipoRuna) {
        String[] partes = tipoRuna.split("Lvl");
        TipoRuna tipo = TipoRuna.valueOf(partes[0].replace("rompimento", "").toUpperCase());
        int nivel = Integer.parseInt(partes[1]);

        if (podeRomperRuna(player, tipo, nivel)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "runas romper " + tipo + " " + player.getName());
            if (itemUsado.getAmount() > 1){
                itemUsado.setAmount(itemUsado.getAmount() - 1);
            } else {
                player.getInventory().remove(itemUsado);
            }
        } else {
            player.sendMessage(Mensagem.formatar(PREFIX + "&cVocê não pode romper essa runa ainda."));
        }
    }

    private boolean temRompimento(Player player, TipoRuna tipo, int nivel) {
        for (ItemStack item : player.getInventory()) {
            if (item != null && item.getType() != Material.AIR) {
                String tipoRuna = NBT.getNBT(item, "tipo", String.class);
                if (tipoRuna != null && tipoRuna.equals("rompimento" + tipo + "Lvl" + nivel)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean podeRomperRuna(Player player, TipoRuna tipo, int nivel) {
        RunasPlayerData playerData = txRPG.getInstance().getRunasPlayerData().get(player.getUniqueId());
        if (playerData == null) return false;

        Runa runa = playerData.getRunas().get(tipo);
        if (runa == null) return false;

        return runa.getNivel() == nivel - 1 && runa.getSubnivel() == RunaAPI.getSubnivelMaximo(runa.getNivel());
    }
}
