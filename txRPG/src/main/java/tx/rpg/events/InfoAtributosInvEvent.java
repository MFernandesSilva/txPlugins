package tx.rpg.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tx.api.Inventario;
import tx.api.Item;
import tx.api.Mensagem;

import java.util.ArrayList;
import java.util.List;

public class InfoAtributosInvEvent implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player) event.getWhoClicked();

        // Verifica se o inventário é o de Info Atributos
        if (inv.getName().equals(Mensagem.formatar("&7Info Atributos"))) {
            event.setCancelled(true);
            ItemStack itemClicado = event.getCurrentItem();
            ItemStack voltar = criarItemVoltar();

            // Verifica se o item clicado é "ATAQUE"
            if (itemClicado.getItemMeta().getDisplayName().equals(Mensagem.formatar("&c&lATAQUE"))) {
                abrirInventarioAtaque(player, voltar);
            }

            // Verifica se o item clicado é "DEFESA"
            if (itemClicado.getItemMeta().getDisplayName().equals(Mensagem.formatar("&a&lDEFESA"))) {
                abrirInventarioDefesa(player, voltar);
            }

            // Verifica se o item clicado é "GERAL"
            if (itemClicado.getItemMeta().getDisplayName().equals(Mensagem.formatar("&b&lGERAL"))) {
                abrirInventarioGeral(player, voltar);
            }
        }

        // Verifica se o inventário é de atributos específicos
        if (inv.getName().equals(Mensagem.formatar("&c&lATAQUE")) ||
                inv.getName().equals(Mensagem.formatar("&a&lDEFESA")) ||
                inv.getName().equals(Mensagem.formatar("&b&lGERAL"))) {
            event.setCancelled(true);
            ItemStack itemClicado = event.getCurrentItem();

            // Verifica se o item clicado é "Voltar"
            if (itemClicado.getItemMeta().getDisplayName().equals(Mensagem.formatar("&eVoltar"))) {
                player.closeInventory();
                Bukkit.dispatchCommand(player, "infoatributos");
            }
        }
    }

    private ItemStack criarItemVoltar() {
        return new Item(Material.ARROW, 1, (short) 0).setName("&eVoltar").getIs();
    }

    private void abrirInventarioAtaque(Player player, ItemStack voltar) {
        Inventory invAtaque = Inventario.criarInventario(27, "&c&lATAQUE");

        List<String> loreDano = new ArrayList<>();
        List<String> loreAlcance = new ArrayList<>();
        List<String> loreRouboVida = new ArrayList<>();
        List<String> loreDanoTotal = new ArrayList<>();

        // Configurando Lore para Ataque
        loreDano.add(" ");
        loreDano.add(Mensagem.formatar("&cDANO&7 de base para o seu &cDANO TOTAL"));
        loreDano.add(" ");

        loreAlcance.add(" ");
        loreAlcance.add(Mensagem.formatar("&7Distância que você pode bater"));
        loreAlcance.add(" ");

        loreRouboVida.add(" ");
        loreRouboVida.add(Mensagem.formatar("&7Quantidade de &cVIDA &7que você"));
        loreRouboVida.add(Mensagem.formatar("&7rouba do seu oponente"));
        loreRouboVida.add(" ");

        loreDanoTotal.add(" ");
        loreDanoTotal.add(Mensagem.formatar("&7Seu &cDANO &7que irá inferir no seu oponente"));
        loreDanoTotal.add(" ");

        // Criando os itens de ataque
        ItemStack dano = new Item(Material.IRON_SWORD, 1, (short) 0)
                .setName("&c➪ DANO").setLore(loreDano).getIs();
        ItemStack alcance = new Item(Material.BOW, 1, (short) 0)
                .setName("&c➪ ALCANCE").setLore(loreAlcance).getIs();
        ItemStack rouboVida = new Item(Material.getMaterial(551), 1, (short) 0)
                .setName("&c➪ ROUBO DE VIDA").setLore(loreRouboVida).getIs();
        ItemStack danoTotal = new Item(Material.DIAMOND_SWORD, 1, (short) 0)
                .setName("&c➪ DANO TOTAL").setLore(loreDanoTotal).getIs();

        // Adicionando itens ao inventário de ataque
        Inventario.adicionarItem(invAtaque, dano, 10);
        Inventario.adicionarItem(invAtaque, alcance, 12);
        Inventario.adicionarItem(invAtaque, rouboVida, 14);
        Inventario.adicionarItem(invAtaque, danoTotal, 16);
        Inventario.adicionarItem(invAtaque, voltar, 18);

        player.openInventory(invAtaque);
    }

    private void abrirInventarioDefesa(Player player, ItemStack voltar) {
        Inventory invDefesa = Inventario.criarInventario(27, "&a&lDEFESA");

        List<String> loreDefesa = new ArrayList<>();
        List<String> loreBloqueio = new ArrayList<>();
        List<String> loreRegenVida = new ArrayList<>();
        List<String> loreDefesaTotal = new ArrayList<>();

        // Configurando Lore para Defesa
        loreDefesa.add(" ");
        loreDefesa.add(Mensagem.formatar("&aDEFESA&7 de base para o sua &aDEFESA TOTAL"));
        loreDefesa.add(" ");

        loreBloqueio.add(" ");
        loreBloqueio.add(Mensagem.formatar("&7Chance de &aBLOQUEAR &7100% do &CDANO &7recebido"));
        loreBloqueio.add(" ");

        loreRegenVida.add(" ");
        loreRegenVida.add(Mensagem.formatar("&7Quantidade de &cVIDA &7que você"));
        loreRegenVida.add(Mensagem.formatar("&aREGENERA &7a cada 5 segundos"));
        loreRegenVida.add(" ");

        loreDefesaTotal.add(" ");
        loreDefesaTotal.add(Mensagem.formatar("&7Sua &aDEFESA &7que irá reduzir &CDANO &7recebido"));
        loreDefesaTotal.add(" ");

        // Criando os itens de defesa
        ItemStack defesa = new Item(Material.IRON_CHESTPLATE, 1, (short) 0)
                .setName("&a➪ DEFESA").setLore(loreDefesa).getIs();
        ItemStack bloqueio = new Item(Material.getMaterial(4352), 1, (short) 0)
                .setName("&a➪ BLOQUEIO").setLore(loreBloqueio).getIs();
        ItemStack regenVida = new Item(Material.getMaterial(4582), 1, (short) 0)
                .setName("&a➪ RENERAÇÃO DE VIDA").setLore(loreRegenVida).getIs();
        ItemStack defesaTotal = new Item(Material.DIAMOND_CHESTPLATE, 1, (short) 0)
                .setName("&a➪ DEFESA TOTAL").setLore(loreDefesaTotal).getIs();

        // Adicionando itens ao inventário de defesa
        Inventario.adicionarItem(invDefesa, defesa, 10);
        Inventario.adicionarItem(invDefesa, bloqueio, 12);
        Inventario.adicionarItem(invDefesa, regenVida, 14);
        Inventario.adicionarItem(invDefesa, defesaTotal, 16);
        Inventario.adicionarItem(invDefesa, voltar, 18);

        player.openInventory(invDefesa);
    }

    private void abrirInventarioGeral(Player player, ItemStack voltar) {
        Inventory invGeral = Inventario.criarInventario(45, "&b&lGERAL");

        List<String> loreIntel = new ArrayList<>();
        List<String> loreRegenMana = new ArrayList<>();
        List<String> loreAmpCombate = new ArrayList<>();
        List<String> lorePenDefesa = new ArrayList<>();
        List<String> loreSorte = new ArrayList<>();

        // Configurando Lore para Geral
        loreIntel.add(" ");
        loreIntel.add(Mensagem.formatar("&c&lEM BREVE"));
        loreIntel.add(" ");

        loreRegenMana.add(" ");
        loreRegenMana.add(Mensagem.formatar("&7&c&lEM BREVE"));
        loreRegenMana.add(" ");

        loreAmpCombate.add(" ");
        loreAmpCombate.add(Mensagem.formatar("&7Porcentagem a mais de &CDANO &7e &aDEFESA"));
        loreAmpCombate.add(Mensagem.formatar("&7Diretamente no seu &cDANO TOTAL &7e &aDEFESA TOTAL"));
        loreAmpCombate.add(" ");

        lorePenDefesa.add(Mensagem.formatar("&7&m+++++++++++++&7 &eNPC &7&m+++++++++++++"));
        lorePenDefesa.add(" ");
        lorePenDefesa.add(Mensagem.formatar("&750% da sua &bPENETRAÇÂO DE DEFESA"));
        lorePenDefesa.add(Mensagem.formatar("&7sera convertida em &cDANO"));
        lorePenDefesa.add(" ");
        lorePenDefesa.add(Mensagem.formatar("&7&m+++++++++++++&7 &eNPC &7&m+++++++++++++"));
        lorePenDefesa.add(" ");
        lorePenDefesa.add(Mensagem.formatar("&7&m+++++++++++++&7 &ePLAYER &7&m+++++++++++++"));
        lorePenDefesa.add(" ");
        lorePenDefesa.add(Mensagem.formatar("&7Quantidade da &aDEFESA &7 do oponente"));
        lorePenDefesa.add(Mensagem.formatar("&7que irá ser ignorada"));

        loreSorte.add(" ");
        loreSorte.add(Mensagem.formatar("&7% de chance de dropar itens raros"));
        loreSorte.add(" ");

        // Criando os itens gerais
        ItemStack intel = new Item(Material.BOOK, 1, (short) 0)
                .setName("&b➪ INTELIGÊNCIA").setLore(loreIntel).getIs();
        ItemStack regenMana = new Item(Material.BOOK, 1, (short) 0)
                .setName("&b➪ REGENERAÇÃO DE MANA").setLore(loreRegenMana).getIs();
        ItemStack ampCombate = new Item(Material.BOOK, 1, (short) 0)
                .setName("&b➪ AMPLIFICAÇÂO DE COMBATE").setLore(loreAmpCombate).getIs();
        ItemStack penDefesa = new Item(Material.BOOK, 1, (short) 0)
                .setName("&b➪ PENETRAÇÃO DE DEFESA").setLore(lorePenDefesa).getIs();
        ItemStack sorte = new Item(Material.BOOK, 1, (short) 0)
                .setName("&b➪ SORTE").setLore(loreSorte).getIs();

        // Adicionando itens ao inventário geral
        Inventario.adicionarItem(invGeral, intel, 11);
        Inventario.adicionarItem(invGeral, regenMana, 13);
        Inventario.adicionarItem(invGeral, ampCombate, 15);
        Inventario.adicionarItem(invGeral, penDefesa, 30);
        Inventario.adicionarItem(invGeral, sorte, 32);
        Inventario.adicionarItem(invGeral, voltar, 36);

        player.openInventory(invGeral);
    }
}
