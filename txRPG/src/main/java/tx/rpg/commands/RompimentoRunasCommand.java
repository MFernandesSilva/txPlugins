package tx.rpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tx.api.DM;
import tx.api.Mensagem;
import tx.rpg.itens.Runas;
import tx.rpg.runas.TipoRuna;
import tx.rpg.txRPG;

public class RompimentoRunasCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        DM dm = new DM();

        Player player = (Player) s;

        if (!player.hasPermission("txrpg.rompimentos.op")){
            player.sendMessage(dm.np());
            return true;
        }

        // /rompimento give <tipo> <nivel>
        if (args[0].equalsIgnoreCase("give")){
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

            Player p = (Player) s;
            ItemStack rompimento = obterRompimentoPorTipoENivel(tipoRuna, nivel);

            if (rompimento != null) {
                p.getInventory().addItem(rompimento);
                p.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&aVocê recebeu um rompimento de " + tipoRuna + " nível " + nivel + "."));
            } else {
                p.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cErro ao criar o rompimento."));
            }

            return true;
        }
        return false;
    }

    private ItemStack obterRompimentoPorTipoENivel(TipoRuna tipoRuna, int nivel) {
        Runas runas = new Runas();
        switch (tipoRuna) {
            case DANO:
                return runas.rompimentoDano[nivel - 1];
            case DEFESA:
                return runas.rompimentoDefesa[nivel - 1];
            case AMPLIFICACAO:
                return runas.rompimentoAmplificacao[nivel - 1];
            default:
                return null;
        }
    }
}
