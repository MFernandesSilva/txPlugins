package tx.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import tx.api.utils.ReflectionUtils;

import java.io.*;
import java.lang.reflect.Constructor;
import java.math.BigInteger;

public class Serializer {

    public static ItemStack deserializeItemStack(String data) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(data, 32).toByteArray());
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        ItemStack itemStack = null;

        try {
            Class<?> nbtTagCompoundClass = ReflectionUtils.getNMSClass("NBTTagCompound");
            Class<?> nmsItemStackClass = ReflectionUtils.getNMSClass("ItemStack");
            Object nbtTagCompound = ReflectionUtils.getNMSClass("NBTCompressedStreamTools").getMethod("a", DataInput.class).invoke(null, dataInputStream);
            Constructor<?> craftItemStackConstructor = nmsItemStackClass.getDeclaredConstructor(nbtTagCompoundClass);
            craftItemStackConstructor.setAccessible(true);
            Object craftItemStack = craftItemStackConstructor.newInstance(nbtTagCompound);
            itemStack = (ItemStack) ReflectionUtils.getOBClass("inventory.CraftItemStack").getMethod("asBukkitCopy", nmsItemStackClass).invoke(null, craftItemStack);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return itemStack;
    }

    public static ItemStack[] deserializeListItemStack(String itens) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(itens));
        BukkitObjectInputStream dataInput;

        try {
            dataInput = new BukkitObjectInputStream(inputStream);
            int size = dataInput.readInt();
            ItemStack[] list = new ItemStack[size];

            for (int i = 0; i < size; i++) {
                Object utf = dataInput.readObject();
                int slot = dataInput.readInt();
                if (utf != null) list[slot] = deserializeItemStack((String) utf);
            }

            dataInput.close();
            return list;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String serializeItemStack(ItemStack item) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(outputStream);

        try {
            Class<?> nbtTagCompoundClass = ReflectionUtils.getNMSClass("NBTTagCompound");
            Constructor<?> nbtTagCompoundConstructor = nbtTagCompoundClass.getConstructor();
            Object nbtTagCompound = nbtTagCompoundConstructor.newInstance();
            Object nmsItemStack = ReflectionUtils.getOBClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
            ReflectionUtils.getNMSClass("ItemStack").getMethod("save", nbtTagCompoundClass).invoke(nmsItemStack, nbtTagCompound);
            ReflectionUtils.getNMSClass("NBTCompressedStreamTools").getMethod("a", nbtTagCompoundClass, DataOutput.class).invoke(null, nbtTagCompound, dataOutput);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return new BigInteger(1, outputStream.toByteArray()).toString(32);
    }

    public static String serializeListItemStack(ItemStack[] itens) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput;

        try {
            dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(itens.length);

            for (int index = 0; index < itens.length; index++) {
                ItemStack is = itens[index];
                if (is != null && is.getType() != Material.AIR) {
                    dataOutput.writeObject(serializeItemStack(is));
                } else {
                    dataOutput.writeObject(null);
                }
                dataOutput.writeInt(index);
            }

            dataOutput.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return Base64Coder.encodeLines(outputStream.toByteArray());
    }
}