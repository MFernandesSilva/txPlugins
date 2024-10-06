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
    public void onInventoryClick(InventoryClickEvent event){
        Inventory inv = event.getInventory();
        Player player = (Player) event.getWhoClicked();

        if (inv.getName().equals(Mensagem.formatar("&7Info Atributos"))){
            event.setCancelled(true);

            ItemStack itemClicado = event.getCurrentItem();
            ItemStack voltar = new Item(Material.ARROW, 1, (short) 0).setName("&eVoltar").getIs();

            if (itemClicado.getItemMeta().getDisplayName().equals(Mensagem.formatar("&c&lATAQUE"))){
                Inventory invAtaque = Inventario.criarInventario(27, "&c&lATAQUE");

                List<String> loreDano = new ArrayList<>();
                List<String> loreAlcance = new ArrayList<>();
                List<String> loreRouboVida = new ArrayList<>();
                List<String> loreDanoTotal = new ArrayList<>();

                {
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

                }


                ItemStack dano = new Item(Material.IRON_SWORD, 1, (short) 0)
                        .setName("&c➪ DANO").setLore(loreDano).getIs();
                ItemStack alcance = new Item(Material.BOW, 1, (short) 0)
                        .setName("&c➪ ALCANCE").setLore(loreAlcance).getIs();
                ItemStack rouboVida = new Item(Material.getMaterial(551), 1, (short) 0)
                        .setName("&c➪ ROUBO DE VIDA").setLore(loreRouboVida).getIs();
                ItemStack danoTotal = new Item(Material.DIAMOND_SWORD, 1, (short) 0)
                        .setName("&c➪ DANO TOTAL").setLore(loreDanoTotal).getIs();

                Inventario.adicionarItem(invAtaque, dano, 10);
                Inventario.adicionarItem(invAtaque, alcance, 12);
                Inventario.adicionarItem(invAtaque, rouboVida, 14);
                Inventario.adicionarItem(invAtaque, danoTotal, 16);
                Inventario.adicionarItem(invAtaque, voltar, 18);

                player.openInventory(invAtaque);
            }
            if (itemClicado.getItemMeta().getDisplayName().equals(Mensagem.formatar("&a&lDEFESA"))){
                Inventory invDefesa = Inventario.criarInventario(27, "&a&lDEFESA");

                List<String> loreDefesa = new ArrayList<>();
                List<String> loreBloqueio = new ArrayList<>();
                List<String> loreRegenVida = new ArrayList<>();
                List<String> loreDefesaTotal = new ArrayList<>();

                {
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

                }


                ItemStack defesa = new Item(Material.IRON_CHESTPLATE, 1, (short) 0)
                        .setName("&a➪ DEFESA").setLore(loreDefesa).getIs();
                ItemStack bloqueio = new Item(Material.getMaterial(4352), 1, (short) 0)
                        .setName("&a➪ BLOQUEIO").setLore(loreBloqueio).getIs();
                ItemStack regenVida = new Item(Material.getMaterial(4582), 1, (short) 0)
                        .setName("&a➪ RENERAÇÃO DE VIDA").setLore(loreRegenVida).getIs();
                ItemStack defesaTotal = new Item(Material.DIAMOND_CHESTPLATE, 1, (short) 0)
                        .setName("&a➪ DEFESA TOTAL").setLore(loreDefesaTotal).getIs();

                Inventario.adicionarItem(invDefesa, defesa, 10);
                Inventario.adicionarItem(invDefesa, bloqueio, 12);
                Inventario.adicionarItem(invDefesa, regenVida, 14);
                Inventario.adicionarItem(invDefesa, defesaTotal, 16);
                Inventario.adicionarItem(invDefesa, voltar, 18);

                player.openInventory(invDefesa);
            }
            if (itemClicado.getItemMeta().getDisplayName().equals(Mensagem.formatar("&b&lGERAL"))){
                Inventory invGeral = Inventario.criarInventario(45, "&b&lGERAL");

                List<String> loreIntel = new ArrayList<>();
                List<String> loreRegenMana = new ArrayList<>();
                List<String> loreAmpCombate = new ArrayList<>();
                List<String> lorePenDefesa = new ArrayList<>();
                List<String> loreSorte = new ArrayList<>();

                {
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
                    lorePenDefesa.add(" ");
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
                    lorePenDefesa.add(" ");
                    lorePenDefesa.add(Mensagem.formatar("&7&m+++++++++++++&7 &ePLAYER &7&m+++++++++++++"));
                    lorePenDefesa.add(" ");
                    loreSorte.add(" ");
                    loreSorte.add(Mensagem.formatar("&c&lEM BREVE"));
                    loreSorte.add(" ");

                }


                ItemStack intel = new Item(Material.getMaterial(4575), 1, (short) 0)
                        .setName("&a➪ INTELIGÊNCIA").setLore(loreIntel).getIs();
                ItemStack regenMana = new Item(Material.getMaterial(4584), 1, (short) 0)
                        .setName("&a➪ REGENERAÇÃO DE MANA").setLore(loreRegenMana).getIs();
                ItemStack ampCombate = new Item(Material.getMaterial(4543), 1, (short) 0)
                        .setName("&a➪ AMPLIFICAÇÃO DE COMBATE").setLore(loreAmpCombate).getIs();
                ItemStack penDefesa = new Item(Material.getMaterial(4383), 1, (short) 0)
                        .setName("&a➪ PENETRAÇÃO DE DEFESA").setLore(lorePenDefesa).getIs();
                ItemStack sorte = new Item(Material.getMaterial(4301), 1, (short) 0)
                        .setName("&a➪ SORTE").setLore(loreSorte).getIs();

                Inventario.adicionarItem(invGeral, intel, 11);
                Inventario.adicionarItem(invGeral, regenMana, 13);
                Inventario.adicionarItem(invGeral, ampCombate, 15);
                Inventario.adicionarItem(invGeral, penDefesa, 30);
                Inventario.adicionarItem(invGeral, sorte, 32);
                Inventario.adicionarItem(invGeral, voltar, 36);

                player.openInventory(invGeral);
            }
        }

        if (inv.getName().equals(Mensagem.formatar("&c&lATAQUE")) ||
                inv.getName().equals(Mensagem.formatar("&a&lDEFESA")) ||
                inv.getName().equals(Mensagem.formatar("&b&lGERAL"))){
            event.setCancelled(true);
            ItemStack itemCliado = event.getCurrentItem();
            if (itemCliado.getItemMeta().getDisplayName().equals(Mensagem.formatar("&eVoltar"))){
                player.closeInventory();
                Bukkit.dispatchCommand(player, "infoatributos");
            }
        }
    }
}
