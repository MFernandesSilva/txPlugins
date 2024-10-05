package tx.api;

import me.dpohvar.powernbt.api.NBTCompound;
import me.dpohvar.powernbt.api.NBTManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class DBC {

    // Forma
    public static Integer getPlayerDBCForm(Player player) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            int form = PlayerPersisted.getInt("jrmcState");
            return Integer.valueOf(form);
        } catch (ClassNotFoundException var4) {
            return null;
        }
    }

    // Raça
    public static Integer getPlayerRace(Player player) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            int race = PlayerPersisted.getInt("jrmcRace");
            return Integer.valueOf(race);
        } catch (ClassNotFoundException var4) {
            return null;
        }
    }

    // Vida
    public static Integer getHealth(Player player) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            int health = PlayerPersisted.getInt("jrmcBdy");
            return Integer.valueOf(health);
        } catch (ClassNotFoundException var4) {
            return null;
        }
    }

    public static Double getMaxHealth(Player player) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            double maxHealth = (PlayerPersisted.getInt("jrmcCnsI") * 22);
            return Double.valueOf(maxHealth);
        } catch (ClassNotFoundException var5) {
            return null;
        }
    }

    public static void setHealth(Player player, int health) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            PlayerPersisted.put("jrmcBdy", Integer.valueOf(health));
            Forgadata.put("PlayerPersisted", PlayerPersisted);
            NBTManager.getInstance().writeForgeData((Entity)player, Forgadata);
        } catch (ClassNotFoundException classNotFoundException) {}
    }

    public static void setHealthCapped(Player player, int health) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            if (health > getMaxHealth(player).doubleValue()) {
                PlayerPersisted.put("jrmcBdy", getMaxHealth(player));
            } else {
                PlayerPersisted.put("jrmcBdy", Integer.valueOf(health));
            }
            Forgadata.put("PlayerPersisted", PlayerPersisted);
            NBTManager.getInstance().writeForgeData((Entity)player, Forgadata);
        } catch (ClassNotFoundException classNotFoundException) {}
    }


    // Atributos
    public static int getSTR(Player player) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            int x = PlayerPersisted.getInt("jrmcStrI");
            return x;
        } catch (ClassNotFoundException var4) {
            return -1;
        }
    }

    public static int getDEX(Player player) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            int x = PlayerPersisted.getInt("jrmcDexI");
            return x;
        } catch (ClassNotFoundException var4) {
            return -1;
        }
    }

    public static int getCON(Player player) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            int x = PlayerPersisted.getInt("jrmcCnsI");
            return x;
        } catch (ClassNotFoundException var4) {
            return -1;
        }
    }

    public static int getWIL(Player player) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            int x = PlayerPersisted.getInt("jrmcWilI");
            return x;
        } catch (ClassNotFoundException var4) {
            return -1;
        }
    }

    public static int getMND(Player player) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            int x = PlayerPersisted.getInt("jrmcIntI");
            return x;
        } catch (ClassNotFoundException var4) {
            return -1;
        }
    }

    public static int getSPI(Player player) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            int x = PlayerPersisted.getInt("jrmcCncI");
            return x;
        } catch (ClassNotFoundException var4) {
            return -1;
        }
    }

    public static void setSTR(Player player, int amount) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            if (amount > 1000000) {
                PlayerPersisted.put("jrmcStrI", Integer.valueOf(1000000));
            } else {
                PlayerPersisted.put("jrmcStrI", Integer.valueOf(amount));
            }
            Forgadata.put("PlayerPersisted", PlayerPersisted);
            NBTManager.getInstance().writeForgeData((Entity)player, Forgadata);
        } catch (ClassNotFoundException classNotFoundException) {}
    }

    public static void setDEX(Player player, int amount) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            if (amount > 1000000) {
                PlayerPersisted.put("jrmcDexI", Integer.valueOf(1000000));
            } else {
                PlayerPersisted.put("jrmcDexI", Integer.valueOf(amount));
            }
            Forgadata.put("PlayerPersisted", PlayerPersisted);
            NBTManager.getInstance().writeForgeData((Entity)player, Forgadata);
        } catch (ClassNotFoundException classNotFoundException) {}
    }

    public static void setCON(Player player, int amount) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            if (amount > 1000000) {
                PlayerPersisted.put("jrmcCnsI", Integer.valueOf(1000000));
            } else {
                PlayerPersisted.put("jrmcCnsI", Integer.valueOf(amount));
            }
            Forgadata.put("PlayerPersisted", PlayerPersisted);
            NBTManager.getInstance().writeForgeData((Entity)player, Forgadata);
        } catch (ClassNotFoundException classNotFoundException) {}
    }

    public static void setWIL(Player player, int amount) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            if (amount > 1000000) {
                PlayerPersisted.put("jrmcWilI", Integer.valueOf(1000000));
            } else {
                PlayerPersisted.put("jrmcWilI", Integer.valueOf(amount));
            }
            Forgadata.put("PlayerPersisted", PlayerPersisted);
            NBTManager.getInstance().writeForgeData((Entity)player, Forgadata);
        } catch (ClassNotFoundException classNotFoundException) {}
    }

    public static void setMND(Player player, int amount) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            if (amount > 1000000) {
                PlayerPersisted.put("jrmcIntI", Integer.valueOf(1000000));
            } else {
                PlayerPersisted.put("jrmcIntI", Integer.valueOf(amount));
            }
            Forgadata.put("PlayerPersisted", PlayerPersisted);
            NBTManager.getInstance().writeForgeData((Entity)player, Forgadata);
        } catch (ClassNotFoundException classNotFoundException) {}
    }

    public static void setSPI(Player player, int amount) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            if (amount > 1000000) {
                PlayerPersisted.put("jrmcCncI", Integer.valueOf(1000000));
            } else {
                PlayerPersisted.put("jrmcCncI", Integer.valueOf(amount));
            }
            Forgadata.put("PlayerPersisted", PlayerPersisted);
            NBTManager.getInstance().writeForgeData((Entity)player, Forgadata);
        } catch (ClassNotFoundException classNotFoundException) {}
    }

    // porcentagem de força
    public static Integer getRelease(Player player) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            int release = PlayerPersisted.getInt("jrmcRelease");
            return Integer.valueOf(release);
        } catch (ClassNotFoundException var4) {
            return null;
        }
    }

    // Stamina
    public static Integer getStamina(Player player) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            int stamina = PlayerPersisted.getInt("jrmcStamina");
            return Integer.valueOf(stamina);
        } catch (ClassNotFoundException var4) {
            return null;
        }
    }

    public static void setStamina(Player player, int amount) {
        try {
            Class.forName("me.dpohvar.powernbt.api.NBTManager");
            NBTCompound Forgadata = NBTManager.getInstance().readForgeData((Entity)player);
            NBTCompound PlayerPersisted = (NBTCompound)Forgadata.get("PlayerPersisted");
            if (amount > 800000) {
                PlayerPersisted.put("jrmcStamina", Integer.valueOf(800000));
            } else {
                PlayerPersisted.put("jrmcStamina", Integer.valueOf(amount));
            }
            Forgadata.put("PlayerPersisted", PlayerPersisted);
            NBTManager.getInstance().writeForgeData((Entity)player, Forgadata);
        } catch (ClassNotFoundException classNotFoundException) {}
    }

    public static void savePlayerPersistedData(Player player, File outputFile) throws IOException, ClassNotFoundException {
        Class.forName("me.dpohvar.powernbt.api.NBTManager");
        NBTCompound forgadata = NBTManager.getInstance().readForgeData((Entity)player);
        NBTCompound playerPersisted = (NBTCompound)forgadata.get("PlayerPersisted");
        if (playerPersisted != null) {
            FileWriter writer = new FileWriter(outputFile);
            Set<String> keys = playerPersisted.keySet();
            for (String key : keys) {
                Object value = playerPersisted.get(key);
                writer.write(key + ": " + value + "\n");
            }
            writer.close();
        }
    }
}
