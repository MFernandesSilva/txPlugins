package tx.chest;

import org.bukkit.plugin.java.JavaPlugin;
import tx.api.DM;

public class txChest extends JavaPlugin {

    private txChest instance;

    @Override
    public void onEnable() {
        instance = this;

        loadConfiguration();
        registerCommands();
        registerEvents();

        DM.onEnable(this);
    }

    @Override
    public void onDisable() {
        saveConfig();

        DM.onDisable(this);
    }

    private void registerCommands(){

    }

    private void registerEvents(){

    }

    private void loadConfiguration(){
        saveDefaultConfig();
        getConfig().options().copyDefaults(false);
    }

    public txChest getInstance() {
        return instance;
    }
}
