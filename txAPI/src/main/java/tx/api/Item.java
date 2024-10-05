package tx.api;

import de.tr7zw.nbtapi.NBTItem;
import java.util.List;

import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.NBTTagList;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Item {
    private ItemStack is;

    private ItemMeta im;

    public Item(Material material, int amount, short data) {
        this.is = new ItemStack(material, amount, data);
        this.im = this.is.getItemMeta();
    }

    public Item(ItemStack itemStack) {
        this.is = itemStack.clone();
        this.im = this.is.getItemMeta();
    }

    public Item setName(String name) {
        this.im.setDisplayName(Mensagem.formatar(name));
        this.is.setItemMeta(this.im);
        return this;
    }

    public Item setLore(List<String> lore) {
        this.im.setLore(lore);
        this.is.setItemMeta(this.im);
        return this;
    }

    public Item setEnchant(Enchantment enchant, int value, boolean bl) {
        this.im.addEnchant(enchant, value, bl);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemStack getIs() {
        return this.is;
    }

    public Item setUmbreakable(boolean unbreakable) {
        NBTItem nbtItem = new NBTItem(this.is);
        nbtItem.setBoolean("Unbreakble", Boolean.valueOf(unbreakable));
        this.is = nbtItem.getItem();
        return this;
    }

    public Item setDurability(short durability) {
        this.is.setDurability(durability);
        return this;
    }

    public Item setLeatherArmorColor(Color color) {
        if (this.is.getType() == Material.LEATHER_HELMET || this.is.getType() == Material.LEATHER_CHESTPLATE || this.is.getType() == Material.LEATHER_LEGGINGS || this.is
                .getType() == Material.LEATHER_BOOTS) {
            LeatherArmorMeta meta = (LeatherArmorMeta)this.is.getItemMeta();
            meta.setColor(color);
            this.is.setItemMeta((ItemMeta)meta);
        }
        return this;
    }

    public CraftItemStack setGlow(ItemStack item){
        net.minecraft.server.v1_7_R4.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()){
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }

        if (tag == null) tag = nmsStack.getTag();
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    public <T> Item setNBT(String key, T value) {
        NBTItem nbtItem = new NBTItem(this.is);
        if (value instanceof Boolean) {
            nbtItem.setBoolean(key, (Boolean)value);
        } else if (value instanceof Integer) {
            nbtItem.setInteger(key, (Integer)value);
        } else if (value instanceof Double) {
            nbtItem.setDouble(key, (Double)value);
        } else if (value instanceof String) {
            nbtItem.setString(key, (String)value);
        }
        this.is = nbtItem.getItem();
        return this;
    }

    public <T> T getNBT(String key, Class<T> type) {
        NBTItem nbtItem = new NBTItem(this.is);
        if (type == Boolean.class)
            return type.cast(nbtItem.getBoolean(key));
        if (type == Integer.class)
            return type.cast(nbtItem.getInteger(key));
        if (type == Double.class)
            return type.cast(nbtItem.getDouble(key));
        if (type == String.class)
            return type.cast(nbtItem.getString(key));
        return null;
    }

    public boolean hasNBTKey(String key) {
        NBTItem nbtItem = new NBTItem(this.is);
        return nbtItem.hasKey(key).booleanValue();
    }

    public Item removeNBTKey(String key) {
        NBTItem nbtItem = new NBTItem(this.is);
        nbtItem.removeKey(key);
        this.is = nbtItem.getItem();
        return this;
    }
}
