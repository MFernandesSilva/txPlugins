package tx.rpg.data;

import tx.rpg.reinos.Reino;
import tx.rpg.reinos.TipoReino;

import java.util.Map;
import java.util.UUID;

public class ReinosPlayerData implements Cloneable {

    private UUID uuid;
    private String nick;
    private Map<TipoReino, Reino> reinos;

    // Construtor para inicializar os dados do jogador
    public ReinosPlayerData(UUID uuid, String nick, Map<TipoReino, Reino> reinos) {
        this.uuid = uuid;
        this.nick = nick;
        this.reinos = reinos;
    }

    // Métodos de acesso para UUID
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    // Métodos de acesso para nick
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    // Métodos de acesso para reinos
    public Map<TipoReino, Reino> getReinos() {
        return reinos;
    }

    public void setReinos(Map<TipoReino, Reino> reinos) {
        this.reinos = reinos;
    }

    // Método para adicionar um reino
    public void adicionarReino(Reino reino) {
        this.reinos.put(reino.getTipo(), reino);
    }

    @Override
    public ReinosPlayerData clone() {
        try {
            return (ReinosPlayerData) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
