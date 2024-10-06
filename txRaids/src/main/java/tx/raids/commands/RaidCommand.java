package tx.raids.commands;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import tx.api.DM;
import tx.api.Mensagem;
import tx.raids.txRaids;

public class RaidCommand implements CommandExecutor {

    private txRaids plugin;
    DM dm = new DM();
    private int raidCounter = 0;
    public RaidCommand(txRaids plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        if (args.length == 3){
            if (args[0].equalsIgnoreCase("iniciar")){
                if (!s.hasPermission("txraids.iniciar")){
                    s.sendMessage(dm.np());
                    return true;
                }

                String level = args[1].toUpperCase();
                int tempo;
                Player alvo;

                try {
                    tempo = Integer.parseInt(args[2]);
                } catch (NumberFormatException e){
                    s.sendMessage(Mensagem.formatar("&cO tempo precisa ser um numero"));
                    return false;
                }

                alvo = Bukkit.getPlayer(args[3]);
                if (alvo == null){
                    s.sendMessage(Mensagem.formatar("&cJogador não existe ou não foi encontrado"));
                    return false;
                }

                if (!level.equals("FACIL") && !level.equals("MEDIO") && !level.equals("DIFICIL") && !level.equals("PESADELO")) {
                    s.sendMessage(Mensagem.formatar("&cNível inválido. Use: FACIL, MEDIO, DIFICIL ou PESADELO."));
                    return false;
                }

                createRegion(alvo, 100);

                s.sendMessage("&a[RAID] &7Iniciando raid &e" + level + " &7para o jogador &e" + alvo.getName() + " &7por &e" + tempo + " &7minutos.");
                return true;
            }
        }
        return false;
    }


}
