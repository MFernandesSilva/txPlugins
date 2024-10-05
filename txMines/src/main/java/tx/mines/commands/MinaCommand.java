package tx.mines.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import tx.api.DM;
import tx.mines.txMines;

import java.util.HashMap;
import java.util.UUID;

public class MinaCommand implements CommandExecutor {

    private txMines plugin;
    private HashMap<UUID, Location> minasPrivadas;
    DM dm = new DM();

    public MinaCommand(txMines plugin, HashMap<UUID, Location> minasPrivadas){
        this.plugin = plugin;
        this.minasPrivadas = minasPrivadas;

    }

    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        if (!(s instanceof Player)){
            s.sendMessage(dm.cc());
            return true;
        }

        Player player = (Player) s;
        UUID playerId = player.getUniqueId();

        World mundoMina = Bukkit.getWorld("mina");
        if (mundoMina == null) {
            WorldCreator wc = new WorldCreator("mina");
            wc.environment(World.Environment.NORMAL);
            wc.generateStructures(false);
            mundoMina = wc.createWorld();
        }

        if (!minasPrivadas.containsKey(playerId)) {
            int minasCriadas = minasPrivadas.size();
            int x = minasCriadas * 200;
            int z = 0;

            Location novaMina = new Location(mundoMina, x, 64, z);
            minasPrivadas.put(playerId, novaMina);
            player.sendMessage("Sua mina privada foi criada em um mundo separado!");
        }

        player.teleport(minasPrivadas.get(playerId));

        ItemStack picareta = new ItemStack(Material.DIAMOND_PICKAXE);
        player.getInventory().addItem(picareta);

        player.sendMessage("Você foi teleportado para sua mina privada!");
        return true;
    }
}
