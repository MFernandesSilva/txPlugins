
package tx.rpg.runas;

import tx.rpg.config.Config;
import tx.rpg.txRPG;

public class Runa {

    // Atributos da classe Runa
    private final TipoRuna tipo;
    private int nivel;
    private int subnivel;
    private double valorAtributo, valorAtributoProx;

    // Construtor da classe Runa
    public Runa(TipoRuna tipo, int nivel, int subnivel) {
        this.tipo = tipo;
        this.nivel = nivel;
        this.subnivel = subnivel;
        this.valorAtributo = calcularValorAtributo(txRPG.getInstance().getConfiguracao());
        this.valorAtributoProx = calcularValorAtributoProximo(txRPG.getInstance().getConfiguracao());
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

    public double getValorAtributoProx() {
        return valorAtributoProx;
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

    private double calcularValorAtributoProximo(Config config){
        int proxNivel = 0, proxSubNivel = 0;
        if (nivel >= 1 && nivel <= 4){
            if (subnivel < RunaAPI.getSubnivelMaximo(nivel)){
                proxSubNivel = subnivel + 1;
                proxNivel = nivel;
            } else {
                proxSubNivel = 1;
                proxNivel = nivel + 1;
            }
        } else if (nivel == 5) {
            if (subnivel < RunaAPI.getSubnivelMaximo(nivel)){
                proxSubNivel = subnivel + 1;
                proxNivel = nivel;
            }
        }

        String path =  "runas." + tipo.toString().toLowerCase() + ".lvl" + proxNivel + ".sublvl" + proxSubNivel;
        return config.config.getDouble(path, 0.0);
    }
}
