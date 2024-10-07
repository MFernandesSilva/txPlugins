package tx.rpg.runas;

import org.bukkit.entity.Player;
import tx.rpg.data.RunasPlayerData;
import tx.rpg.txRPG;

public class RunaAPI {

    // Método para adicionar uma runa ao jogador
    public static void adicionarRuna(Player player, TipoRuna tipoRuna, int nivel, int subnivel) {
        RunasPlayerData playerData = txRPG.getInstance().getRunasPlayerData().get(player.getUniqueId());

        if (playerData != null) {
            Runa novaRuna = new Runa(tipoRuna, nivel, subnivel);
            playerData.adicionarRuna(novaRuna);
        }
    }

    // Método para remover uma runa do jogador
    public static void removerRuna(Player player, TipoRuna tipoRuna) {
        RunasPlayerData playerData = txRPG.getInstance().getRunasPlayerData().get(player.getUniqueId());

        if (playerData != null) {
            playerData.getRunas().remove(tipoRuna);
        }
    }

    // Método para atualizar os níveis de uma runa do jogador
    public static void atualizarRuna(Player player, TipoRuna tipoRuna, int novoNivel, int novoSubNivel) {
        RunasPlayerData playerData = txRPG.getInstance().getRunasPlayerData().get(player.getUniqueId());

        if (playerData != null) {
            Runa runa = playerData.getRunas().get(tipoRuna);

            if (runa != null) {
                runa.setNivel(novoNivel);
                runa.setSubnivel(novoSubNivel);
            }
        }
    }

    // Método para verificar se uma runa pode evoluir
    public static boolean podeEvoluirRuna(Player player, TipoRuna tipoRuna) {
        RunasPlayerData playerData = txRPG.getInstance().getRunasPlayerData().get(player.getUniqueId());

        if (playerData != null) {
            Runa runa = playerData.getRunas().get(tipoRuna);

            if (runa != null) {
                int nivelMaximo = getNivelMaximo(runa.getNivel());
                int subnivelMaximo = getSubnivelMaximo(runa.getNivel());
                return runa.getNivel() < 5 && runa.getSubnivel() == subnivelMaximo;
            }
        }
        return false;
    }

    // Método para obter o nível máximo com base no nível atual
    public static int getNivelMaximo(int nivelAtual) {
        switch (nivelAtual) {
            case 0:
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            default:
                return 5;
        }
    }

    // Método para obter o subnível máximo com base no nível atual
    public static int getSubnivelMaximo(int nivelAtual) {
        switch (nivelAtual) {
            case 0:
                return 0;
            case 1:
                return 20;
            case 2:
                return 15;
            case 3:
                return 10;
            case 4:
                return 5;
            default:
                return 3;
        }
    }
}
