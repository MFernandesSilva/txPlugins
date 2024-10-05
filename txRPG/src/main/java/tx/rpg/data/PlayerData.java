package tx.rpg.data;

import java.util.UUID;

public class PlayerData implements Cloneable {

    private final UUID uuid;
    private String nick;
    private double dano;
    private double defesa;
    private int intel;
    private int ampCombate;
    private int alcance;
    private double penDefesa;
    private int bloqueio;
    private int rouboVida;
    private int regenVida;
    private int regenMana;
    private int sorte;
    private double danoFinal;
    private double defesaFinal;

    public PlayerData(UUID uuid, String nick, double dano, double defesa, int intel, int ampCombate, int alcance, double penDefesa, int bloqueio, int rouboVida, int regenVida, int regenMana, int sorte, double danoFinal, double defesaFinal){
        this.uuid = uuid;
        this.nick = nick;
        this.dano = dano;
        this.defesa = defesa;
        this.intel = intel;
        this.ampCombate = ampCombate;
        this.alcance = alcance;
        this.penDefesa = penDefesa;
        this.bloqueio = bloqueio;
        this.rouboVida = rouboVida;
        this.regenVida = regenVida;
        this.regenMana = regenMana;
        this.sorte = sorte;
        this.danoFinal = danoFinal;
        this.defesaFinal = defesaFinal;
    }

    // Getters

    public UUID getUuid() {
        return uuid;
    }

    public String getNick() {
        return nick;
    }

    public double getDano() {
        return dano;
    }

    public double getDefesa() {
        return defesa;
    }

    public int getIntel() {
        return intel;
    }

    public int getAmpCombate() {
        return ampCombate;
    }

    public int getAlcance() {
        return alcance;
    }

    public double getPenDefesa() {
        return penDefesa;
    }

    public int getBloqueio() {
        return bloqueio;
    }

    public int getRouboVida() {
        return rouboVida;
    }

    public int getRegenVida() {
        return regenVida;
    }

    public int getRegenMana() {
        return regenMana;
    }

    public int getSorte() {
        return sorte;
    }

    public double getDanoFinal() {
        return danoFinal;
    }

    public double getDefesaFinal() {
        return defesaFinal;
    }

    // Setters


    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setDano(double dano) {
        this.dano = dano;
    }

    public void setDefesa(double defesa) {
        this.defesa = defesa;
    }

    public void setIntel(int intel) {
        this.intel = intel;
    }

    public void setAmpCombate(int ampCombate) {
        this.ampCombate = ampCombate;
    }

    public void setAlcance(int alcance) {
        this.alcance = alcance;
    }

    public void setPenDefesa(double penDefesa) {
        this.penDefesa = penDefesa;
    }

    public void setBloqueio(int bloqueio) {
        this.bloqueio = bloqueio;
    }

    public void setRouboVida(int rouboVida) {
        this.rouboVida = rouboVida;
    }

    public void setRegenVida(int regenVida) {
        this.regenVida = regenVida;
    }

    public void setRegenMana(int regenMana) {
        this.regenMana = regenMana;
    }

    public void setSorte(int sorte) {
        this.sorte = sorte;
    }

    public void setDanoFinal(double danoFinal) {
        this.danoFinal = danoFinal;
    }

    public void setDefesaFinal(double defesaFinal) {
        this.defesaFinal = defesaFinal;
    }

    @Override
    public PlayerData clone(){
        try {
            PlayerData cloned = (PlayerData) super.clone();
            return cloned;
        } catch (CloneNotSupportedException e){
            throw new AssertionError(); // Se acontecer, fudeo.
        }
    }
}
