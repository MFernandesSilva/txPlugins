package tx.rpg.commands.equipamentosUtils;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import tx.api.Mensagem;

public class AtributosPrompt extends NumericPrompt {

    private final String atributo;
    private final String pergunta;

    public AtributosPrompt(String atributo, String pergunta) {
        this.atributo = atributo;
        this.pergunta = pergunta;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return pergunta;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
        context.setSessionData(atributo, input.doubleValue());

        switch (atributo) {
            case "dano":
                return new AtributosPrompt("defesa", Mensagem.formatar("&7Digite a defesa do equipamento:"));
            case "defesa":
                return new AtributosPrompt("intel", Mensagem.formatar("&7Digite a inteligência de crítico do equipamento:"));
            case "intel":
                return new AtributosPrompt("ampCombate", Mensagem.formatar("&7Digite a amplificação de combate do equipamento:"));
            case "ampCombate":
                return new AtributosPrompt("alcance", Mensagem.formatar("&7Digite o alcance do equipamento:"));
            case "alcance":
                return new AtributosPrompt("penDefesa", Mensagem.formatar("&7Digite a penetração de defesa do equipamento:"));
            case "penDefesa":
                return new AtributosPrompt("bloqueio", Mensagem.formatar("&7Digite o bloqueio do equipamento:"));
            case "bloqueio":
                return new AtributosPrompt("rouboVida", Mensagem.formatar("&7Digite o roubo de vida do equipamento:"));
            case "rouboVida":
                return new AtributosPrompt("regenVida", Mensagem.formatar("&7Digite a regeneração de vida do equipamento:"));
            case "regenVida":
                return new AtributosPrompt("regenMana", Mensagem.formatar("&7Digite a regeneração de mana do equipamento:"));
            case "regenMana":
                return new AtributosPrompt("sorte", Mensagem.formatar("&7Digite a sorte do equipamento:"));
            case "sorte":
                return new AtributosPrompt("aoe", Mensagem.formatar("&7Digite o AOE do equipamento:"));
            case "aoe":
                return new ConfirmacaoPrompt();
            default:
                return Prompt.END_OF_CONVERSATION;
        }
    }

    @Override
    protected String getFailedValidationText(ConversationContext context, String invalidInput) {
        return Mensagem.formatar("Valor inválido. Por favor, digite um número.");
    }
}