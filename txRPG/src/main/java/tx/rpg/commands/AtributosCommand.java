package tx.rpg.commands;

import org.bukkit.Bukkit;
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
import tx.rpg.data.PlayerData;
import tx.rpg.data.RunasPlayerData;
import tx.rpg.runas.Runa;
import tx.rpg.runas.TipoRuna;
import tx.rpg.txRPG;
import tx.rpg.utils.CalcularStatus;

import java.util.ArrayList;
import java.util.List;

import static tx.rpg.runas.TipoRuna.*;
import static tx.rpg.utils.CalcularStatus.formatarNumero;

public class AtributosCommand implements CommandExecutor {

    private static final String PREFIX = txRPG.getInstance().getConfiguracao().getPrefix();
    private static final String MSG_ATRIBUTO_INVALIDO = "&cAtributo inváldo.";
    private static final String MSG_QUANTIDADE_INVALIDA = "&cA quantidade deve ser um número.";
    private static final String MSG_JOGADOR_NAO_ENCONTRADO = "&cJogador não encontrado.";
    private static final String MSG_JOGADOR_SEM_ATRIBUTOS = "&cJogador não encontrado ou sem atributos.";

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        DM dm = new DM();
        if (!(s instanceof Player)){
            s.sendMessage(dm.cc());
            return true;
        }

        Player player = (Player) s;

        if (args.length == 0){
            PlayerData playerData = txRPG.getInstance().getPlayerData().get(player.getUniqueId());
            RunasPlayerData runasPlayerData = txRPG.getInstance().getRunasPlayerData().get(player.getUniqueId());
            Runa runaDANO = runasPlayerData.getRunas().get(DANO);
            Runa runaDEFESA = runasPlayerData.getRunas().get(DEFESA);
            Runa runaAmpCombate = runasPlayerData.getRunas().get(AMPLIFICACAO);
            Inventory menu = Inventario.criarInventario(54, "&7Atributos");

            List<String> loreAtaque = new ArrayList<>();
            List<String> loreDefesa = new ArrayList<>();
            List<String> loreGeral = new ArrayList<>();

            {
                loreAtaque.add(" ");
                loreAtaque.add(Mensagem.formatar("&c➪ Dano: &7" + formatarNumero(playerData.getDano() + runaDANO.getValorAtributo())));
                loreAtaque.add(Mensagem.formatar("&c➪ Alcance: &7" + playerData.getAlcance()));
                loreAtaque.add(Mensagem.formatar("&c➪ Roubo de Vida: &7" + formatarNumero(playerData.getRouboVida())));
                loreAtaque.add(" ");
                loreAtaque.add(Mensagem.formatar("&c➪ Dano Total: &7" + formatarNumero(playerData.getDanoFinal())));
                loreAtaque.add(" ");
            }

            {
                loreDefesa.add(" ");
                loreDefesa.add(Mensagem.formatar("&a➪ Defesa: &7" + formatarNumero(playerData.getDefesa() + runaDEFESA.getValorAtributo())));
                loreDefesa.add(Mensagem.formatar("&a➪ Bloqueio: &7" + playerData.getBloqueio() + "%"));
                loreDefesa.add(Mensagem.formatar("&a➪ Regeneração de Vida: &7" + formatarNumero(playerData.getRegenVida())));
                loreDefesa.add(" ");
                loreDefesa.add(Mensagem.formatar("&a➪ Defesa Total: &7" + formatarNumero(playerData.getDefesaFinal())));
                loreDefesa.add(" ");
            }

            {
                loreGeral.add(" ");
                loreGeral.add(Mensagem.formatar("&b➪ Inteligência: &7" + playerData.getIntel()));
                loreGeral.add(Mensagem.formatar("&b➪ Regeneração de Mana: &7" + formatarNumero(playerData.getRegenMana())));
                loreGeral.add(Mensagem.formatar("&b➪ Amplificação de Combate: &7" + formatarNumero(playerData.getAmpCombate() + runaAmpCombate.getValorAtributo()) + "%"));
                loreGeral.add(Mensagem.formatar("&b➪ Penetração de Defesa: &7" + formatarNumero(playerData.getPenDefesa())));
                loreGeral.add(Mensagem.formatar("&b➪ Sorte: &7" + playerData.getSorte()));
                loreGeral.add(" ");
            }


            ItemStack ataque = new Item(Material.DIAMOND_SWORD, 1, (short) 0)
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

            ItemStack runas = criarItemRunas(runasPlayerData);

            Inventario.adicionarItem(menu, ataque, 37);
            Inventario.adicionarItem(menu, geral, 40);
            Inventario.adicionarItem(menu, defesa, 43);
            Inventario.adicionarItem(menu, runas, 22);

            player.openInventory(menu);
            return true;
        }

        if (args.length == 2){
            if (args[0].equals("ver")){
                if (!(s instanceof Player)){
                    s.sendMessage(dm.cc());
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null){
                    s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + " &cJogador não encontrado."));
                    return true;
                }

                PlayerData playerData = txRPG.getInstance().getPlayerData().get(target.getUniqueId());
                RunasPlayerData runasPlayerData = txRPG.getInstance().getRunasPlayerData().get(target.getUniqueId());
                Runa runaDANO = runasPlayerData.getRunas().get(DANO);
                Runa runaDEFESA = runasPlayerData.getRunas().get(DEFESA);
                Runa runaAmpCombate = runasPlayerData.getRunas().get(AMPLIFICACAO);
                if (playerData == null){
                    s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + txRPG.getInstance().getConfiguracao().getMensagemErroVerAtributos()));
                    return true;
                }

                Inventory menu = Inventario.criarInventario(54, "&7Atributos");

                List<String> loreAtaque = new ArrayList<>();
                List<String> loreDefesa = new ArrayList<>();
                List<String> loreGeral = new ArrayList<>();
                List<String> lorePlayer = new ArrayList<>();

                {
                    lorePlayer.add(" ");
                    lorePlayer.add(Mensagem.formatar("&7Atributos de &e" + target.getName()));
                    lorePlayer.add(" ");
                }

                {
                    loreAtaque.add(" ");
                    loreAtaque.add(Mensagem.formatar("&c➪ Dano: &7" + formatarNumero(playerData.getDano() + runaDANO.getValorAtributo())));
                    loreAtaque.add(Mensagem.formatar("&c➪ Alcance: &7" + playerData.getAlcance()));
                    loreAtaque.add(Mensagem.formatar("&c➪ Roubo de Vida: &7" + formatarNumero(playerData.getRouboVida())));
                    loreAtaque.add(" ");
                    loreAtaque.add(Mensagem.formatar("&c➪ Dano Total: &7" + formatarNumero(playerData.getDanoFinal())));
                    loreAtaque.add(" ");
                }

                {
                    loreDefesa.add(" ");
                    loreDefesa.add(Mensagem.formatar("&a➪ Defesa: &7" + formatarNumero(playerData.getDefesa() + runaDEFESA.getValorAtributo())));
                    loreDefesa.add(Mensagem.formatar("&a➪ Bloqueio: &7" + playerData.getBloqueio() + "%"));
                    loreDefesa.add(Mensagem.formatar("&a➪ Regeneração de Vida: &7" + formatarNumero(playerData.getRegenVida())));
                    loreDefesa.add(" ");
                    loreDefesa.add(Mensagem.formatar("&a➪ Defesa Total: &7" + formatarNumero(playerData.getDefesaFinal())));
                    loreDefesa.add(" ");
                }

                {
                    loreGeral.add(" ");
                    loreGeral.add(Mensagem.formatar("&b➪ Inteligencia: &7" + playerData.getIntel()));
                    loreGeral.add(Mensagem.formatar("&b➪ Regeneração de Mana: &7" + formatarNumero(playerData.getRegenMana())));
                    loreGeral.add(Mensagem.formatar("&b➪ Amplificação de Combate: &7" + formatarNumero(playerData.getAmpCombate() + runaAmpCombate.getValorAtributo()) + "%"));
                    loreGeral.add(Mensagem.formatar("&b➪ Penetração de Defesa: &7" + formatarNumero(playerData.getPenDefesa())));
                    loreGeral.add(Mensagem.formatar("&b➪ Sorte: &7" + playerData.getSorte()));
                    loreGeral.add(" ");
                }

                    ItemStack ataque = new Item(Material.DIAMOND_SWORD, 1, (short) 0)
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

                    ItemStack playerItem = new Item(Material.NETHER_STAR, 1, (short) 0)
                            .setName("&7&lATRIBUTOS")
                            .setLore(lorePlayer)
                            .getIs();

                ItemStack runas = criarItemRunas(runasPlayerData);

                    Inventario.adicionarItem(menu, ataque, 37);
                    Inventario.adicionarItem(menu, geral, 40);
                    Inventario.adicionarItem(menu, defesa, 43);
                    Inventario.adicionarItem(menu, playerItem, 4);
                Inventario.adicionarItem(menu, runas, 22);

                    player.openInventory(menu);

                    return true;
                }
            }

        if (args.length == 4){
            if (!s.hasPermission("txrpg.admin")){
                s.sendMessage(dm.np());
                return true;
            }

            String operacao = args[0].toLowerCase();
            String atributo = args[1].toLowerCase();
            double quantidade;

            try {
                quantidade = Double.parseDouble(args[2]);
            } catch (NumberFormatException e){
                s.sendMessage(Mensagem.formatar(PREFIX + MSG_QUANTIDADE_INVALIDA));
                return true;
            }

            Player target = Bukkit.getPlayer(args[3]);
            if (target == null){
                s.sendMessage(Mensagem.formatar(PREFIX + MSG_JOGADOR_NAO_ENCONTRADO));
                return true;
            }

            PlayerData playerData = txRPG.getInstance().getPlayerData().get(target.getUniqueId());
            RunasPlayerData runasPlayerData = txRPG.getInstance().getRunasPlayerData().get(target.getUniqueId());
            if (playerData == null){
                s.sendMessage(Mensagem.formatar(PREFIX + MSG_JOGADOR_SEM_ATRIBUTOS));
                return true;
            }

            switch (operacao){
                case "add":
                    adicionarAtributo(s, playerData, runasPlayerData, atributo, quantidade);
                    break;
                case "remove":
                    removerAtributo(s, playerData, runasPlayerData, atributo, quantidade);
                    break;
                case "set":
                    definirAtributo(s, playerData, runasPlayerData, atributo, quantidade);
                    break;
                default:
                    s.sendMessage(Mensagem.formatar(PREFIX + txRPG.getInstance().getConfiguracao().getMensagemUsoTxAtributos()));
                    return true;
            }

            txRPG.getInstance().db().salvarDadosJogadorAsync(playerData);
            s.sendMessage(Mensagem.formatar(PREFIX + txRPG.getInstance().getConfiguracao().getMensagemAtributosAlterados()
                    .replace("%jogador%", target.getName())));
            target.sendMessage(Mensagem.formatar(PREFIX + txRPG.getInstance().getConfiguracao().getMensagemSeusAtributosAlterados()
                    .replace("%staff%", s.getName())));

            return true;

        }
        return false;
    }

    private void definirAtributo(CommandSender s, PlayerData playerData, RunasPlayerData runasPlayerData, String atributo, double quantidade) {
        switch (atributo) {
            case "dano":
                playerData.setDano(quantidade);
                break;
            case "defesa":
                playerData.setDefesa(quantidade);
                break;
            case "intel":
                playerData.setIntel((int) quantidade);
                break;
            case "ampCombate":
                playerData.setAmpCombate((int) quantidade);
                break;
            case "alcance":
                playerData.setAlcance((int) quantidade);
                break;
            case "penDefesa":
                playerData.setPenDefesa(quantidade);
                break;
            case "bloqueio":
                playerData.setBloqueio((int) quantidade);
                break;
            case "rouboVida":
                playerData.setRouboVida((int) quantidade);
                break;
            case "regenVida":
                playerData.setRegenVida((int) quantidade);
                break;
            case "regenMana":
                playerData.setRegenMana((int) quantidade);
                break;
            case "sorte":
                playerData.setSorte((int) quantidade);
                break;
            default:
                s.sendMessage(Mensagem.formatar(PREFIX + MSG_ATRIBUTO_INVALIDO));
        }
        CalcularStatus.calcularAtributos(playerData, runasPlayerData);
    }

    private void removerAtributo(CommandSender s, PlayerData playerData, RunasPlayerData runasPlayerData, String atributo, double quantidade) {
        switch (atributo) {
            case "dano":
                playerData.setDano(Math.max(0, playerData.getDano() - quantidade));
                break;
            case "defesa":
                playerData.setDefesa(Math.max(0, playerData.getDefesa() - quantidade));
                break;
            case "intel":
                playerData.setIntel((int) Math.max(0, playerData.getIntel() - quantidade));
                break;
            case "ampCombate":
                playerData.setAmpCombate((int) Math.max(0, playerData.getAmpCombate() - quantidade));
                break;
            case "alcance":
                playerData.setAlcance((int) Math.max(0, playerData.getAlcance() - quantidade));
                break;
            case "penDefesa":
                playerData.setPenDefesa(Math.max(0, playerData.getPenDefesa() - quantidade));
                break;
            case "bloqueio":
                playerData.setBloqueio((int) Math.max(0, playerData.getBloqueio() - quantidade));
                break;
            case "rouboVida":
                playerData.setRouboVida((int) Math.max(0, playerData.getRouboVida() - quantidade));
                break;
            case "regenVida":
                playerData.setRegenVida((int) Math.max(0, playerData.getRegenVida() - quantidade));
                break;
            case "regenMana":
                playerData.setRegenMana((int) Math.max(0, playerData.getRegenMana() - quantidade));
                break;
            case "sorte":
                playerData.setSorte((int) Math.max(0, playerData.getSorte() - quantidade));
                break;
            default:
                s.sendMessage(Mensagem.formatar(PREFIX + MSG_ATRIBUTO_INVALIDO));
        }
        CalcularStatus.calcularAtributos(playerData, runasPlayerData);
    }

    private void adicionarAtributo(CommandSender s, PlayerData playerData, RunasPlayerData runasPlayerData, String atributo, double quantidade) {
        switch (atributo) {
            case "dano":
                playerData.setDano(playerData.getDano() + quantidade);
                break;
            case "defesa":
                playerData.setDefesa(playerData.getDefesa() + quantidade);
                break;
            case "intel":
                playerData.setIntel((int) (playerData.getIntel() + quantidade));
                break;
            case "ampCombate":
                playerData.setAmpCombate((int) (playerData.getAmpCombate() + quantidade));
                break;
            case "alcance":
                playerData.setAlcance((int) (playerData.getAlcance() + quantidade));
                break;
            case "penDefesa":
                playerData.setPenDefesa((playerData.getPenDefesa() + quantidade));
                break;
            case "bloqueio":
                playerData.setBloqueio((int) (playerData.getBloqueio() + quantidade));
                break;
            case "rouboVida":
                playerData.setRouboVida((int) (playerData.getRouboVida() + quantidade));
                break;
            case "regenVida":
                playerData.setRegenVida((int) (playerData.getRegenVida() + quantidade));
                break;
            case "regenMana":
                playerData.setRegenMana((int) (playerData.getRegenMana() + quantidade));
                break;
            case "sorte":
                playerData.setSorte((int) (playerData.getSorte() + quantidade));
                break;
            default:
                s.sendMessage(Mensagem.formatar(PREFIX + MSG_ATRIBUTO_INVALIDO));
        }
        CalcularStatus.calcularAtributos(playerData, runasPlayerData);
    }

    private static ItemStack criarItemRunas(RunasPlayerData playerData) {
        List<String> lore = new ArrayList<>();
        for (TipoRuna tipoRuna : TipoRuna.values()) {
            Runa runa = playerData.getRunas().get(tipoRuna);
            if (runa.getNivel() > 0) {
                lore.add(Mensagem.formatar("&7" + tipoRuna + ": &f" + runa.getNivel() + "-" + runa.getSubnivel()));
                lore.add(Mensagem.formatar("&7+" + formatarNumero(runa.getValorAtributo()) + (tipoRuna == TipoRuna.AMPLIFICACAO ? "%" : "")));
                lore.add("");
            }
        }
        Material material = Material.getMaterial(4584);
        return new Item(material, 1, (short) 0)
                .setName(Mensagem.formatar("&5&lRUNAS"))
                .setLore(lore)
                .setUmbreakable(true)
                .getIs();
    }
}
