package tx.rpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import tx.api.DM;
import tx.api.Mensagem;
import tx.rpg.commands.equipamentosUtils.CustomPrefix;
import tx.rpg.commands.equipamentosUtils.TipoEquipamentoPrompt;
import tx.rpg.txRPG;

public class CriarEquipamentosCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
        DM dm = new DM();

        if (!(s instanceof Player)) {
            s.sendMessage(dm.cc()); // Mensagem de erro para não jogadores
            return true;
        }

        Player player = (Player) s;

        if (!player.hasPermission("txrpg.admin")) {
            player.sendMessage(dm.np()); // Mensagem de erro para falta de permissão
            return true;
        }

        // Registra o listener ANTES de iniciar a conversa
        registerChatListener();

        // Cria a fábrica de conversas
        ConversationFactory cf = createConversationFactory();

        // Inicia a conversa com o jogador
        cf.buildConversation(player).begin();

        return true;
    }

    // Método auxiliar para registrar o listener do chat
    private void registerChatListener() {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerChat(AsyncPlayerChatEvent event) {
                Player player = event.getPlayer();
                if (player.isConversing()) {
                    event.setCancelled(true); // Cancela o chat se o jogador estiver em conversa
                }
            }
        }, txRPG.getInstance());
    }

    // Método auxiliar para criar a fábrica de conversas
    private ConversationFactory createConversationFactory() {
        ConversationFactory cf = new ConversationFactory(txRPG.getInstance())
                .withPrefix(new CustomPrefix())
                .withFirstPrompt(new TipoEquipamentoPrompt())
                .withTimeout(30)
                .thatExcludesNonPlayersWithMessage(Mensagem.formatar("Apenas jogadores podem usar este comando!"));

        cf.addConversationAbandonedListener(new ConversationAbandonedListener() {
            @Override
            public void conversationAbandoned(ConversationAbandonedEvent event) {
                if (!event.gracefulExit()) {
                    Player player = (Player) event.getContext().getForWhom();
                    player.sendMessage(Mensagem.formatar("&cVocê saiu da conversa.")); // Mensagem ao sair da conversa
                }
            }
        });

        return cf;
    }
}
