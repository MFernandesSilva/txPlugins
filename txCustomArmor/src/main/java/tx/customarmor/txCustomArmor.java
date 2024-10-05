package tx.customarmor;

import org.bukkit.plugin.java.JavaPlugin;
import tx.api.DM;
import tx.customarmor.commands.ArmaduraCommand;
import tx.customarmor.events.ArmorEquipEvent;
import tx.customarmor.events.ArmorEvent;

public class txCustomArmor extends JavaPlugin {

    private static txCustomArmor instance;

    @Override
    public void onEnable() {
        instance = this;

        registerCommands();
        registerEvents();

        DM.onEnable(this);
    }

    @Override
    public void onDisable() {
        DM.onDisable(this);
    }

    private void registerCommands(){
        getCommand("armadura").setExecutor(new ArmaduraCommand());
    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new ArmorEvent(), this);
        getServer().getPluginManager().registerEvents(new ArmorEquipEvent(), this);
    }

    public static txCustomArmor getInstance() {
        return instance;
    }
}
