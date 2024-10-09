package tx.rpg.itens;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tx.api.Item;
import tx.api.Mensagem;
import tx.rpg.reinos.TipoReino;
import tx.rpg.runas.TipoRuna;
import tx.rpg.txRPG;

import java.util.Arrays;

public class Reinos {

    // Tipos de itens de runa
    private enum TipoItemReino {
        REINO,
        ROMPIMENTO
    }

    private static final String CONFIG_REINOS_ITENS = "reinos.itens.";
    private static final String LVL = "lvl";

    // Nomes das runas e rompimentos
    private static final String[] NOMES_REINOS = {
            "&cReino Mortal",
            "&cReino de Combate",
            "&cReino Celestial",
            "&cReino Imortal",
            "&cReino Deus"
    };

    private static final String[] NOMES_ROMPIMENTOS = {
            "&cRompimento Mortal",
            "&cRompimento de Combate",
            "&cRompimento Celestial",
            "&cRompimento Imortal",
            "&cRompimento Deus"
    };

    private static final String LORE_RUNA = "&7Para usar este item, primeiro você tem que romper.";
    private static final String LORE_ROMPIMENTO = "&7Usado para romper nível de &cREINO&7.";

    // Método para criar runas e rompimentos
    private static ItemStack criarReino(TipoItemReino tipoItem, TipoReino tipoReino, int nivel) {
        String configPath;
        String nivelNome;
        if (nivel >= 1 && nivel <= 20){
            configPath = CONFIG_REINOS_ITENS + "reino_mortal";
            nivelNome = "MORTAL";
        } else if (nivel >= 21 && nivel <= 35) {
            configPath = CONFIG_REINOS_ITENS + "reino_deCombate";
            nivelNome = "DE COMBATE";
        } else if (nivel >= 36 && nivel <= 45)  {
            configPath = CONFIG_REINOS_ITENS + "reino_celestial";
            nivelNome = "CELESTIAL";
        } else if (nivel >= 46 && nivel <= 50)  {
            configPath = CONFIG_REINOS_ITENS + "reino_imortal";
            nivelNome = "IMORTAL";
        } else if (nivel >= 51 && nivel <= 53) {
            configPath = CONFIG_REINOS_ITENS + "reino_deus";
            nivelNome = "DEUS";
        } else {
            configPath = "erro";
            nivelNome = "erro";
        }

        int itemId = txRPG.getInstance().getConfig().getInt(configPath);

        Material material = Material.getMaterial(itemId);

        if (material == null || material == Material.AIR) {
            Bukkit.getLogger().severe("Material inválido para reino em '" + configPath + "': " + itemId);
            return null;
        }

        ItemStack itemStack = new ItemStack(material, 1, (short) 0);
        ItemMeta meta = itemStack.getItemMeta();

        if (meta == null) {
            meta = Bukkit.getServer().getItemFactory().getItemMeta(material);
        }

        itemStack.setItemMeta(meta);

        String nome = String.format(tipoItem == TipoItemReino.REINO ? NOMES_REINOS[tipoReino.ordinal()] : NOMES_ROMPIMENTOS[tipoReino.ordinal()], nivelNome);
        String lore = tipoItem == TipoItemReino.REINO ? LORE_RUNA : LORE_ROMPIMENTO;
        String tagNBT = tipoItem.toString().toLowerCase() + tipoReino;

        return new Item(itemStack)
                .setName(Mensagem.formatar(nome))
                .setLore(Arrays.asList(Mensagem.formatar(lore), Mensagem.formatar("&cCertifique-se de usar o nível certo!")))
                .setNBT("reino", tagNBT)
                .setUmbreakable(true)
                .getIs();
    }

    // Arrays para armazenar runas e rompimentos
    public ItemStack[] reinoMortal = new ItemStack[5];
    public ItemStack[] reinoDeCombate = new ItemStack[5];
    public ItemStack[] reinoCelestial = new ItemStack[5];
    public ItemStack[] reinoImortal = new ItemStack[5];
    public ItemStack[] reinoDeus = new ItemStack[5];

    public ItemStack[] rompimentoMortal = new ItemStack[5];
    public ItemStack[] rompimentoDeCombate = new ItemStack[5];
    public ItemStack[] rompimentoCelestial = new ItemStack[5];
    public ItemStack[] rompimentoImortal = new ItemStack[5];
    public ItemStack[] rrompimentoDeus = new ItemStack[5];


    // Construtor da classe Runas
    public Reinos() {
        for (int i = 0; i < 5; i++) {
            reinoMortal[i] = criarReino(TipoItemReino.REINO, TipoReino.REINO, i + 1);
            reinoDeCombate[i] = criarReino(TipoItemReino.REINO, TipoReino.REINO, i + 1);
            reinoCelestial[i] = criarReino(TipoItemReino.REINO, TipoReino.REINO, i + 1);
            reinoImortal[i] = criarReino(TipoItemReino.REINO, TipoReino.REINO, i + 1);
            reinoDeus[i] = criarReino(TipoItemReino.REINO, TipoReino.REINO, i + 1);

            rompimentoMortal[i] = criarReino(TipoItemReino.REINO, TipoReino.REINO, i + 1);
            rompimentoDeCombate[i] = criarReino(TipoItemReino.REINO, TipoReino.REINO, i + 1);
            rompimentoCelestial[i] = criarReino(TipoItemReino.REINO, TipoReino.REINO, i + 1);
            rompimentoImortal[i] = criarReino(TipoItemReino.REINO, TipoReino.REINO, i + 1);
            rrompimentoDeus[i] = criarReino(TipoItemReino.REINO, TipoReino.REINO, i + 1);
        }
    }
}
