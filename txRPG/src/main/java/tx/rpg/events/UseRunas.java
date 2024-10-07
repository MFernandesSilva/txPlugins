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
import tx.rpg.commands.RunasCommand;
import tx.rpg.data.RunasPlayerData;
import tx.rpg.itens.Runas;
import tx.rpg.runas.Runa;
import tx.rpg.runas.RunaAPI;
import tx.rpg.runas.TipoRuna;
import tx.rpg.txRPG;

public class UseRunas implements Listener {

    private static final String PREFIX = txRPG.getInstance().getConfiguracao().getPrefix();

    @EventHandler
    public void onRunaUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack itemUsado = e.getItem();

        if (itemUsado != null && itemUsado.getType() != Material.AIR &&
                (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {

            String tipoRuna = NBT.getNBT(itemUsado, "tipo", String.class);

            if (tipoRuna != null && tipoRuna.startsWith("runa")) {
                usarRuna(player, itemUsado, tipoRuna);
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
            if (temRompimento(player, tipo, nivelRunaUsada)){
            if (runaPlayer.getSubnivel() == RunaAPI.getSubnivelMaximo(runaPlayer.getNivel())) {
                player.sendMessage(Mensagem.formatar(PREFIX + "&cVocê já está no subnível máximo."));
                return;
            }

            // Realiza o upgrade da runa diretamente
            runaPlayer.setSubnivel(runaPlayer.getSubnivel() + 1); // Incrementa o subnível
            player.sendMessage(Mensagem.formatar(PREFIX + "&aRuna " + tipo + " atualizada com sucesso!"));

            atualizarItemUsado(itemUsado, player);
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
            // Realiza a ação de romper a runa diretamente
            RunasPlayerData playerData = txRPG.getInstance().getRunasPlayerData().get(player.getUniqueId());
            Runa runa = playerData.getRunas().get(tipo);
            runa.setNivel(runa.getNivel() + 1); // Decrementa o nível da runa
            runa.setSubnivel(1);
            player.sendMessage(Mensagem.formatar(PREFIX + "&aRuna " + tipo + " rompida com sucesso!"));

            atualizarItemUsado(itemUsado, player);
        } else {
            player.sendMessage(Mensagem.formatar(PREFIX + "&cVocê não pode romper essa runa ainda."));
        }
    }


    private boolean temRompimento(Player player, TipoRuna tipo, int nivel) {
        RunasPlayerData playerData = txRPG.getInstance().getRunasPlayerData().get(player.getUniqueId());
        if (playerData == null) return false;

        Runa runa = playerData.getRunas().get(tipo);
        return runa != null && runa.getNivel() == nivel; // Verifica se a runa está no nível correto
    }

    private boolean podeRomperRuna(Player player, TipoRuna tipo, int nivel) {
        RunasPlayerData playerData = txRPG.getInstance().getRunasPlayerData().get(player.getUniqueId());
        if (playerData == null) return false;

        Runa runa = playerData.getRunas().get(tipo);
        if (runa == null) return false;

        return runa.getNivel() == nivel - 1 && runa.getSubnivel() == RunaAPI.getSubnivelMaximo(runa.getNivel());
    }

    private void atualizarItemUsado(ItemStack itemUsado, Player player) {
        if (itemUsado.getAmount() > 1) {
            itemUsado.setAmount(itemUsado.getAmount() - 1);
        } else {
            player.getInventory().remove(itemUsado);
        }
    }
}
