package tx.rank.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tx.rank.RankSystem;

public class RankCommand implements CommandExecutor {

    private final RankSystem rankSystem = new RankSystem();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return false;
        }

        Player player = (Player) sender;
        String rank = rankSystem.getRank(player);

        player.sendMessage("Seu rank atual é: " + rank);
        return true;
    }
}
