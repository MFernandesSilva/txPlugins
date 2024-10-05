package tx.rpg.commands.equipamentosUtils;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import tx.api.Mensagem;

public class CustomPrefix implements ConversationPrefix {

    @Override
    public String getPrefix(ConversationContext context) {
        return Mensagem.formatar("&7[&cEquipamentos&7] ");
    }
}