package tx.rpg.itens;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tx.api.Item;
import tx.api.Mensagem;
import tx.rpg.txRPG;

import java.util.Arrays;

public class Reinos {

    // Tipos de itens de runa
    private enum TipoItemReino {
        REINO,
        ROMPIMENTOREINO
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
    private static ItemStack criarReino(TipoItemReino tipoItem, int nivel) {
        String configPath = obterCaminhoConfig(nivel);
        String nivelNome = obterNomeNivel(nivel);

        if (configPath.equals("erro") || nivelNome.equals("erro")) {
            return null; // Retorna null se o nível estiver fora do intervalo
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

        String nome = tipoItem == TipoItemReino.REINO ? NOMES_REINOS[nivelNomeToIndex(nivelNome)] : NOMES_ROMPIMENTOS[nivelNomeToIndex(nivelNome)];
        String lore = tipoItem == TipoItemReino.REINO ? LORE_RUNA : LORE_ROMPIMENTO;
        String tagNBT = tipoItem.toString().toLowerCase() + "Lvl" + nivel;

        return new Item(itemStack)
                .setName(Mensagem.formatar(nome))
                .setLore(Arrays.asList(Mensagem.formatar(lore), Mensagem.formatar("&cCertifique-se de usar o nível certo!")))
                .setNBT("reino", tagNBT)
                .setUmbreakable(true)
                .getIs();
    }

    // Método auxiliar para obter o caminho da configuração baseado no nível
    private static String obterCaminhoConfig(int nivel) {
        if (nivel >= 1 && nivel <= 20) {
            return CONFIG_REINOS_ITENS + "reino_mortal";
        } else if (nivel >= 21 && nivel <= 35) {
            return CONFIG_REINOS_ITENS + "reino_deCombate";
        } else if (nivel >= 36 && nivel <= 45) {
            return CONFIG_REINOS_ITENS + "reino_celestial";
        } else if (nivel >= 46 && nivel <= 50) {
            return CONFIG_REINOS_ITENS + "reino_imortal";
        } else if (nivel >= 51 && nivel <= 53) {
            return CONFIG_REINOS_ITENS + "reino_deus";
        } else {
            return "erro";
        }
    }

    // Método auxiliar para obter o nome do nível
    private static String obterNomeNivel(int nivel) {
        if (nivel >= 1 && nivel <= 20) {
            return "MORTAL";
        } else if (nivel >= 21 && nivel <= 35) {
            return "DE COMBATE";
        } else if (nivel >= 36 && nivel <= 45) {
            return "CELESTIAL";
        } else if (nivel >= 46 && nivel <= 50) {
            return "IMORTAL";
        } else if (nivel >= 51 && nivel <= 53) {
            return "DEUS";
        } else {
            return "erro";
        }
    }

    // Método auxiliar para converter nome do nível para índice
    private static int nivelNomeToIndex(String nivelNome) {
        switch (nivelNome) {
            case "MORTAL":
                return 0;
            case "DE COMBATE":
                return 1;
            case "CELESTIAL":
                return 2;
            case "IMORTAL":
                return 3;
            case "DEUS":
                return 4;
            default:
                return -1;
        }
    }

    // Arrays para armazenar runas e rompimentos
    public ItemStack[] reinoMortal = new ItemStack[53];
    public ItemStack[] reinoDeCombate = new ItemStack[53];
    public ItemStack[] reinoCelestial = new ItemStack[53];
    public ItemStack[] reinoImortal = new ItemStack[53];
    public ItemStack[] reinoDeus = new ItemStack[53];

    public ItemStack[] rompimentoMortal = new ItemStack[53];
    public ItemStack[] rompimentoDeCombate = new ItemStack[53];
    public ItemStack[] rompimentoCelestial = new ItemStack[53];
    public ItemStack[] rompimentoImortal = new ItemStack[53];
    public ItemStack[] rompimentoDeus = new ItemStack[53];

    // Construtor da classe Reinos
    public Reinos() {
        for (int i = 0; i < 53; i++) {
            int nivel = i + 1;
            reinoMortal[i] = criarReino(TipoItemReino.REINO, nivel);
            reinoDeCombate[i] = criarReino(TipoItemReino.REINO, nivel);
            reinoCelestial[i] = criarReino(TipoItemReino.REINO, nivel);
            reinoImortal[i] = criarReino(TipoItemReino.REINO, nivel);
            reinoDeus[i] = criarReino(TipoItemReino.REINO, nivel);

            rompimentoMortal[i] = criarReino(TipoItemReino.ROMPIMENTOREINO, nivel);
            rompimentoDeCombate[i] = criarReino(TipoItemReino.ROMPIMENTOREINO, nivel);
            rompimentoCelestial[i] = criarReino(TipoItemReino.ROMPIMENTOREINO, nivel);
            rompimentoImortal[i] = criarReino(TipoItemReino.ROMPIMENTOREINO, nivel);
            rompimentoDeus[i] = criarReino(TipoItemReino.ROMPIMENTOREINO, nivel);
        }
    }
}
