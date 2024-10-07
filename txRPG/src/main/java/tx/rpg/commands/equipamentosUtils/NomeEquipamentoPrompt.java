package tx.rpg.commands.equipamentosUtils;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import tx.api.Mensagem;

// Classe que gerencia a solicitação do nome do equipamento
public class NomeEquipamentoPrompt extends StringPrompt {

    @Override
    public String getPromptText(ConversationContext context) {
        // Retorna a mensagem que solicita o nome do equipamento
        return Mensagem.formatar("&7Digite o nome do equipamento (pode ser colorido e com espaços):");
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        // Armazena o nome do equipamento na sessão
        context.setSessionData("nomeEquipamento", input);

        // Retorna o próximo prompt para solicitar os atributos do equipamento
        return new AtributosPrompt("dano", Mensagem.formatar("&7Digite o dano do equipamento:"));
    }
}
