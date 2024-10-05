package tx.rpg.data;

import tx.rpg.runas.Runa;
import tx.rpg.runas.TipoRuna;

import java.util.Map;
import java.util.UUID;

public class RunasPlayerData implements Cloneable{
    private final UUID uuid;
    private String nick;

    private Map<TipoRuna, Runa> runas;

    public RunasPlayerData(UUID uuid, String nick, Map<TipoRuna, Runa> runas) {
        this.uuid = uuid;
        this.nick = nick;
        this.runas = runas;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Map<TipoRuna, Runa> getRunas() {
        return runas;
    }

    public void setRunas(Map<TipoRuna, Runa> runas) {
        this.runas = runas;
    }

    public void adicionarRuna(Runa runa){ this.runas = runas; }

    @Override
    public RunasPlayerData clone() {
        try {
            return (RunasPlayerData) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
