package tx.rpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tx.api.DM;
import tx.api.Mensagem;
import tx.rpg.itens.Reinos;
import tx.rpg.txRPG;

public class RompimentoReinoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DM dm = new DM();

        // Verifica se o remetente é um jogador
        if (!(sender instanceof Player)) {
            sender.sendMessage(dm.cc());
            return true;
        }

        Player player = (Player) sender;

        // Verifica se o jogador tem permissão para usar o comando
        if (!player.hasPermission("txrpg.admin")) {
            player.sendMessage(dm.np());
            return true;
        }

        // Verifica se o comando é /rompimentoreinos give <tipo>
        if (!isValidCommandUsage(player, args)) {
            return true;
        }

        String tipo = args[1].toLowerCase();

        // Adiciona o rompimento ao inventário do jogador
        ItemStack rompimento = getRompimentoByType(tipo);
        if (rompimento != null) {
            player.getInventory().addItem(rompimento);
            player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&aVocê recebeu um rompimento do tipo " + tipo + "."));
        } else {
            player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cErro ao criar o rompimento."));
        }

        return true;
    }

    // Método auxiliar para verificar o uso correto do comando
    private boolean isValidCommandUsage(Player player, String[] args) {
        if (args.length < 2 || !args[0].equalsIgnoreCase("give")) {
            player.sendMessage(Mensagem.formatar(txRPG.getInstance().getConfiguracao().getPrefix() + "&cUso: /rompimentoreinos give <tipo>"));
            return false;
        }
        return true;
    }

    // Método auxiliar para obter o rompimento por tipo
    private ItemStack getRompimentoByType(String tipo) {
        switch (tipo) {
            case "mortal":
                return new Reinos().rompimentoMortal[19]; // Exemplo, retorna o primeiro rompimento mortal
            case "combate":
                return new Reinos().rompimentoDeCombate[34]; // Exemplo, retorna o primeiro rompimento de combate
            case "celestial":
                return new Reinos().rompimentoCelestial[44]; // Exemplo, retorna o primeiro rompimento celestial
            case "imortal":
                return new Reinos().rompimentoImortal[49]; // Exemplo, retorna o primeiro rompimento imortal
            case "deus":
                return new Reinos().rompimentoDeus[52]; // Exemplo, retorna o primeiro rompimento deus
            default:
                return null; // Retorna null se o tipo não for válido
        }
    }
}
