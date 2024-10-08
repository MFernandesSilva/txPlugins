package tx.rpg.itens;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tx.api.Item;
import tx.api.Mensagem;
import tx.rpg.runas.TipoRuna;
import tx.rpg.txRPG;

import java.util.Arrays;

public class Runas {

    // Tipos de itens de runa
    private enum TipoItemRuna {
        RUNA,
        ROMPIMENTO
    }

    private static final String CONFIG_RUNAS_ITENS = "runas.itens.";
    private static final String LVL = "Lvl";

    // Nomes das runas e rompimentos
    private static final String[] NOMES_RUNAS = {
            "&cRuna de Dano Nível %d",
            "&aRuna de Defesa Nível %d",
            "&bRuna de Amplificação Nível %d"
    };

    private static final String[] NOMES_ROMPIMENTOS = {
            "&cRompimento de Dano Nível %d",
            "&cRompimento de Defesa Nível %d",
            "&cRompimento de Amplificação Nível %d"
    };

    private static final String LORE_RUNA = "&7Para usar este item, primeiro você tem que romper.";
    private static final String LORE_ROMPIMENTO = "&7Usado para romper nível de &cRUNA&7.";

    // Método para criar runas e rompimentos
    private static ItemStack criarRuna(TipoItemRuna tipoItem, TipoRuna tipoRuna, int nivel) {
        String configPath = CONFIG_RUNAS_ITENS + tipoItem.toString().toLowerCase() + "." + tipoRuna.toString().toLowerCase() + LVL + nivel;
        int itemId = txRPG.getInstance().getConfig().getInt(configPath);

        Material material = Material.getMaterial(itemId);

        if (material == null || material == Material.AIR) {
            Bukkit.getLogger().severe("Material inválido para runa em '" + configPath + "': " + itemId);
            return null;
        }

        ItemStack itemStack = new ItemStack(material, 1, (short) 0);
        ItemMeta meta = itemStack.getItemMeta();

        if (meta == null) {
            meta = Bukkit.getServer().getItemFactory().getItemMeta(material);
        }

        itemStack.setItemMeta(meta);

        String nome = String.format(tipoItem == TipoItemRuna.RUNA ? NOMES_RUNAS[tipoRuna.ordinal()] : NOMES_ROMPIMENTOS[tipoRuna.ordinal()], nivel);
        String lore = tipoItem == TipoItemRuna.RUNA ? LORE_RUNA : LORE_ROMPIMENTO;
        String tagNBT = tipoItem.toString().toLowerCase() + tipoRuna.toString() + "Lvl" + nivel;

        return new Item(itemStack)
                .setName(Mensagem.formatar(nome))
                .setLore(Arrays.asList(Mensagem.formatar(lore), Mensagem.formatar("&cCertifique-se de usar o nível certo!")))
                .setNBT("tipo", tagNBT)
                .setUmbreakable(true)
                .getIs();
    }

    // Arrays para armazenar runas e rompimentos
    public ItemStack[] runasDano = new ItemStack[5];
    public ItemStack[] runasDefesa = new ItemStack[5];
    public ItemStack[] runasAmplificacao = new ItemStack[5];

    public ItemStack[] rompimentoDano = new ItemStack[5];
    public ItemStack[] rompimentoDefesa = new ItemStack[5];
    public ItemStack[] rompimentoAmplificacao = new ItemStack[5];

    // Construtor da classe Runas
    public Runas() {
        for (int i = 0; i < 5; i++) {
            runasDano[i] = criarRuna(TipoItemRuna.RUNA, TipoRuna.DANO, i + 1);
            runasDefesa[i] = criarRuna(TipoItemRuna.RUNA, TipoRuna.DEFESA, i + 1);
            runasAmplificacao[i] = criarRuna(TipoItemRuna.RUNA, TipoRuna.AMPLIFICACAO, i + 1);

            rompimentoDano[i] = criarRuna(TipoItemRuna.ROMPIMENTO, TipoRuna.DANO, i + 1);
            rompimentoDefesa[i] = criarRuna(TipoItemRuna.ROMPIMENTO, TipoRuna.DEFESA, i + 1);
            rompimentoAmplificacao[i] = criarRuna(TipoItemRuna.ROMPIMENTO, TipoRuna.AMPLIFICACAO, i + 1);
        }
    }
}
