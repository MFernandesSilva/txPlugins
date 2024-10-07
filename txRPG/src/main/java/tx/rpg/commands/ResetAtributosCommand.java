package tx.rpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tx.api.DM;
import tx.api.Mensagem;
import tx.rpg.config.Config;
import tx.rpg.data.PlayerData;
import tx.rpg.data.RunasPlayerData;
import tx.rpg.runas.Runa;
import tx.rpg.runas.TipoRuna;
import tx.rpg.txRPG;

import java.util.EnumMap;
import java.util.Map;

public class ResetAtributosCommand implements CommandExecutor {

    private final DM dm = new DM();

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        if (!s.hasPermission("txrpg.admin")) {
            s.sendMessage(dm.np());
            return true;
        }

        // Verifica se o jogador alvo foi especificado
        if (args.length == 0) {
            s.sendMessage(Mensagem.formatar("&cVocê deve especificar um jogador."));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            s.sendMessage(Mensagem.formatar("&cO jogador não está online."));
            return true;
        }

        // Obtém os dados do jogador
        PlayerData playerData = txRPG.getInstance().getPlayerData().get(target.getUniqueId());
        Config config = txRPG.getInstance().getConfiguracao();

        // Reseta os atributos do jogador
        resetPlayerAttributes(playerData, config);

        // Envia mensagens de feedback
        sendFeedbackMessages(s, target, config);

        return true;
    }

    private void resetPlayerAttributes(PlayerData playerData, Config config) {
        playerData.setDano(config.getDanoPadrao());
        playerData.setDefesa(config.getDefesaPadrao());
        playerData.setIntel(config.getIntelPadrao());
        playerData.setAmpCombate(config.getAmpCombatePadrao());
        playerData.setBloqueio(config.getBloqueioPadrao());
        playerData.setSorte(config.getSortePadrao());
        playerData.setAlcance(config.getAlcancePadrao());
        playerData.setRegenMana(config.getRegenManaPadrao());
        playerData.setRegenVida(config.getRegenVidaPadrao());
        playerData.setRouboVida(config.getRouboVidaPadrao());
        playerData.setPenDefesa(config.getPenDefesaPadrao());
    }

    private void sendFeedbackMessages(CommandSender sender, Player target, Config config) {
        String prefix = config.getPrefix();
        if (sender != target) {
            sender.sendMessage(Mensagem.formatar(prefix + "atributos de &e" + target.getName() + " &7foram &cRESETADOS"));
            target.sendMessage(Mensagem.formatar(prefix + "seus atributos foram &cRESETADOS"));
        } else {
            sender.sendMessage(Mensagem.formatar(prefix + "seus atributos foram &cRESETADOS"));
        }
    }
}
