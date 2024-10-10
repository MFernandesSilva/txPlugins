package tx.rpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tx.api.DM;
import tx.api.Mensagem;
import tx.rpg.data.RunasPlayerData;
import tx.rpg.itens.Runas;
import tx.rpg.runas.Runa;
import tx.rpg.runas.RunaAPI;
import tx.rpg.runas.TipoRuna;
import tx.rpg.txRPG;

public class RunasCommand implements CommandExecutor {
    DM dm = new DM();

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {

        if (!(s instanceof Player)) {
            s.sendMessage(dm.cc());
            return true;
        }

        Player player = (Player) s;

        // Verifica se o jogador tem permissão para usar o comando
        if (!player.hasPermission("txrpg.admin")) {
            player.sendMessage(dm.np());
            return true;
        }

        if (args.length == 3) {
            // Comando: /runas give <tipo> <nivel>
            if (args[0].equalsIgnoreCase("give")) {
                return handleGiveCommand(player, s, args);
            }
            // Comando: /runas romper <tipo> <player>
            if (args[0].equalsIgnoreCase("romper")) {
                return handleRomperCommand(s, args);
            }
            // Comando: /runas upgrade <tipo> <player>
            if (args[0].equalsIgnoreCase("upgrade")) {
                return handleUpgradeCommand(s, args);
            }
        } else if (args.length == 5) {
            // /runas set <tipo> <nivel> <subnivel> <player>
            if (args[0].equalsIgnoreCase("set")) {
                return handleSetCommand(s, args);
            }
        }
        return false;
    }

    // Método para lidar com o comando give
    private boolean handleGiveCommand(Player player, CommandSender s, String[] args) {
        if (!s.hasPermission("txrpg.admin")) {
            s.sendMessage(dm.np());
            return true;
        }

        TipoRuna tipoRuna = obterTipoRuna(s, args[1]);
        if (tipoRuna == null) return true;

        int nivel = obterNivel(s, args[2]);
        if (nivel == -1) return true;

        ItemStack runa = obterRunaPorTipoENivel(tipoRuna, nivel);
        if (runa != null) {
            player.getInventory().addItem(runa);
            player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&aVocê recebeu uma runa de " + tipoRuna + " nível " + nivel + "."));
        } else {
            player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cErro ao criar a runa."));
        }
        return true;
    }

    // Método para lidar com o comando romper
    private boolean handleRomperCommand(CommandSender s, String[] args) {
        TipoRuna tipoRuna = obterTipoRuna(s, args[1]);
        if (tipoRuna == null) return true;

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não encontrado."));
            return true;
        }

        RunasPlayerData playerData = txRPG.getInstance().getRunasPlayerData().get(target.getUniqueId());
        if (playerData == null) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não encontrado ou sem atributos."));
            return true;
        }

        Runa runa = playerData.getRunas().get(tipoRuna);
        if (runa == null) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não possui este tipo de runa."));
            return true;
        }

        if (!RunaAPI.podeEvoluirRuna(target, tipoRuna)) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cA runa não pode ser rompida ainda."));
            return true;
        }

        int novoNivel = runa.getNivel() + 1;
        runa.setNivel(novoNivel);
        runa.setSubnivel(1);

        txRPG.getInstance().runasDB().salvarDadosJogadorAsync(playerData);
        s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Runa rompida com sucesso para &c" + target.getName() + "&7."));
        target.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Sua runa de &c" + tipoRuna + " &7foi rompida para o nível &c" + novoNivel + "&7!"));

        return true;
    }

    // Método para lidar com o comando upgrade
    private boolean handleUpgradeCommand(CommandSender s, String[] args) {
        TipoRuna tipoRuna = obterTipoRuna(s, args[1]);
        if (tipoRuna == null) return true;

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não encontrado."));
            return true;
        }

        RunasPlayerData playerData = txRPG.getInstance().getRunasPlayerData().get(target.getUniqueId());
        if (playerData == null) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não encontrado ou sem atributos."));
            return true;
        }

        Runa runa = playerData.getRunas().get(tipoRuna);
        if (runa == null) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não possui este tipo de runa."));
            return true;
        }

        // Lógica para atualizar a runa
        if (runa.getNivel() == 0) {
            runa.setNivel(1);
            runa.setSubnivel(1);
        } else if (runa.getSubnivel() < RunaAPI.getSubnivelMaximo(runa.getNivel())) {
            runa.setSubnivel(runa.getSubnivel() + 1);
        } else {
            target.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cA runa já está no subnível máximo."));
            return true;
        }

        txRPG.getInstance().runasDB().salvarDadosJogadorAsync(playerData);
        s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Runa atualizada com sucesso para &c" + target.getName() + "&7."));
        target.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Sua runa de &c" + tipoRuna + " &7foi atualizada."));

        return true;
    }

    // Método para lidar com o comando set
    private boolean handleSetCommand(CommandSender s, String[] args) {
        if (!s.hasPermission("txrpg.admin")) {
            s.sendMessage(dm.np());
            return true;
        }

        try {
            TipoRuna tipo;
            String stringTipo = args[1].toUpperCase();
            int nivel = Integer.parseInt(args[2]);
            int subnivel = Integer.parseInt(args[3]);
            Player target = Bukkit.getPlayer(args[4]);

            switch (stringTipo) {
                case "DANO":
                    tipo = TipoRuna.DANO;
                    break;
                case "DEFESA":
                    tipo = TipoRuna.DEFESA;
                    break;
                case "AMPLIFICACAO":
                    tipo = TipoRuna.AMPLIFICACAO;
                    break;
                default:
                    s.sendMessage(Mensagem.formatar("&cTipo de runa inválida"));
                    return true;
            }
            setarNivelRuna(s, target, tipo, nivel, subnivel);
        } catch (Exception e) {
            s.sendMessage(Mensagem.formatar("&cUso: /runas set <tipo> <nivel> <subnivel>"));
        }
        return true;
    }

    // Método auxiliar para obter o tipo de runa
    private TipoRuna obterTipoRuna(CommandSender s, String tipoArg) {
        try {
            return TipoRuna.valueOf(tipoArg.toUpperCase());
        } catch (IllegalArgumentException e) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cTipo de runa inválido."));
            return null;
        }
    }

    // Método auxiliar para obter o nível
    private int obterNivel(CommandSender s, String nivelArg) {
        try {
            int nivel = Integer.parseInt(nivelArg);
            if (nivel < 1 || nivel > 5) {
                s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cNível deve ser entre 1 e 5."));
                return -1;
            }
            return nivel;
        } catch (NumberFormatException e) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cNível inválido."));
            return -1;
        }
    }

    // Método auxiliar para obter a runa por tipo e nível
    private ItemStack obterRunaPorTipoENivel(TipoRuna tipoRuna, int nivel) {
        Runas runas = new Runas();
        switch (tipoRuna) {
            case DANO:
                return runas.runasDano[nivel - 1];
            case DEFESA:
                return runas.runasDefesa[nivel - 1];
            case AMPLIFICACAO:
                return runas.runasAmplificacao[nivel - 1];
            default:
                return null;
        }
    }

    // Método auxiliar para setar o nível da runa
    private void setarNivelRuna(CommandSender s, Player target, TipoRuna tipo, int nivel, int subnivel) {
        RunasPlayerData playerData = txRPG.getInstance().getRunasPlayerData().get(target.getUniqueId());
        if (playerData == null) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não encontrado ou sem atributos."));
            return;
        }

        Runa runa = playerData.getRunas().get(tipo);
        if (runa == null) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cJogador não possui este tipo de runa."));
            return;
        }

        runa.setNivel(nivel);
        runa.setSubnivel(subnivel);
        txRPG.getInstance().runasDB().salvarDadosJogadorAsync(playerData);
        s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Nível da runa de &c" + target.getName() + " &7atualizado com sucesso."));
        target.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Seu nível de runa de &c" + tipo + " &7foi atualizado."));
    }
}
