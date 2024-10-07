package tx.rpg.commands.equipamentosUtils;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import tx.api.Mensagem;

// Classe que implementa um prefixo customizado para conversas relacionadas a equipamentos
public class CustomPrefix implements ConversationPrefix {

    @Override
    public String getPrefix(ConversationContext context) {
        // Retorna o prefixo formatado para a conversa
        return Mensagem.formatar("&c[Equipamentos] ");
    }
}
