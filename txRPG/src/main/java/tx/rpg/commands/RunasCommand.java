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
    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        DM dm = new DM();

        Player player = (Player) s;

        if (!player.hasPermission("txrpg.runas.op")){
            player.sendMessage(dm.np());
            return true;
        }

        if (args.length == 3) {
            // /runas give <tipo> <nivel>
            if (args[0].equalsIgnoreCase("give")) {

                TipoRuna tipoRuna;

                try {
                    tipoRuna = TipoRuna.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cTipo de runa inválido."));
                    return true;
                }

                int nivel;

                try {
                    nivel = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cNível deve ser um número."));
                    return true;
                }

                if (nivel < 1 || nivel > 5) {
                    s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cNível inválido (1-5)."));
                    return true;
                }

                ItemStack runa = obterRunaPorTipoENivel(tipoRuna, nivel);

                if (runa != null) {
                    player.getInventory().addItem(runa);
                    player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&aVocê recebeu uma runa de " + tipoRuna + " nível " + nivel + "."));
                } else {
                    player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cErro ao criar a runa."));
                }

                return true;
            }
            // /runas romper <tipo> <player>
            if (args[0].equalsIgnoreCase("romper")){
                TipoRuna tipoRuna;
                try {
                    tipoRuna = TipoRuna.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cTipo de runa inválido."));
                    return true;
                }

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
            // /runas upgrade <tipo> <player>
            if (args[0].equalsIgnoreCase("upgrade")){
                TipoRuna tipoRuna;

                try {
                    tipoRuna = TipoRuna.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cTipo de runa inválido."));
                    return true;
                }

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

                if (runa.getNivel() == 0) {
                    runa.setNivel(1);
                    runa.setSubnivel(1);
                } else if (runa.getSubnivel() < RunaAPI.getSubnivelMaximo(runa.getNivel())) {
                    runa.setSubnivel(runa.getSubnivel() + 1);
                } else {
                    s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cA runa já está no subnível máximo."));
                    target.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cA runa já está no subnível máximo."));
                    return true;
                }

                txRPG.getInstance().runasDB().salvarDadosJogadorAsync(playerData);
                s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Runa atualizada com sucesso para &c" + target.getName() + "&7."));
                target.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&7Sua runa de &c" + tipoRuna + " &7foi atualizada."));

                return true;
            }
        }
        return false;
    }

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
}
