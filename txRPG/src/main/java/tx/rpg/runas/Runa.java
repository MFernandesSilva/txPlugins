package tx.rpg.runas;

import tx.rpg.config.Config;
import tx.rpg.txRPG;

public class Runa {

    // Atributos da classe Runa
    private final TipoRuna tipo;
    private int nivel;
    private int subnivel;
    private double valorAtributo;

    // Construtor da classe Runa
    public Runa(TipoRuna tipo, int nivel, int subnivel) {
        this.tipo = tipo;
        this.nivel = nivel;
        this.subnivel = subnivel;
        this.valorAtributo = calcularValorAtributo(txRPG.getInstance().getConfiguracao());
    }

    // Getters
    public TipoRuna getTipo() {
        return tipo;
    }

    public int getNivel() {
        return nivel;
    }

    public int getSubnivel() {
        return subnivel;
    }

    public double getValorAtributo() {
        return valorAtributo;
    }

    // Setters
    public void setNivel(int nivel) {
        this.nivel = nivel;
        this.valorAtributo = calcularValorAtributo(txRPG.getInstance().getConfiguracao());
    }

    public void setSubnivel(int subnivel) {
        this.subnivel = subnivel;
        this.valorAtributo = calcularValorAtributo(txRPG.getInstance().getConfiguracao());
    }

    // Método para calcular o valor do atributo
    private double calcularValorAtributo(Config config) {
        String path = "runas." + tipo.toString().toLowerCase() + ".lvl" + nivel + ".sublvl" + subnivel;
        return config.config.getDouble(path, 0.0);
    }
}
