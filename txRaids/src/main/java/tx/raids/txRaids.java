package tx.raids;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import tx.api.DM;
import tx.raids.commands.RaidCommand;

public class txRaids extends JavaPlugin {

    private static txRaids instance;
    private WorldEditPlugin we;
    private WorldGuardPlugin wg;
    private World raidWorld;

    @Override
    public void onEnable() {
        instance = this;

        loadConfiguration();
        registerCommands();
        registerEvents();

        Plugin worldEdit = getServer().getPluginManager().getPlugin("WorldEdit");
        if (worldEdit instanceof WorldEditPlugin){
            we = (WorldEditPlugin) worldEdit;
            getLogger().info("WorldEdit integrado com sucesso!");
        } else {
            getLogger().info("WorldEdit nao foi encontrado! O Plugin pode nao funcionar!");
        }

        if (Bukkit.getWorld("RAID") == null) {
            WorldCreator wc = new WorldCreator("RAID");
            wc.environment(World.Environment.NORMAL);
            wc.type(WorldType.FLAT); // Mundo superplano
            wc.generateStructures(false); // Sem estruturas
            raidWorld = wc.createWorld();
            getLogger().info("Mundo 'RAID' criado!");
        } else {
            raidWorld = Bukkit.getWorld("RAID");
            getLogger().info("Mundo 'RAID' carregado!");
        }

        DM.onEnable(this);
    }

    public World getRaidWorld() {
        return raidWorld;
    }

    @Override
    public void onDisable() {
        saveConfig();

        DM.onDisable(this);
    }

    private void registerCommands(){
        getCommand("raid").setExecutor(new RaidCommand(this));
    }

    private void registerEvents(){

    }

    private void loadConfiguration(){
        saveDefaultConfig();
        getConfig().options().copyDefaults(false);
    }

    public static txRaids getInstance() {
        return instance;
    }

    public WorldEditPlugin getWe() {
        return we;
    }

    public WorldGuardPlugin getWg() {
        return wg;
    }
}
