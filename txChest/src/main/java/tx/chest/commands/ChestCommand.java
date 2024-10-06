package tx.chest.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import tx.api.DM;
import tx.api.Inventario;
import tx.chest.txChest;

public class ChestCommand implements CommandExecutor {

    private txChest plugin;
    DM dm = new DM();

    public ChestCommand(txChest plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        if (!(s instanceof Player)){
            s.sendMessage(dm.cc());
            return true;
        }

        Player player = (Player) s;

        if (!player.hasPermission("txchest.openchest")){
            player.sendMessage(dm.np());
            return true;
        }

        Inventory chest = Inventario.criarInventario(54, "&8&lARMAZEM");


        return false;
    }
}
