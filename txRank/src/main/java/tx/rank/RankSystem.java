package tx.rank;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tx.api.Mensagem;

import java.util.HashMap;
import java.util.UUID;

public class RankSystem {

    private final int xpPorLevel = 1000;
    private final HashMap<UUID, String> rankAtualJogador = new HashMap<>();

    public String getRank(Player player) {
        int xpTotal = calcularXPAtual(player);

        if (xpTotal < 1000) {
            return Mensagem.formatar( "&7Novato");
        } else if (xpTotal < 5000) {
            return Mensagem.formatar("&aGuerreiro");
        } else if (xpTotal < 10000) {
            return Mensagem.formatar("&9Campeao");
        } else if (xpTotal < 20000) {
            return Mensagem.formatar("&cMestre");
        } else {
            return Mensagem.formatar("&6DEUS");
        }
    }

    public void verificarRank(Player player) {
        String rankAtual = getRank(player);
        UUID playerUUID = player.getUniqueId();

        if (!rankAtualJogador.containsKey(playerUUID)) {
            rankAtualJogador.put(playerUUID, rankAtual);
            return;
        }

        String rankAnterior = rankAtualJogador.get(playerUUID);

        if (!rankAnterior.equals(rankAtual)) {
            rankAtualJogador.put(playerUUID, rankAtual);
            anunciarRank(player, rankAtual);
        }
    }

    private void anunciarRank(Player player, String novoRank) {
        String mensagem = "§e" + player.getName() + " §7subiu para o rank " + novoRank + "§7!";
        for (Player players : Bukkit.getOnlinePlayers()){
            players.sendMessage(mensagem);
        }
    }

    private int calcularXPAtual(Player player) {
        int nivelAtual = player.getLevel();
        float progressoNoNivel = player.getExp();
        return (nivelAtual * xpPorLevel) + Math.round(progressoNoNivel * xpPorLevel);
    }
}
