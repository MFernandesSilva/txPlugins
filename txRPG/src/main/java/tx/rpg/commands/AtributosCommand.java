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
import tx.rpg.data.ReinosPlayerData;
import tx.rpg.data.RunasPlayerData;
import tx.rpg.reinos.Reino;
import tx.rpg.reinos.TipoReino;
import tx.rpg.runas.Runa;
import tx.rpg.runas.RunaAPI;
import tx.rpg.runas.TipoRuna;
import tx.rpg.txRPG;

import java.util.ArrayList;
import java.util.List;

import static tx.rpg.runas.TipoRuna.*;
import static tx.rpg.utils.CalcularStatus.formatarNumero;

public class AtributosCommand implements CommandExecutor {

    private DM dm = new DM();
    private static final String PREFIX = txRPG.getInstance().getConfiguracao().getPrefix();
    private static final String MSG_ATRIBUTO_INVALIDO = "&cAtributo inválido.";
    private static final String MSG_QUANTIDADE_INVALIDA = "&cA quantidade deve ser um número.";
    private static final String MSG_JOGADOR_NAO_ENCONTRADO = "&cJogador não encontrado.";
    private static final String MSG_JOGADOR_SEM_ATRIBUTOS = "&cJogador não encontrado ou sem atributos.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DM dm = new DM();

        // Verifica se o comando foi enviado por um jogador
        if (!(sender instanceof Player)) {
            sender.sendMessage(dm.cc());
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            abrirMenuAtributos(player);
            return true;
        }

        if (args.length == 2 && args[0].equals("ver")) {
            return verAtributosDeOutroJogador(sender, args[1]);
        }

        if (args.length == 4) {
            return manipularAtributos(sender, args);
        }

        return false;
    }

    private void abrirMenuAtributos(Player player) {
        PlayerData playerData = txRPG.getInstance().getPlayerData().get(player.getUniqueId());
        ReinosPlayerData reinosPlayerData = txRPG.getInstance().getReinosPlayerData().get(player.getUniqueId());
        RunasPlayerData runasPlayerData = txRPG.getInstance().getRunasPlayerData().get(player.getUniqueId());

        Runa runaDANO = runasPlayerData.getRunas().get(DANO);
        Runa runaDEFESA = runasPlayerData.getRunas().get(DEFESA);
        Runa runaAmpCombate = runasPlayerData.getRunas().get(AMPLIFICACAO);

        Reino reino = reinosPlayerData.getReinos().get(TipoReino.REINO);

        Inventory menu = Inventario.criarInventario(54, "&7Atributos");
        List<String> loreAtaque = criarLoreAtaque(playerData, runaDANO, reino);
        List<String> loreDefesa = criarLoreDefesa(playerData, runaDEFESA, reino);
        List<String> loreGeral = criarLoreGeral(playerData, runaAmpCombate, reino);

        ItemStack ataque = criarItem(Material.DIAMOND_SWORD, "&c&lATAQUE", loreAtaque);
        ItemStack defesa = criarItem(Material.DIAMOND_CHESTPLATE, "&a&lDEFESA", loreDefesa);
        ItemStack geral = criarItem(Material.BOOK_AND_QUILL, "&b&lGERAL", loreGeral);
        ItemStack runas = criarItemRunas(runasPlayerData);
        ItemStack reinos = criarItemReinos(reinosPlayerData);

        Inventario.adicionarItem(menu, ataque, 37);
        Inventario.adicionarItem(menu, geral, 40);
        Inventario.adicionarItem(menu, defesa, 43);
        Inventario.adicionarItem(menu, runas, 21);
        Inventario.adicionarItem(menu, reinos, 23);


        player.openInventory(menu);
    }

    private List<String> criarLoreAtaque(PlayerData playerData, Runa runaDANO, Reino reino) {
        List<String> loreAtaque = new ArrayList<>();
        loreAtaque.add(" ");
        loreAtaque.add(Mensagem.formatar("&c➪ Dano: &7" + formatarNumero(playerData.getDano() + runaDANO.getValorAtributo() + reino.getValorAtributo())));
        loreAtaque.add(Mensagem.formatar("&c➪ Alcance: &7" + playerData.getAlcance()));
        loreAtaque.add(Mensagem.formatar("&c➪ Roubo de Vida: &7" + formatarNumero(playerData.getRouboVida())));
        loreAtaque.add(" ");
        loreAtaque.add(Mensagem.formatar("&c➪ Dano Total: &7" + formatarNumero(playerData.getDanoFinal())));
        loreAtaque.add(" ");
        return loreAtaque;
    }

    private List<String> criarLoreDefesa(PlayerData playerData, Runa runaDEFESA, Reino reino) {
        List<String> loreDefesa = new ArrayList<>();
        loreDefesa.add(" ");
        loreDefesa.add(Mensagem.formatar("&a➪ Defesa: &7" + formatarNumero(playerData.getDefesa() + runaDEFESA.getValorAtributo() + reino.getValorAtributo())));
        loreDefesa.add(Mensagem.formatar("&a➪ Bloqueio: &7" + playerData.getBloqueio() + "%"));
        loreDefesa.add(Mensagem.formatar("&a➪ Regeneração de Vida: &7" + formatarNumero(playerData.getRegenVida())));
        loreDefesa.add(" ");
        loreDefesa.add(Mensagem.formatar("&a➪ Defesa Total: &7" + formatarNumero(playerData.getDefesaFinal())));
        loreDefesa.add(" ");
        return loreDefesa;
    }

    private List<String> criarLoreGeral(PlayerData playerData, Runa runaAmpCombate, Reino reino) {
        List<String> loreGeral = new ArrayList<>();
        double amp = playerData.getAmpCombate();
        amp += runaAmpCombate.getValorAtributo();
        double ampReino = reino.getValorAtributo() / 1000;
        amp += ampReino;
        loreGeral.add(" ");
        loreGeral.add(Mensagem.formatar("&b➪ Inteligência: &7" + playerData.getIntel()));
        loreGeral.add(Mensagem.formatar("&b➪ Regeneração de Mana: &7" + formatarNumero(playerData.getRegenMana())));
        loreGeral.add(Mensagem.formatar("&b➪ Amplificação de Combate: &7" + formatarNumero(amp) + "%"));
        loreGeral.add(Mensagem.formatar("&b➪ Penetração de Defesa: &7" + formatarNumero(playerData.getPenDefesa())));
        loreGeral.add(Mensagem.formatar("&b➪ Sorte: &7" + playerData.getSorte()));
        loreGeral.add(" ");
        return loreGeral;
    }

    private ItemStack criarItem(Material material, String nome, List<String> lore) {
        return new Item(material, 1, (short) 0)
                .setName(nome)
                .setLore(lore)
                .getIs();
    }

    private boolean verAtributosDeOutroJogador(CommandSender sender, String nomeJogador) {
        if (!sender.hasPermission("txrpg.veratributos")) {
            sender.sendMessage(dm.np());
            return true;
        }
        Player target = Bukkit.getPlayer(nomeJogador);
        if (target == null) {
            sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + " &cJogador não encontrado."));
            return true;
        }

        PlayerData playerData = txRPG.getInstance().getPlayerData().get(target.getUniqueId());
        RunasPlayerData runasPlayerData = txRPG.getInstance().getRunasPlayerData().get(target.getUniqueId());
        ReinosPlayerData reinosPlayerData = txRPG.getInstance().getReinosPlayerData().get(target.getUniqueId());

        if (playerData == null) {
            sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + txRPG.getInstance().getConfiguracao().getMensagemErroVerAtributos()));
            return true;
        }

        abrirMenuAtributosDeOutroJogador(sender, target, playerData, runasPlayerData, reinosPlayerData);
        return true;
    }

    private void abrirMenuAtributosDeOutroJogador(CommandSender sender, Player target, PlayerData playerData, RunasPlayerData runasPlayerData, ReinosPlayerData reinosPlayerData) {
        Inventory menu = Inventario.criarInventario(54, "&7Atributos");

        List<String> loreAtaque = criarLoreAtaque(playerData, runasPlayerData.getRunas().get(DANO), reinosPlayerData.getReinos().get(TipoReino.REINO));
        List<String> loreDefesa = criarLoreDefesa(playerData, runasPlayerData.getRunas().get(DEFESA), reinosPlayerData.getReinos().get(TipoReino.REINO));
        List<String> loreGeral = criarLoreGeral(playerData, runasPlayerData.getRunas().get(AMPLIFICACAO), reinosPlayerData.getReinos().get(TipoReino.REINO));
        List<String> lorePlayer = criarLorePlayer(target);

        ItemStack ataque = criarItem(Material.DIAMOND_SWORD, "&c&lATAQUE", loreAtaque);
        ItemStack defesa = criarItem(Material.DIAMOND_CHESTPLATE, "&a&lDEFESA", loreDefesa);
        ItemStack geral = criarItem(Material.BOOK_AND_QUILL, "&b&lGERAL", loreGeral);
        ItemStack playerItem = criarItem(Material.NETHER_STAR, "&7&lATRIBUTOS", lorePlayer);
        ItemStack runas = criarItemRunas(runasPlayerData);
        ItemStack reinos = criarItemReinos(reinosPlayerData);

        Inventario.adicionarItem(menu, ataque, 37);
        Inventario.adicionarItem(menu, geral, 40);
        Inventario.adicionarItem(menu, defesa, 43);
        Inventario.adicionarItem(menu, playerItem, 4);
        Inventario.adicionarItem(menu, runas, 21);
        Inventario.adicionarItem(menu, reinos, 23);

        ((Player) sender).openInventory(menu);
    }

    private List<String> criarLorePlayer(Player target) {
        List<String> lorePlayer = new ArrayList<>();
        lorePlayer.add(" ");
        lorePlayer.add(Mensagem.formatar("&7➪ Jogador: &e" + target.getName()));
        lorePlayer.add(Mensagem.formatar("&7➪ UUID: &e" + target.getUniqueId()));
        lorePlayer.add(" ");
        return lorePlayer;
    }

    private boolean manipularAtributos(CommandSender sender, String[] args) {
        if (!sender.hasPermission("txrpg.admin")){
            sender.sendMessage(dm.np());
            return true;
        }
        String acao = args[0];
        String atributo = args[1];
        String quantidadeStr = args[2];
        String jogadorNome = args[3];

        Player target = Bukkit.getPlayer(jogadorNome);
        if (target == null) {
            sender.sendMessage(Mensagem.formatar(PREFIX + MSG_JOGADOR_NAO_ENCONTRADO));
            return true;
        }

        PlayerData playerData = txRPG.getInstance().getPlayerData().get(target.getUniqueId());
        if (playerData == null) {
            sender.sendMessage(Mensagem.formatar(PREFIX + MSG_JOGADOR_SEM_ATRIBUTOS));
            return true;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeStr);
        } catch (NumberFormatException e) {
            sender.sendMessage(Mensagem.formatar(PREFIX + MSG_QUANTIDADE_INVALIDA));
            return true;
        }

        switch (acao.toLowerCase()) {
            case "add":
                return adicionarAtributo(sender, atributo, quantidade, playerData, target);
            case "remove":
                return removerAtributo(sender, atributo, quantidade, playerData, target);
            case "set":
                return definirAtributo(sender, atributo, quantidade, playerData, target);
            default:
                sender.sendMessage(Mensagem.formatar(PREFIX + MSG_ATRIBUTO_INVALIDO));
                return true;
        }
    }

    private boolean adicionarAtributo(CommandSender sender, String atributo, double quantidade, PlayerData playerData, Player target) {
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
                sender.sendMessage(Mensagem.formatar(PREFIX + MSG_ATRIBUTO_INVALIDO));
                return true;
        }
        sender.sendMessage(Mensagem.formatar(PREFIX + "&7Você adicionou &e" + quantidade + " &7ao atributo &e" + atributo + " &7do jogador &e" + target.getName() + "."));
        return true;
    }

    private boolean removerAtributo(CommandSender sender, String atributo, double quantidade, PlayerData playerData, Player target) {
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
                sender.sendMessage(Mensagem.formatar(PREFIX + MSG_ATRIBUTO_INVALIDO));
                return true;
        }
        sender.sendMessage(Mensagem.formatar(PREFIX + "&7Você removeu &e" + quantidade + " &7do atributo &e" + atributo + " &7do jogador &e" + target.getName() + "."));
        return true;
    }

    private boolean definirAtributo(CommandSender sender, String atributo, double quantidade, PlayerData playerData, Player target) {
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
                sender.sendMessage(Mensagem.formatar(PREFIX + MSG_ATRIBUTO_INVALIDO));
                return true;
        }
        sender.sendMessage(Mensagem.formatar(PREFIX + "&7Você definiu o atributo &e" + atributo + " &7do jogador &e" + target.getName() + " &7para &e" + quantidade + "."));
        return true;
    }

    private static ItemStack criarItemRunas(RunasPlayerData playerData) {
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        for (TipoRuna tipoRuna : TipoRuna.values()) {
            Runa runa = playerData.getRunas().get(tipoRuna);
            int nivel = runa.getNivel();
            if (nivel >= 1){
                int subnivel = runa.getSubnivel();
                int limite;
                String cor;
                String nivelString = String.valueOf(subnivel);
                switch (tipoRuna){
                    case DANO:
                        cor = "&c";
                        break;
                    case DEFESA:
                        cor = "&a";
                        break;
                    case AMPLIFICACAO:
                        cor = "&b";
                        break;
                    default:
                        cor = "&7";
                        break;
                }

                if (subnivel >= 1 && subnivel <= 20) {
                    limite = RunaAPI.getSubnivelMaximo(nivel);
                } else if (subnivel >= 21 && subnivel <= 35) {
                    limite = RunaAPI.getSubnivelMaximo(nivel);
                    nivelString = String.valueOf(subnivel - 20);
                } else if (subnivel >= 36 && subnivel <= 45) {
                    limite = RunaAPI.getSubnivelMaximo(nivel);
                    nivelString = String.valueOf(subnivel - 35);
                } else if (subnivel >= 46 && subnivel <= 50) {
                    limite = RunaAPI.getSubnivelMaximo(nivel);
                    nivelString = String.valueOf(subnivel - 45);
                } else if (subnivel >= 51 && subnivel <= 53) {
                    limite = RunaAPI.getSubnivelMaximo(nivel);
                    nivelString = String.valueOf(subnivel - 50);
                } else {
                    return null;
                }

                if (runa.getNivel() > 0) {
                    lore.add(Mensagem.formatar(cor + "RUNA DE " + tipoRuna + " " + nivel + " &7(" + nivelString + "/" + limite + ")"));
                    lore.add(Mensagem.formatar("&7+" + formatarNumero(runa.getValorAtributo()) + (tipoRuna == TipoRuna.AMPLIFICACAO ? "%" : "")));
                    lore.add(" ");
                }
            }
        }

        lore.add(Mensagem.formatar("&c&lPROXIMO NÍVEL"));
        lore.add(" ");
        for (TipoRuna tipoRuna : TipoRuna.values()) {
            Runa runa = playerData.getRunas().get(tipoRuna);
            int nivel = runa.getNivel();
            int subnivel = runa.getSubnivel();
            int limite = RunaAPI.getSubnivelMaximo(nivel);
            String cor;
            String nivelString = " ";
            switch (tipoRuna){
                case DANO:
                    cor = "&c";
                    break;
                case DEFESA:
                    cor = "&a";
                    break;
                case AMPLIFICACAO:
                    cor = "&b";
                    break;
                default:
                    cor = "&7";
                    break;
            }

            if (runa.getNivel() >= 1 && runa.getNivel() <= 52) {
                lore.add(Mensagem.formatar(cor + tipoRuna + ": &7+" + formatarNumero(runa.getValorAtributoProx())));
            } else if (runa.getNivel() == 53) {
                lore.add(Mensagem.formatar(cor + tipoRuna + ": &c&lNÍVEL MAXIMO"));
            }
        }
        lore.add(" ");
        Material material = Material.getMaterial(4584);
        return new Item(material, 1, (short) 0)
                .setName(Mensagem.formatar("&5&lRUNAS"))
                .setLore(lore)
                .setUmbreakable(true)
                .getIs();
    }

    private static ItemStack criarItemReinos(ReinosPlayerData playerData){
        List<String> lore = new ArrayList<>();
        Reino reino = playerData.getReinos().get(TipoReino.REINO);

        int nivel = reino.getNivel();
        if (nivel >= 1){
            int limite;
            String nivelString;
            String nivelString2 = String.valueOf(nivel);
            String cor;

            if (nivel >= 1 && nivel <= 20) {
                cor = "&7&l";
                nivelString = "MORTAL";
                limite = 20;
            } else if (nivel >= 21 && nivel <= 35) {
                cor = "&a&l";
                nivelString = "DE COMBATE";
                limite = 15;
                nivelString2 = String.valueOf(nivel - 20);
            } else if (nivel >= 36 && nivel <= 45) {
                cor = "&5&l";
                nivelString = "CELESTIAL";
                limite = 10;
                nivelString2 = String.valueOf(nivel - 35);
            } else if (nivel >= 46 && nivel <= 50) {
                cor = "&c&l";
                nivelString = "IMORTAL";
                limite = 5;
                nivelString2 = String.valueOf(nivel - 45);
            } else if (nivel >= 51 && nivel <= 53) {
                cor = "&6&l";
                nivelString = "DEUS";
                limite = 3;
                nivelString2 = String.valueOf(nivel - 50);
            } else {
                return null;
            }

            lore.add(" ");
            lore.add(Mensagem.formatar( cor + "REINO " + nivelString + " &7(" + nivelString2 + "/" + limite + ")"));
            lore.add(" ");
            lore.add(Mensagem.formatar("&cDANO: &7+" + formatarNumero(reino.getValorAtributo())));
            lore.add(Mensagem.formatar("&aDEFESA: &7+" + formatarNumero(reino.getValorAtributo())));
            lore.add(Mensagem.formatar("&bAMP. DE COMBATE: &7+" + formatarNumero(reino.getValorAtributo() / 1000) + "%"));
            lore.add(" ");
            if (nivel <= 52) {
                lore.add(" ");
                lore.add(Mensagem.formatar("&c&lPROXIMO NÍVEL"));
                lore.add(" ");
                lore.add(Mensagem.formatar("&cDANO: &7+" + formatarNumero(reino.getValorAtributoProx())));
                lore.add(Mensagem.formatar("&aDEFESA: &7+" + formatarNumero(reino.getValorAtributoProx())));
                lore.add(Mensagem.formatar("&bAMP DE COMBATE: &7+" + formatarNumero(reino.getValorAtributoProx() / 1000) + "%"));
                lore.add(" ");
            } else if (nivel == 53){
                lore.add(" ");
                lore.add(Mensagem.formatar("&c&lNÍVEL MAXIMO"));
                lore.add(" ");
            }
        }
        lore.add(" ");
        Material material = Material.getMaterial(4543);
        return new Item(material, 1, (short) 0)
                .setName(Mensagem.formatar("&9&lREINO"))
                .setLore(lore)
                .setUmbreakable(true)
                .getIs();
    }
}
