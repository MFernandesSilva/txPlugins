package tx.rpg.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import tx.api.Mensagem;
import tx.api.NBT;
import tx.rpg.data.ReinosPlayerData;
import tx.rpg.reinos.Reino;
import tx.rpg.reinos.ReinoAPI;
import tx.rpg.reinos.TipoReino;
import tx.rpg.txRPG;

public class UseReinos implements Listener {

    private static final String PREFIX = txRPG.getInstance().getConfiguracao().getPrefix();

    @EventHandler
    public void onReinoUse(PlayerInteractEvent e){
        Player player = e.getPlayer();
        ItemStack itemUsado = e.getItem();

        if (itemUsado != null && itemUsado.getType() != Material.AIR &&
                (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && NBT.hasNBTKey(itemUsado, "reino")) {

            String tipoReino = NBT.getNBT(itemUsado, "reino", String.class);

            if (tipoReino != null && tipoReino.startsWith("reino")){
                //usarReino(player, itemUsado, tipoReino);
            } else if (tipoReino != null && tipoReino.startsWith("rompimentoreino")) {
                romperReino(player, itemUsado, tipoReino);
            }
        }
    }

    private void romperReino(Player player, ItemStack itemUsado, String tipoReino){
        String[] partes = tipoReino.split("Lvl");
        int nivel = Integer.parseInt(partes[1]);

        if (podeRomperReino(player, nivel)){
            ReinosPlayerData playerData = txRPG.getInstance().getReinosPlayerData().get(player.getUniqueId());
            Reino reino = playerData.getReinos().get(TipoReino.REINO);
            reino.setNivel(reino.getNivel() + 1);
            player.sendMessage(Mensagem.formatar(PREFIX));
        }

    }

    private boolean temRompimento(Player player, int nivel){
        ReinosPlayerData playerData = txRPG.getInstance().getReinosPlayerData().get(player.getUniqueId());
        if (playerData == null) return false;

        if (podeRomperReino(player, nivel)){
            return false;
        } else {
            return true;
        }
    }

    private boolean podeRomperReino(Player player, int nivel){
        ReinosPlayerData playerData = txRPG.getInstance().getReinosPlayerData().get(player.getUniqueId());
        if (playerData == null) return false;

        Reino reino = playerData.getReinos().get(TipoReino.REINO);
        if (reino == null) return false;
        nivel = reino.getNivel();

        return nivel == ReinoAPI.getNivelMaximo(nivel);
    }


    private void atualizarItemUsado(ItemStack itemUsado, Player player){
        if (itemUsado.getAmount() > 1) {
            itemUsado.setAmount(itemUsado.getAmount() - 1);
        } else {
            player.getInventory().remove(itemUsado);
        }
    }
}
