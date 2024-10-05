package tx.rank.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tx.rank.RankSystem;

public class XPCommand implements CommandExecutor {

    private final int xpPorLevel = 1000;
    private final RankSystem rankSystem = new RankSystem();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 3) {
            sender.sendMessage("Uso incorreto! /exp <add/remove/set> <quantidade> <player>");
            return false;
        }

        String action = args[0];
        int quantidade;

        try {
            quantidade = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("A quantidade deve ser um número!");
            return false;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage("Jogador não encontrado.");
            return false;
        }

        switch (action.toLowerCase()) {
            case "add":
                adicionarXP(target, quantidade);
                sender.sendMessage("Você adicionou " + quantidade + " XP a " + target.getName());
                rankSystem.verificarRank(target);
                break;
            case "remove":
                removerXP(target, quantidade);
                sender.sendMessage("Você removeu " + quantidade + " XP de " + target.getName());
                rankSystem.verificarRank(target);
                break;
            case "set":
                definirXP(target, quantidade);
                sender.sendMessage("Você definiu o XP de " + target.getName() + " para " + quantidade);
                rankSystem.verificarRank(target);
                break;
            default:
                sender.sendMessage("Ação inválida! Use add, remove ou set.");
                return false;
        }
        return true;
    }

    private void adicionarXP(Player player, int quantidade) {
        int xpAtual = calcularXPAtual(player);
        int novoXP = xpAtual + quantidade;
        definirXP(player, novoXP);
    }

    private void removerXP(Player player, int quantidade) {
        int xpAtual = calcularXPAtual(player);
        int novoXP = Math.max(0, xpAtual - quantidade);
        definirXP(player, novoXP);
    }

    private void definirXP(Player player, int quantidade) {
        int novoNivel = quantidade / xpPorLevel;
        int xpRestante = quantidade % xpPorLevel;

        player.setLevel(novoNivel);
        player.setExp(xpRestante / (float) xpPorLevel);
    }

    private int calcularXPAtual(Player player) {
        int nivelAtual = player.getLevel();
        float progressoNoNivel = player.getExp();
        return (nivelAtual * xpPorLevel) + Math.round(progressoNoNivel * xpPorLevel);
    }
}