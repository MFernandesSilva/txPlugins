package tx.rpg.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tx.api.DM;
import tx.api.Inventario;
import tx.api.Item;
import tx.api.Mensagem;

import java.util.ArrayList;
import java.util.List;

public class AtributosInfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        DM dm = new DM();

        if (!(s instanceof Player)){
            s.sendMessage(dm.cc());
            return true;
        }

        Player player = (Player) s;

        Inventory inv = Inventario.criarInventario(27, "&7Info Atributos");

        List<String> loreAtaque = new ArrayList<>();
        List<String> loreDefesa = new ArrayList<>();
        List<String> loreGeral = new ArrayList<>();

        {
            loreAtaque.add(" ");
            loreAtaque.add(Mensagem.formatar("&7Clique para ver informações"));
            loreAtaque.add(Mensagem.formatar("&7sobre os atributos de &c&lATAQUE"));
            loreAtaque.add(" ");
            loreDefesa.add(" ");
            loreDefesa.add(Mensagem.formatar("&7Clique para ver informações"));
            loreDefesa.add(Mensagem.formatar("&7sobre os atributos de &a&lDEFESA"));
            loreDefesa.add(" ");
            loreGeral.add(" ");
            loreGeral.add(Mensagem.formatar("&7Clique para ver informações"));
            loreGeral.add(Mensagem.formatar("&7sobre os atributos &b&lGERAIS"));
            loreGeral.add(" ");
        }

        ItemStack dano = new Item(Material.DIAMOND_SWORD, 1, (short) 0)
                .setName("&c&lATAQUE")
                .setLore(loreAtaque)
                .getIs();

        ItemStack defesa = new Item(Material.DIAMOND_CHESTPLATE, 1, (short) 0)
                .setName("&a&lDEFESA")
                .setLore(loreDefesa)
                .getIs();

        ItemStack geral = new Item(Material.BOOK_AND_QUILL, 1, (short) 0)
                .setName("&b&lGERAL")
                .setLore(loreGeral)
                .getIs();

        Inventario.adicionarItem(inv, dano, 11);
        Inventario.adicionarItem(inv, defesa, 13);
        Inventario.adicionarItem(inv, geral, 15);

        player.openInventory(inv);
        return true;
    }
}
