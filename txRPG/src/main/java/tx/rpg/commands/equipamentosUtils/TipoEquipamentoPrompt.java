package tx.rpg.commands.equipamentosUtils;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import tx.api.Mensagem;

// Classe que gerencia a solicitação do tipo de equipamento
public class TipoEquipamentoPrompt extends ValidatingPrompt {

    @Override
    public String getPromptText(ConversationContext context) {
        // Retorna a mensagem que solicita o tipo de equipamento
        return Mensagem.formatar("&7Digite o tipo de equipamento (espada, capacete, peitoral, calça ou bota):");
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        // Tipos válidos de equipamento
        String[] tiposValidos = {"espada", "capacete", "peitoral", "calça", "bota"};

        // Verifica se o input é um dos tipos válidos
        for (String tipo : tiposValidos) {
            if (input.equalsIgnoreCase(tipo)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String input) {
        // Armazena o tipo de equipamento na sessão
        context.setSessionData("tipoEquipamento", input.toLowerCase());

        // Retorna o próximo prompt para solicitar o nome do equipamento
        return new NomeEquipamentoPrompt();
    }

    @Override
    protected String getFailedValidationText(ConversationContext context, String invalidInput) {
        // Retorna a mensagem de erro caso o tipo de equipamento seja inválido
        return Mensagem.formatar("&cTipo de equipamento inválido. Por favor, digite um dos seguintes: espada, capacete, peitoral, calça ou bota.");
    }
}
