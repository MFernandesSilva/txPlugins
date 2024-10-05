package tx.rpg.commands.equipamentosUtils;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import tx.api.Mensagem;

public class NomeEquipamentoPrompt extends StringPrompt {

    @Override
    public String getPromptText(ConversationContext context) {
        return Mensagem.formatar( "&7Digite o nome do equipamento (pode ser colorido e com espaços):");
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        context.setSessionData("nomeEquipamento", input);

        return new AtributosPrompt("dano", Mensagem.formatar("&7Digite o dano do equipamento:"));
    }
}