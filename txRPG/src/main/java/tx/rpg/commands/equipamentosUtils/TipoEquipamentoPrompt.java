package tx.rpg.commands.equipamentosUtils;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import tx.api.Mensagem;

public class TipoEquipamentoPrompt extends ValidatingPrompt {

    @Override
    public String getPromptText(ConversationContext context) {
        return Mensagem.formatar( "&7Digite o tipo de equipamento (espada, capacete, peitoral, calça ou bota):");
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        String[] tiposValidos = {"espada", "capacete", "peitoral", "calça", "bota"};

        for (String tipo : tiposValidos) {
            if (input.equalsIgnoreCase(tipo)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String input) {
        context.setSessionData("tipoEquipamento", input.toLowerCase());

        return new NomeEquipamentoPrompt();
    }

    @Override
    protected String getFailedValidationText(ConversationContext context, String invalidInput) {
        return Mensagem.formatar("&cTipo de equipamento inválido. Por favor, digite um dos seguintes: espada, capacete, peitoral, calça ou bota.");
    }
}