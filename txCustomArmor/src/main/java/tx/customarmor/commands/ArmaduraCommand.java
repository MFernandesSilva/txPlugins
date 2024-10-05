package tx.customarmor.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tx.api.DM;
import tx.api.Item;
import tx.api.Mensagem;

import java.util.ArrayList;
import java.util.List;

public class ArmaduraCommand implements CommandExecutor {
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        DM dm = new DM();

        if (!(s instanceof Player)){
            s.sendMessage(dm.cc());
            return true;
        }

        Player player = (Player) s;

        if (!player.hasPermission("tx.customarmor.op")){
            player.sendMessage(dm.np());
            return true;
        }

        List<String> lore = new ArrayList<>();

        {
            lore.add(" ");
            lore.add(Mensagem.formatar("&7Uma lore simples."));
            lore.add(" ");
        }

        double haste = 2;

        ItemStack armadura = new Item(Material.DIAMOND_CHESTPLATE, 1, (short) 0)
                .setName("&c&lARMADURA")
                .setLore(lore)
                .setNBT("haste", haste)
                .getIs();

        player.getInventory().addItem(armadura);
        player.sendMessage(Mensagem.formatar("&aArmadura recebida."));
        return true;
    }


}
