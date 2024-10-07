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
        double aoe = (double) context.getSessionData("aoe");

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
                "&eAOE: &7" + aoe + "\n" +
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
        } else if (input.equalsIgnoreCase("não")) {
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
        double aoe = (double) context.getSessionData("aoe");

        Material material = determinarMaterialEquipamento(tipoEquipamento, player);
        if (material == null) return;

        List<String> lore = gerarLoreEquipamento(dano, defesa, intel, ampCombate, alcance, penDefesa, bloqueio, rouboVida, regenVida, regenMana, sorte, aoe);

        // Criação do equipamento com os atributos especificados
        ItemStack equipamento = new Item(material, 1, (short) 0)
                .setName(nomeComCores)
                .setLore(lore)
                .setUmbreakable(true)
                .setNBT("dano", dano)
                .setNBT("defesa", defesa)
                .setNBT("intel", intel)
                .setNBT("ampCombate", ampCombate)
                .setNBT("alcance", alcance)
                .setNBT("penDefesa", penDefesa)
                .setNBT("bloqueio", bloqueio)
                .setNBT("rouboVida", rouboVida)
                .setNBT("regenVida", regenVida)
                .setNBT("regenMana", regenMana)
                .setNBT("sorte", sorte)
                .setNBT("aoe", (int) aoe)
                .setEnchant(Enchantment.DURABILITY, 1000, true)
                .setUmbreakable(true)
                .getIs();

        // Adiciona o equipamento ao inventário do jogador
        player.getInventory().addItem(equipamento);
    }

    private Material determinarMaterialEquipamento(String tipoEquipamento, Player player) {
        Material material;
        switch (tipoEquipamento) {
            case "espada":
                return Material.DIAMOND_SWORD;
            case "capacete":
                return Material.DIAMOND_HELMET;
            case "peitoral":
                return Material.DIAMOND_CHESTPLATE;
            case "calça":
                return Material.DIAMOND_LEGGINGS;
            case "bota":
                return Material.DIAMOND_BOOTS;
            default:
                player.sendMessage(Mensagem.formatar("&cErro ao criar o equipamento: tipo inválido."));
                return null;
        }
    }

    private List<String> gerarLoreEquipamento(double dano, double defesa, double intel, double ampCombate,
                                              double alcance, double penDefesa, double bloqueio,
                                              double rouboVida, double regenVida, double regenMana,
                                              double sorte, double aoe) {
        List<String> lore = new ArrayList<>();

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
        if (sorte > 0) {
            lore.add(Mensagem.formatar("&bSorte: &7" + CalcularStatus.formatarNumero(sorte)));
        }
        if (regenMana > 0) {
            lore.add(Mensagem.formatar("&bRegeneração de Mana: &7" + CalcularStatus.formatarNumero(regenMana)));
        }
        if (aoe > 0) {
            lore.add(Mensagem.formatar("&eÁrea de Efeito: &7" + aoe));
        }
        lore.add(" ");
        return lore;
    }
}
