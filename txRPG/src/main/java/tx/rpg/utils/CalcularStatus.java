package tx.rpg.utils;

import tx.rpg.data.PlayerData;
import tx.rpg.data.ReinosPlayerData;
import tx.rpg.data.RunasPlayerData;
import tx.rpg.reinos.Reino;
import tx.rpg.runas.Runa;

public class CalcularStatus {

    // Método para calcular os atributos do jogador
    public static void calcularAtributos(PlayerData playerData, RunasPlayerData runasPlayerData, ReinosPlayerData reinosPlayerData) {
        if (playerData == null) {
            throw new IllegalArgumentException("Dados do jogador não podem ser nulos.");
        }

        // Inicializa os atributos do jogador
        double dano = playerData.getDano();
        double defesa = playerData.getDefesa();
        int intel = playerData.getIntel();
        int ampCombate = playerData.getAmpCombate();
        int alcance = playerData.getAlcance();
        double penDefesa = playerData.getPenDefesa();
        int bloqueio = playerData.getBloqueio();
        int rouboVida = playerData.getRouboVida();
        int regenVida = playerData.getRegenVida();
        int regenMana = playerData.getRegenMana();
        int sorte = playerData.getSorte();

        // Aplica os bônus das runas nos atributos do jogador
        for (Runa runa : runasPlayerData.getRunas().values()) {
            switch (runa.getTipo()) {
                case DANO:
                    dano += runa.getValorAtributo();
                    break;
                case DEFESA:
                    defesa += runa.getValorAtributo();
                    break;
                case AMPLIFICACAO:
                    ampCombate += runa.getValorAtributo();
                    break;
            }
        }

        for (Reino reino : reinosPlayerData.getReinos().values()){
            switch (reino.getTipo()){
                case REINO:
                    dano += reino.getValorAtributo();
                    defesa += reino.getValorAtributo();
                    ampCombate += (reino.getValorAtributo() / 1000);
                    break;
            }
        }

        // Calcula os valores finais de dano e defesa
        double danoFinal = dano + ((dano / 100) * ampCombate);
        double defesaFinal = defesa + ((defesa / 100) * ampCombate);

        // Atualiza os valores finais no playerData
        playerData.setDanoFinal(danoFinal);
        playerData.setDefesaFinal(defesaFinal);
    }

    // Método para formatar números em um formato legível
    public static String formatarNumero(double valor) {
        double MIL = 1000;
        double MILHAO = 1000000;
        double BILHAO = 1000000000;
        double TRILHAO = 1000000000000L;
        double QUADRILHAO = 1000000000000000L;
        double QUINTILHAO = 1000000000000000000L;
        double SEXTILHAO = 1000000000000000000000D;
        double SEPTILHAO = 1000000000000000000000000D;
        double OCTILHAO = 1000000000000000000000000000D;

        // Formata o valor com sufixos apropriados
        if (valor < MIL) {
            return String.format("%.2f", valor);
        } else if (valor < MILHAO) {
            return String.format("%.2fK", valor / MIL);
        } else if (valor < BILHAO) {
            return String.format("%.2fM", valor / MILHAO);
        } else if (valor < TRILHAO) {
            return String.format("%.2fB", valor / BILHAO);
        } else if (valor < QUADRILHAO) {
            return String.format("%.2fT", valor / TRILHAO);
        } else if (valor < QUINTILHAO) {
            return String.format("%.2fQ", valor / QUADRILHAO);
        } else if (valor < SEXTILHAO) {
            return String.format("%.2fQQ", valor / QUINTILHAO);
        } else if (valor < SEPTILHAO) {
            return String.format("%.2fS", valor / SEXTILHAO);
        } else if (valor < OCTILHAO) {
            return String.format("%.2fSS", valor / SEPTILHAO);
        } else {
            return String.format("%.2fOC", valor / OCTILHAO);
        }
    }
}
