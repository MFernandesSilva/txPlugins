package tx.rpg.config;

import org.bukkit.configuration.file.FileConfiguration;
import tx.rpg.txRPG;

public class Config {

    public FileConfiguration config;

    public Config(){
        this.config = txRPG.getInstance().getConfig();
        loadConfiguration();

    }

    public void loadConfiguration(){
        txRPG.getInstance().saveDefaultConfig();
        config = txRPG.getInstance().getConfig();
    }

    public String getPrefix(){ return config.getString("prefix"); }
    public double getDanoPadrao(){ return config.getDouble("atributos_iniciais.dano"); }
    public double getDefesaPadrao(){ return config.getDouble("atributos_iniciais.defesa"); }
    public int getIntelPadrao(){ return config.getInt("atributos_iniciais.intel"); }
    public int getAmpCombatePadrao(){ return config.getInt("atributos_iniciais.ampCombate"); }
    public int getAlcancePadrao(){ return config.getInt("atributos_iniciais.alcance"); }
    public double getPenDefesaPadrao(){ return config.getDouble("atributos_iniciais.penDefesa"); }
    public int getBloqueioPadrao(){ return config.getInt("atributos_iniciais.bloqueio"); }
    public int getRouboVidaPadrao(){ return config.getInt("atributos_iniciais.rouboVida"); }
    public int getRegenVidaPadrao(){ return config.getInt("atributos_iniciais.regenVida"); }
    public int getRegenManaPadrao(){ return config.getInt("atributos_iniciais.regenMana"); }
    public int getSortePadrao(){ return config.getInt("atributos_iniciais.sorte"); }


    public String getMensagemAtributos() { return config.getString("mensagens.atributos"); }
    public String getMensagemErroAtributos() { return config.getString("mensagens.erro_atributos"); }
    public String getMensagemVerAtributos() { return config.getString("mensagens.ver_atributos"); }
    public String getMensagemErroVerAtributos() { return config.getString("mensagens.erro_ver_atributos"); }
    public String getMensagemAtributosAlterados() { return config.getString("mensagens.atributos_alterados"); }
    public String getMensagemSeusAtributosAlterados() { return config.getString("mensagens.seus_atributos_alterados"); }
    public String getMensagemErroAtributosAlterados() { return config.getString("mensagens.erro_atributos_alterados"); }
    public String getMensagemUsoTxAtributos() { return config.getString("mensagens.uso_txatributos"); }


}
