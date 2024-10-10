package tx.rpg.reinos;

import tx.rpg.config.Config;
import tx.rpg.txRPG;

public class Reino {
    private final TipoReino tipo; // Tipo do reino
    private int nivel; // Nível do reino
    private double valorAtributo; // Valor do atributo atual
    private double valorAtributoProx; // Valor do atributo do próximo nível

    // Construtor do reino
    public Reino(TipoReino tipo, int nivel) {
        this.tipo = tipo;
        this.nivel = nivel;
        this.valorAtributo = calcularValorAtributo(txRPG.getInstance().getConfiguracao());
        this.valorAtributoProx = calcularValorAtributoProximo(txRPG.getInstance().getConfiguracao());
    }

    // Métodos de acesso

    public TipoReino getTipo() {
        return tipo;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public double getValorAtributo() {
        return valorAtributo;
    }

    public double getValorAtributoProx() {
        return valorAtributoProx;
    }

    public void setValorAtributo(double valorAtributo) {
        this.valorAtributo = valorAtributo;
    }

    // Métodos auxiliares para cálculo de valores de atributos

    private double calcularValorAtributo(Config config) {
        String path = "reinos.lvl" + nivel;
        return config.config.getDouble(path, 0.0);
    }

    private double calcularValorAtributoProximo(Config config) {
        int proxNivel = nivel + 1;
        String path = "reinos.lvl" + proxNivel;
        return config.config.getDouble(path, 0.0);
    }
}
