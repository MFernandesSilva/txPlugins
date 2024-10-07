package tx.rpg.data;

import tx.rpg.runas.Runa;
import tx.rpg.runas.TipoRuna;

import java.util.Map;
import java.util.UUID;

public class RunasPlayerData implements Cloneable {
    // Identificador único do jogador
    private final UUID uuid;
    // Nome do jogador
    private String nick;
    // Mapa de runas do jogador
    private Map<TipoRuna, Runa> runas;

    // Construtor da classe
    public RunasPlayerData(UUID uuid, String nick, Map<TipoRuna, Runa> runas) {
        this.uuid = uuid;
        this.nick = nick;
        this.runas = runas;
    }

    // Método para obter o UUID do jogador
    public UUID getUuid() {
        return uuid;
    }

    // Método para obter o nome do jogador
    public String getNick() {
        return nick;
    }

    // Método para definir o nome do jogador
    public void setNick(String nick) {
        this.nick = nick;
    }

    // Método para obter as runas do jogador
    public Map<TipoRuna, Runa> getRunas() {
        return runas;
    }

    // Método para definir as runas do jogador
    public void setRunas(Map<TipoRuna, Runa> runas) {
        this.runas = runas;
    }

    // Método para adicionar uma runa (o método não estava implementado corretamente)
    public void adicionarRuna(Runa runa) {
        this.runas.put(runa.getTipo(), runa);
    }

    // Método para clonar o objeto RunasPlayerData
    @Override
    public RunasPlayerData clone() {
        try {
            return (RunasPlayerData) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
