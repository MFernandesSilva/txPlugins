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

        // Verifica se o comando é /rompimento give <tipo> <nivel>
        if (args.length < 3 || !args[0].equalsIgnoreCase("give")) {
            player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cUso: /rompimento give <tipo> <nivel>"));
            return true;
        }

        TipoRuna tipoRuna = obterTipoRuna(s, args[1]);
        if (tipoRuna == null) {
            return true;
        }

        int nivel = obterNivel(s, args[2]);
        if (nivel == -1) {
            return true;
        }

        // Adiciona o rompimento ao inventário do jogador
        ItemStack rompimento = obterRompimentoPorTipoENivel(tipoRuna, nivel);
        if (rompimento != null) {
            player.getInventory().addItem(rompimento);
            player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&aVocê recebeu um rompimento de " + tipoRuna + " nível " + nivel + "."));
        } else {
            player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cErro ao criar o rompimento."));
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
                s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cNível inválido (1-5)."));
                return -1;
            }
            return nivel;
        } catch (NumberFormatException e) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cNível deve ser um número."));
            return -1;
        }
    }

    // Método auxiliar para obter o rompimento por tipo e nível
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
