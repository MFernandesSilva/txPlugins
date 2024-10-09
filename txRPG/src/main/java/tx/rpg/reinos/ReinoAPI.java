package tx.rpg.reinos;

import org.bukkit.entity.Player;
import tx.rpg.data.ReinosPlayerData;
import tx.rpg.txRPG;

public class ReinoAPI {

    public static void adicionarReino(Player player, TipoReino tipoReino, int nivel){
        ReinosPlayerData playerData = txRPG.getInstance().getReinosPlayerData().get(player.getUniqueId());

        if (playerData != null){
            Reino novoReino = new Reino(tipoReino, nivel);
            playerData.adicionarReino(novoReino);
        }
    }

    public static void removerReino(Player player, TipoReino tipoReino){
        ReinosPlayerData playerData = txRPG.getInstance().getReinosPlayerData().get(player.getUniqueId());

        if (playerData != null){
            playerData.getReinos().remove(tipoReino);
        }
    }

    public static void atualizarReino(Player player, TipoReino tipoReino, int novoNivel){
        ReinosPlayerData playerData = txRPG.getInstance().getReinosPlayerData().get(player.getUniqueId());

        if (playerData != null){
            Reino reino = playerData.getReinos().get(tipoReino);

            if (reino != null){
                reino.setNivel(novoNivel);
            }
        }
    }

    public static boolean podeEvoluirReino(Player player, TipoReino tipoReino){
        ReinosPlayerData playerData = txRPG.getInstance().getReinosPlayerData().get(player.getUniqueId());

        if (playerData != null){
            Reino reino = playerData.getReinos().get(tipoReino);

            if (reino != null){
                int nivelMaximo = getNivelMaximo(reino.getNivel());
                if (nivelMaximo == 20 && reino.getNivel() == 20){
                    return true;
                } else if (nivelMaximo == 35 && reino.getNivel() == 35){
                    return true;
                } else if (nivelMaximo == 45 && reino.getNivel() == 45) {
                    return true;
                } else if (nivelMaximo == 50 && reino.getNivel() == 50) {
                    return true;
                }
            }
        }
        return false;
    }

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

}
