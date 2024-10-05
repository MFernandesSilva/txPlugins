package tx.api;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class NBT {
    public static void adicionarAListaNBT(ItemStack item, String chaveLista, Object valor) {
        NBTItem nbtItem = new NBTItem(item);
        List<Object> lista = (List<Object>)nbtItem.getObject(chaveLista, List.class);
        if (lista == null) {
            lista = new ArrayList();
            nbtItem.setObject(chaveLista, lista);
        }
        lista.add(valor);
        nbtItem.applyNBT(item);
    }

    public static <T> List<T> getListaNBT(ItemStack item, String chaveLista) {
        NBTItem nbtItem = new NBTItem(item);
        return (List<T>)nbtItem.getObject(chaveLista, List.class);
    }

    public static void setarCompostoNBT(ItemStack item, String chaveComposto, NBTCompound composto) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setObject(chaveComposto, composto);
        nbtItem.applyNBT(item);
    }

    public static NBTCompound getCompostoNBT(ItemStack item, String chaveComposto) {
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getCompound(chaveComposto);
    }

    public static boolean hasNBTKey(ItemStack item, String key) {
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasKey(key).booleanValue();
    }

    public static <T> T getNBT(ItemStack item, String key, Class<T> type) {
        NBTItem nbtItem = new NBTItem(item);
        if (type == Boolean.class)
            return type.cast(nbtItem.getBoolean(key));
        if (type == Integer.class)
            return type.cast(nbtItem.getInteger(key));
        if (type == Double.class)
            return type.cast(nbtItem.getDouble(key));
        if (type == String.class)
            return type.cast(nbtItem.getString(key));
        if (type == NBTCompound.class)
            return type.cast(nbtItem.getCompound(key));
        return null;
    }

    public static String serializarItem(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.toString();
    }

    public static ItemStack desserializarItem(ItemStack itemBase, String nbtString) {
        NBTItem nbtItemBase = new NBTItem(itemBase);
        try {
            String[] tags = nbtString.substring(1, nbtString.length() - 1).split(",");
            for (String tag : tags) {
                String[] partes = tag.split(":");
                String chave = partes[0].trim();
                String valor = partes[1].trim();
                if (valor.equals("true") || valor.equals("false")) {
                    nbtItemBase.setBoolean(chave, Boolean.valueOf(Boolean.parseBoolean(valor)));
                } else {
                    try {
                        int valorInt = Integer.parseInt(valor);
                        nbtItemBase.setInteger(chave, Integer.valueOf(valorInt));
                    } catch (NumberFormatException e) {
                        nbtItemBase.setString(chave, valor);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nbtItemBase.getItem();
    }
}
