package tx.rpg.commands.equipamentosUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tx.api.Item;
import tx.api.Mensagem;
import tx.rpg.utils.CalcularStatus;

import java.util.ArrayList;
import java.util.List;

public class ConfirmacaoPrompt extends ValidatingPrompt {

    @Override
    public String getPromptText(ConversationContext context) {
        // Obtém os dados da sessão da conversa
        String tipoEquipamento = (String) context.getSessionData("tipoEquipamento");
        String nomeEquipamento = (String) context.getSessionData("nomeEquipamento");
        String nomeComCores = ChatColor.translateAlternateColorCodes('&', nomeEquipamento);
        double dano = (double) context.getSessionData("dano");
        double defesa = (double) context.getSessionData("defesa");
        double intel = (double) context.getSessionData("intel");
        double ampCombate = (double) context.getSessionData("ampCombate");
        double alcance = (double) context.getSessionData("alcance");
        double penDefesa = (double) context.getSessionData("penDefesa");
        double bloqueio = (double) context.getSessionData("bloqueio");
        double rouboVida = (double) context.getSessionData("rouboVida");
        double regenVida = (double) context.getSessionData("regenVida");
        double regenMana = (double) context.getSessionData("regenMana");
        double sorte = (double) context.getSessionData("sorte");

        // Formata a mensagem de confirmação com os dados coletados
        String mensagem = Mensagem.formatar("&7Confirme os dados do equipamento:\n" +
                "&eTipo: &7" + tipoEquipamento + "\n" +
                "&eNome: " + nomeComCores + "\n" +
                "&cDano: &7" + CalcularStatus.formatarNumero(dano) + "\n" +
                "&aDefesa: &7" + CalcularStatus.formatarNumero(defesa) + "\n" +
                "&bInteligência: &7" + CalcularStatus.formatarNumero(intel) + "\n" +
                "&bAmplificação de Combate: &7" + CalcularStatus.formatarNumero(ampCombate) + "\n" +
                "&cAlcance: &7" + CalcularStatus.formatarNumero(alcance) + "\n" +
                "&bPenetração de Defesa: &7" + CalcularStatus.formatarNumero(penDefesa) + "\n" +
                "&aBloqueio: &7" + CalcularStatus.formatarNumero(bloqueio) + "\n" +
                "&cRoubo de Vida: &7" + CalcularStatus.formatarNumero(rouboVida) + "\n" +
                "&aRegeneração de Vida: &7" + CalcularStatus.formatarNumero(regenVida) + "\n" +
                "&bRegeneração de Mana: &7" + CalcularStatus.formatarNumero(regenMana) + "\n" +
                "&bSorte: &7" + CalcularStatus.formatarNumero(sorte) + "\n" +
                "&7Digite 'sim' para criar o equipamento ou 'não' para cancelar.");

        return mensagem;
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        return input.equalsIgnoreCase("sim") || input.equalsIgnoreCase("não");
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String input) {
        if (input.equalsIgnoreCase("sim")) {
            criarEquipamento(context);

            context.getForWhom().sendRawMessage(Mensagem.formatar("&aEquipamento criado com sucesso!"));
        } else {
            context.getForWhom().sendRawMessage(Mensagem.formatar("&cCriação de equipamento cancelada."));
        }

        // Encerra a conversa
        return Prompt.END_OF_CONVERSATION;
    }

    private void criarEquipamento(ConversationContext context) {
        Player player = (Player) context.getForWhom();

        String tipoEquipamento = (String) context.getSessionData("tipoEquipamento");
        String nomeEquipamento = (String) context.getSessionData("nomeEquipamento");
        String nomeComCores = ChatColor.translateAlternateColorCodes('&', nomeEquipamento);
        double dano = (double) context.getSessionData("dano");
        double defesa = (double) context.getSessionData("defesa");
        double intel = (double) context.getSessionData("intel");
        double ampCombate = (double) context.getSessionData("ampCombate");
        double alcance = (double) context.getSessionData("alcance");
        double penDefesa = (double) context.getSessionData("penDefesa");
        double bloqueio = (double) context.getSessionData("bloqueio");
        double rouboVida = (double) context.getSessionData("rouboVida");
        double regenVida = (double) context.getSessionData("regenVida");
        double regenMana = (double) context.getSessionData("regenMana");
        double sorte = (double) context.getSessionData("sorte");

        Material material;
        switch (tipoEquipamento) {
            case "espada":
                material = Material.DIAMOND_SWORD;
                break;
            case "capacete":
                material = Material.DIAMOND_HELMET;
                break;
            case "peitoral":
                material = Material.DIAMOND_CHESTPLATE;
                break;
            case "calça":
                material = Material.DIAMOND_LEGGINGS;
                break;
            case "bota":
                material = Material.DIAMOND_BOOTS;
                break;
            default:
                player.sendMessage(Mensagem.formatar("&cErro ao criar o equipamento: tipo inválido."));
                return;
        }


        List<String> lore = new ArrayList<>();

        double danoNBT, defesaNBT, intelNBT, ampCombateNBT, alcanceNBT, penDefesaNBT, bloqueioNBT, rouboVidaNBT, regenVidaNBT, regenManaNBT, sorteNBT;

        try {
            danoNBT = Double.parseDouble(String.valueOf(dano));
            defesaNBT = Double.parseDouble(String.valueOf(defesa));
            intelNBT = Double.parseDouble(String.valueOf(intel));
            ampCombateNBT = Double.parseDouble(String.valueOf(ampCombate));
            alcanceNBT = Double.parseDouble(String.valueOf(alcance));
            penDefesaNBT = Double.parseDouble(String.valueOf(penDefesa));
            bloqueioNBT = Double.parseDouble(String.valueOf(bloqueio));
            rouboVidaNBT = Double.parseDouble(String.valueOf(rouboVida));
            regenVidaNBT = Double.parseDouble(String.valueOf(regenVida));
            regenManaNBT = Double.parseDouble(String.valueOf(regenMana));
            sorteNBT = Double.parseDouble(String.valueOf(sorte));

        } catch (NumberFormatException e){
            Bukkit.getConsoleSender().sendMessage("erro");
            return;
        }

        {
            lore.add(" ");
            if (dano > 0) {
                lore.add(Mensagem.formatar("&cDano: &7" + CalcularStatus.formatarNumero(dano)));
            }
            if (alcance > 0) {
                lore.add(Mensagem.formatar("&cAlcance: &7" + CalcularStatus.formatarNumero(alcance)));
            }
            if (rouboVida > 0) {
                lore.add(Mensagem.formatar("&cRoubo de Vida: &7" + CalcularStatus.formatarNumero(rouboVida)));
            }
            if (defesa > 0) {
                lore.add(Mensagem.formatar("&aDefesa: &7" + CalcularStatus.formatarNumero(defesa)));
            }
            if (bloqueio > 0) {
                lore.add(Mensagem.formatar("&aBloqueio: &7" + CalcularStatus.formatarNumero(bloqueio)));
            }
            if (regenVida > 0) {
                lore.add(Mensagem.formatar("&aRegeneração de Vida: &7" + CalcularStatus.formatarNumero(regenVida)));
            }
            if (intel > 0) {
                lore.add(Mensagem.formatar("&bInteligência: &7" + CalcularStatus.formatarNumero(intel)));
            }
            if (ampCombate > 0) {
                lore.add(Mensagem.formatar("&bAmplificação de Combate: &7" + CalcularStatus.formatarNumero(ampCombate)));
            }
            if (penDefesa > 0) {
                lore.add(Mensagem.formatar("&bPenetração de Defesa: &7" + CalcularStatus.formatarNumero(penDefesa)));
            }
            if (regenMana > 0) {
                lore.add(Mensagem.formatar("&bRegeneração de Mana: &7" + CalcularStatus.formatarNumero(regenMana)));
            }
            if (sorte > 0) {
                lore.add(Mensagem.formatar("&bSorte: &7" + CalcularStatus.formatarNumero(sorte)));
            }
            lore.add(" ");

        }

        ItemStack equipamento = new Item(material, 1, (short) 0)
                .setName(nomeComCores)
                .setLore(lore)
                .setUmbreakable(true)
                .setNBT("dano", danoNBT)
                .setNBT("defesa", defesaNBT)
                .setNBT("intel", intelNBT)
                .setNBT("ampCombate", ampCombateNBT)
                .setNBT("alcance", alcanceNBT)
                .setNBT("penDefesa", penDefesaNBT)
                .setNBT("bloqueio", bloqueioNBT)
                .setNBT("rouboVida", rouboVidaNBT)
                .setNBT("regenVida", regenVidaNBT)
                .setNBT("regenMana", regenManaNBT)
                .setNBT("sorte", sorteNBT)
                .setEnchant(Enchantment.DURABILITY, 1000, true)
                .setUmbreakable(true)
                .getIs();

        // Adiciona o equipamento ao inventário do jogador
        player.getInventory().addItem(equipamento);
    }

    @Override
    protected String getFailedValidationText(ConversationContext context, String invalidInput) {
        return Mensagem.formatar("&cResposta inválida. Por favor, digite 'sim' ou 'não'.");
    }
}