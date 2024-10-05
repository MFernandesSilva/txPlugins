package tx.mines;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import tx.api.DM;
import tx.mines.commands.MinaCommand;
import tx.mines.events.PlayerQuit;
import tx.mines.events.PlayerTeleport;

import java.util.HashMap;
import java.util.UUID;

public class txMines extends JavaPlugin {

    private txMines instance;
    private HashMap<UUID, Location> minasPrivadas = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        registerCommands();
        registerEvents();

        carregarMinas();

        DM.onEnable(this);
    }

    @Override
    public void onDisable() {
        salvarMinas();
        DM.onDisable(this);
    }

    private void registerCommands(){
        getCommand("mina").setExecutor(new MinaCommand(this, minasPrivadas));
    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new PlayerTeleport(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
    }

    public txMines getInstance() {
        return instance;
    }

    public void salvarMinas() {
        FileConfiguration config = getConfig();
        for (UUID uuid : minasPrivadas.keySet()) {
            Location loc = minasPrivadas.get(uuid);
            config.set("minas." + uuid + ".world", loc.getWorld().getName());
            config.set("minas." + uuid + ".x", loc.getX());
            config.set("minas." + uuid + ".y", loc.getY());
            config.set("minas." + uuid + ".z", loc.getZ());
        }
        saveConfig();
    }

    public void carregarMinas() {
        FileConfiguration config = getConfig();
        if (config.contains("minas")) {
            for (String key : config.getConfigurationSection("minas").getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                String worldName = config.getString("minas." + key + ".world");
                World world = Bukkit.getWorld(worldName);
                double x = config.getDouble("minas." + key + ".x");
                double y = config.getDouble("minas." + key + ".y");
                double z = config.getDouble("minas." + key + ".z");
                Location loc = new Location(world, x, y, z);
                minasPrivadas.put(uuid, loc);
            }
        }
    }
}
