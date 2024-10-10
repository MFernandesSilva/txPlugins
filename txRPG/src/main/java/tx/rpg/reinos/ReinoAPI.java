package tx.rpg.reinos;

import org.bukkit.entity.Player;
import tx.rpg.data.ReinosPlayerData;
import tx.rpg.txRPG;

public class ReinoAPI {

    // Adiciona um novo reino ao jogador
    public static void adicionarReino(Player player, TipoReino tipoReino, int nivel) {
        ReinosPlayerData playerData = obterDadosDoJogador(player);

        if (playerData != null) {
            Reino novoReino = new Reino(tipoReino, nivel);
            playerData.adicionarReino(novoReino);
        }
    }

    // Remove um reino do jogador
    public static void removerReino(Player player, TipoReino tipoReino) {
        ReinosPlayerData playerData = obterDadosDoJogador(player);

        if (playerData != null) {
            playerData.getReinos().remove(tipoReino);
        }
    }

    // Atualiza o nível de um reino do jogador
    public static void atualizarReino(Player player, TipoReino tipoReino, int novoNivel) {
        ReinosPlayerData playerData = obterDadosDoJogador(player);

        if (playerData != null) {
            Reino reino = playerData.getReinos().get(tipoReino);
            if (reino != null) {
                reino.setNivel(novoNivel);
            }
        }
    }

    // Verifica se o jogador pode evoluir seu reino
    public static boolean podeEvoluirReino(Player player, TipoReino tipoReino) {
        ReinosPlayerData playerData = obterDadosDoJogador(player);

        if (playerData != null) {
            Reino reino = playerData.getReinos().get(tipoReino);
            if (reino != null) {
                return podeEvoluir(reino);
            }
        }
        return false;
    }

    // Obtém o nível máximo baseado no nível atual
    public static int getNivelMaximo(int nivelAtual) {
        if (nivelAtual >= 1 && nivelAtual <= 20) {
            return 20;
        } else if (nivelAtual >= 21 && nivelAtual <= 35) {
            return 35;
        } else if (nivelAtual >= 36 && nivelAtual <= 45) {
            return 45;
        } else if (nivelAtual >= 46 && nivelAtual <= 50) {
            return 50;
        } else if (nivelAtual >= 51 && nivelAtual <= 53) {
            return 53;
        }
        return 20;
    }

    // Métodos auxiliares

    // Obtém os dados do jogador
    private static ReinosPlayerData obterDadosDoJogador(Player player) {
        return txRPG.getInstance().getReinosPlayerData().get(player.getUniqueId());
    }

    // Verifica se o reino pode ser evoluído
    private static boolean podeEvoluir(Reino reino) {
        int nivelMaximo = getNivelMaximo(reino.getNivel());
        return reino.getNivel() == nivelMaximo;
    }
}
