package tx.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Inventario {
    public static Inventory criarInventario(int tamanho, String titulo) {
        if (tamanho % 9 != 0 || tamanho < 9 || tamanho > 54)
            throw new IllegalArgumentException("Tamanho do inventário inválido. Deve ser múltiplo de 9 e entre 9 e 54.");
        return Bukkit.createInventory(null, tamanho, Mensagem.formatar(titulo));
    }

    public static void adicionarItem(Inventory inventario, ItemStack item, int slot) {
        if (slot < 0 || slot >= inventario.getSize())
            throw new IllegalArgumentException("Slot inválido.");
        inventario.setItem(slot, item);
    }

    public static void removerItem(Inventory inventario, int slot) {
        adicionarItem(inventario, null, slot);
    }

    public static void substituirItem(Inventory inventario, ItemStack novoItem, int slot) {
        adicionarItem(inventario, novoItem, slot);
    }

    public static void preencherInventario(Inventory inventario, ItemStack item) {
        for (int i = 0; i < inventario.getSize(); i++) {
            if (inventario.getItem(i) == null)
                inventario.setItem(i, item);
        }
    }

    public static ItemStack getItem(Inventory inventario, int slot) {
        if (slot < 0 || slot >= inventario.getSize())
            throw new IllegalArgumentException("Slot inválido.");
        return inventario.getItem(slot);
    }

    public static boolean temEspaco(Inventory inventario) {
        for (ItemStack item : inventario.getContents()) {
            if (item == null)
                return true;
        }
        return false;
    }

    public static int getPrimeiroSlotVazio(Inventory inventario) {
        for (int i = 0; i < inventario.getSize(); i++) {
            if (inventario.getItem(i) == null)
                return i;
        }
        return -1;
    }

    public static ItemStack gerarItemAleatorio() {
        Random random = new Random();
        List<Material> materiaisValidos = new ArrayList<>();
        for (Material material : Material.values()) {
            if (!material.isBlock() && material != Material.AIR)
                materiaisValidos.add(material);
        }
        Material materialAleatorio = materiaisValidos.get(random.nextInt(materiaisValidos.size()));
        ItemStack item = new ItemStack(materialAleatorio);
        if (random.nextDouble() < 0.3D) {
            Enchantment encantamento = Enchantment.values()[random.nextInt((Enchantment.values()).length)];
            int nivel = random.nextInt(encantamento.getMaxLevel()) + 1;
            item.addEnchantment(encantamento, nivel);
        }
        return item;
    }
}