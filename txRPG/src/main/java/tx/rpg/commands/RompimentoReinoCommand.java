package tx.rpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tx.api.DM;
import tx.api.Mensagem;
import tx.rpg.itens.Reinos;
import tx.rpg.itens.Runas;
import tx.rpg.runas.TipoRuna;
import tx.rpg.txRPG;

public class RompimentoReinoCommand implements CommandExecutor {

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

        // Verifica se o comando é /rompimento give <nivel>
        if (args.length < 2 || !args[0].equalsIgnoreCase("give")) {
            player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cUso: /rompimento give <tipo> <nivel>"));
            return true;
        }

        int nivel = obterNivel(s, args[1]);
        if (nivel == -1) {
            return true;
        }

        // Adiciona o rompimento ao inventário do jogador
        ItemStack rompimento = obterRompimentoPorTipoENivel(nivel);
        if (rompimento != null) {
            player.getInventory().addItem(rompimento);
            player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&aVocê recebeu um rompimento nível " + nivel + "."));
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
            if (nivel < 1 || nivel > 53) {
                s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cNível inválido."));
                return -1;
            }
            return nivel;
        } catch (NumberFormatException e) {
            s.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cNível deve ser um número."));
            return -1;
        }
    }

    // Método auxiliar para obter o rompimento por tipo e nível
    private ItemStack obterRompimentoPorTipoENivel(int nivel) {

        Reinos reinos = new Reinos();
        if (nivel >= 1 && nivel <= 20){
            return reinos.rompimentoMortal[nivel - 1];
        } else if (nivel >= 21 && nivel <= 35) {
            return reinos.rompimentoDeCombate[nivel - 1];
        } else if (nivel >= 36 && nivel <= 45) {
            return reinos.rompimentoCelestial[nivel - 1];
        } else if (nivel >= 46 && nivel <= 50){
            return reinos.rompimentoImortal[nivel - 1];
        } else if (nivel >= 51 && nivel <= 53) {
            return reinos.rrompimentoDeus[nivel - 1];
        } else {
            return null;
        }
    }
}
