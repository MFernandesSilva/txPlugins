package tx.rpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tx.api.DM;
import tx.api.Mensagem;
import tx.rpg.data.ReinosPlayerData;
import tx.rpg.itens.Reinos;
import tx.rpg.reinos.Reino;
import tx.rpg.reinos.ReinoAPI;
import tx.rpg.reinos.TipoReino;
import tx.rpg.txRPG;

public class ReinosCommand implements CommandExecutor {
    private final DM dm = new DM();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(dm.cc());
            return true;
        }

        Player player = (Player) sender;

        // Verifica se o jogador tem permissão para usar o comando
        if (!player.hasPermission("txrpg.reinos.op")) {
            player.sendMessage(dm.np());
            return true;
        }

        switch (args.length) {
            case 2:
                return handleTwoArgsCommands(sender, args);
            case 3:
                return handleThreeArgsCommands(sender, args);
            default:
                return false;
        }
    }

    private boolean handleTwoArgsCommands(CommandSender sender, String[] args) {
        String action = args[0].toLowerCase();

        if (action.equals("give")) {
            if (!sender.hasPermission("txrpg.admin")) {
                sender.sendMessage(dm.np());
                return true;
            }
            return handleGiveCommand(sender, args);
        }

        if (action.equals("romper")) {
            if (!sender.hasPermission("txrpg.admin")) {
                sender.sendMessage(dm.np());
                return true;
            }
            return handleRomperCommand(sender, args);
        }

        if (action.equals("upgrade")) {
            if (!sender.hasPermission("txrpg.admin")) {
                sender.sendMessage(dm.np());
                return true;
            }
            return handleUpgradeCommand(sender, args);
        }

        return false;
    }

    private boolean handleThreeArgsCommands(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("set")) {
            if (!sender.hasPermission("txrpg.admin")) {
                sender.sendMessage(dm.np());
                return true;
            }
            return handleSetCommand(sender, args);
        }
        return false;
    }

    private boolean handleGiveCommand(CommandSender sender, String[] args) {
        String nivelStr = args[1].toLowerCase();
        ItemStack reino = obterRunaPorTipoENivel(nivelStr);

        if (reino != null) {
            ((Player) sender).getInventory().addItem(reino);
            sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&aVocê recebeu um reino nível " + nivelStr + "."));
        } else {
            sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cErro ao criar a runa ou nível inválido."));
        }
        return true;
    }

    private boolean handleRomperCommand(CommandSender sender, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não encontrado."));
            return true;
        }

        ReinosPlayerData playerData = txRPG.getInstance().getReinosPlayerData().get(target.getUniqueId());
        if (playerData == null) {
            sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não encontrado ou sem atributos."));
            return true;
        }

        Reino reino = playerData.getReinos().get(TipoReino.REINO);
        if (reino == null) {
            sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não possui este tipo de runa."));
            return true;
        }

        if (!ReinoAPI.podeEvoluirReino(target, TipoReino.REINO)) {
            sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cA runa não pode ser rompida ainda."));
            return true;
        }

        int novoNivel = reino.getNivel() + 1;
        reino.setNivel(novoNivel);

        txRPG.getInstance().reinosDB().salvarDadosJogadorAsync(playerData);
        sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Reino rompido com sucesso para &c" + target.getName() + "&7."));
        target.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Sua runa foi rompida para o nível &c" + novoNivel + "&7!"));

        return true;
    }

    private boolean handleUpgradeCommand(CommandSender sender, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não encontrado."));
            return true;
        }

        ReinosPlayerData playerData = txRPG.getInstance().getReinosPlayerData().get(target.getUniqueId());
        if (playerData == null) {
            sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não encontrado ou sem atributos."));
            return true;
        }

        Reino reino = playerData.getReinos().get(TipoReino.REINO);
        if (reino == null) {
            sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não possui este tipo de runa."));
            return true;
        }

        // Lógica para atualizar a runa
        if (reino.getNivel() == 0) {
            reino.setNivel(1);
        } else if (reino.getNivel() < ReinoAPI.getNivelMaximo(reino.getNivel())) {
            reino.setNivel(reino.getNivel() + 1);
        } else {
            target.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cO reino já está no nível máximo ou precisa romper."));
            return true;
        }

        txRPG.getInstance().reinosDB().salvarDadosJogadorAsync(playerData);
        sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Reino atualizado com sucesso para &c" + target.getName() + "&7."));
        target.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Seu reino foi atualizado."));

        return true;
    }

    private boolean handleSetCommand(CommandSender sender, String[] args) {
        try {
            int nivel = Integer.parseInt(args[1]);
            Player target = Bukkit.getPlayer(args[2]);
            setarNivelReino(sender, target, nivel);
        } catch (Exception e) {
            sender.sendMessage(Mensagem.formatar("&cUso: /reinos set <nivel>"));
        }
        return true;
    }

    // Método auxiliar para obter o nível
    private int obterNivel(CommandSender sender, String nivelArg) {
        try {
            int nivel = Integer.parseInt(nivelArg);
            if (nivel < 1 || nivel > 53) {
                sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cNível inválido."));
                return -1;
            }
            return nivel;
        } catch (NumberFormatException e) {
            sender.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cNível deve ser um número."));
            return -1;
        }
    }

    // Método auxiliar para obter a runa por tipo e nível
    private ItemStack obterRunaPorTipoENivel(String nivelStr) {
        int nivel = -1; // Definindo um nível padrão inválido

        switch (nivelStr) {
            case "mortal":
                nivel = 1; // Defina o intervalo que você deseja
                break;
            case "combate":
                nivel = 21; // Defina o intervalo que você deseja
                break;
            case "celestial":
                nivel = 36; // Defina o intervalo que você deseja
                break;
            case "imortal":
                nivel = 46; // Defina o intervalo que você deseja
                break;
            case "deus":
                nivel = 51; // Defina o intervalo que você deseja
                break;
            default:
                return null; // Retorna null se o nível não for válido
        }

        return obterRunaPorNivel(nivel); // Chama o método existente que cria a runa com base no nível
    }

    private ItemStack obterRunaPorNivel(int nivel) {
        // Sua lógica existente para obter runa por nível
        // Exemplo:
        if (nivel >= 1 && nivel <= 20) {
            return new Reinos().reinoMortal[nivel - 1];
        } else if (nivel >= 21 && nivel <= 35) {
            return new Reinos().reinoDeCombate[nivel - 1];
        } else if (nivel >= 36 && nivel <= 45) {
            return new Reinos().reinoCelestial[nivel - 1];
        } else if (nivel >= 46 && nivel <= 50) {
            return new Reinos().reinoImortal[nivel - 1];
        } else if (nivel >= 51 && nivel <= 53) {
            return new Reinos().reinoDeus[nivel - 1];
        } else {
            return null; // Nível inválido
        }
    }

    private void setarNivelReino(CommandSender sender, Player player, int nivel) {
        if (player == null) {
            sender.sendMessage(Mensagem.formatar("&cPrecisa informar o jogador"));
            return;
        }

        if (!player.isOnline()) {
            sender.sendMessage(Mensagem.formatar("&cJogador não está online"));
            return;
        }

        ReinosPlayerData reinosPlayerData = txRPG.getInstance().getReinosPlayerData().get(player.getUniqueId());

        if (reinosPlayerData == null) {
            sender.sendMessage(Mensagem.formatar("&cJogador não possui dados de reino."));
            return;
        }

        Reino reino = reinosPlayerData.getReinos().get(TipoReino.REINO);

        if (reino == null) {
            sender.sendMessage(Mensagem.formatar("&cJogador não possui reino."));
            return;
        }

        reino.setNivel(nivel);
        txRPG.getInstance().reinosDB().salvarDadosJogadorAsync(reinosPlayerData);
        sender.sendMessage(Mensagem.formatar("&7O reino de &c" + player.getName() + "&7 foi setado para o nível &c" + nivel + "&7."));
        player.sendMessage(Mensagem.formatar("&7Seu nível foi setado para &c" + nivel + "&7."));
    }
}
