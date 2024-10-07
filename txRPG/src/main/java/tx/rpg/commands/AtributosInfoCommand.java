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

        if (!(s instanceof Player)) {
            s.sendMessage(dm.cc());
            return true;
        }

        Player player = (Player) s;
        Inventory inv = Inventario.criarInventario(27, "&7Info Atributos");

        // Criar as listas de lore para os itens
        List<String> loreAtaque = criarLoreAtaque();
        List<String> loreDefesa = criarLoreDefesa();
        List<String> loreGeral = criarLoreGeral();

        // Criar os itens com suas respectivas lores
        ItemStack dano = criarItem(Material.DIAMOND_SWORD, "&c&lATAQUE", loreAtaque);
        ItemStack defesa = criarItem(Material.DIAMOND_CHESTPLATE, "&a&lDEFESA", loreDefesa);
        ItemStack geral = criarItem(Material.BOOK_AND_QUILL, "&b&lGERAL", loreGeral);

        // Adicionar os itens ao inventário
        Inventario.adicionarItem(inv, dano, 11);
        Inventario.adicionarItem(inv, defesa, 13);
        Inventario.adicionarItem(inv, geral, 15);

        player.openInventory(inv);
        return true;
    }

    // Método auxiliar para criar o lore de ataque
    private List<String> criarLoreAtaque() {
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(Mensagem.formatar("&7Clique para ver informações"));
        lore.add(Mensagem.formatar("&7sobre os atributos de &c&lATAQUE"));
        lore.add(" ");
        return lore;
    }

    // Método auxiliar para criar o lore de defesa
    private List<String> criarLoreDefesa() {
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(Mensagem.formatar("&7Clique para ver informações"));
        lore.add(Mensagem.formatar("&7sobre os atributos de &a&lDEFESA"));
        lore.add(" ");
        return lore;
    }

    // Método auxiliar para criar o lore geral
    private List<String> criarLoreGeral() {
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(Mensagem.formatar("&7Clique para ver informações"));
        lore.add(Mensagem.formatar("&7sobre os atributos &b&lGERAIS"));
        lore.add(" ");
        return lore;
    }

    // Método auxiliar para criar itens
    private ItemStack criarItem(Material material, String nome, List<String> lore) {
        return new Item(material, 1, (short) 0)
                .setName(nome)
                .setLore(lore)
                .getIs();
    }
}
