package tx.rank.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import tx.api.Mensagem;
import tx.rank.RankSystem;

import java.util.Random;

public class PlayerAttackEvent implements Listener {

    private final RankSystem rankSystem = new RankSystem();
    private final Random random = new Random();

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Entity entity = event.getEntity();
            String rank = rankSystem.getRank(player);
            double danoOriginal = event.getDamage();
            double danoAdicional = 0;

            switch (rank) {
                case "§7NOVATO":
                    break;
                case "§aGUERREIRO":
                    if (random.nextInt(100) < 2) {
                        danoAdicional = danoOriginal * 0.20;
                    }
                    break;
                case "§9CAMPEÃO":
                    if (random.nextInt(100) < 5) {
                        danoAdicional = danoOriginal * 0.20;
                    }
                    break;
                case "§cMESTRE":
                    if (random.nextInt(100) < 10) {
                        danoAdicional = danoOriginal * 0.20;
                    }
                    break;
                case "§6DEUS":
                    if (random.nextInt(100) < 20) {
                        danoAdicional = danoOriginal * 0.20;
                    }
                    break;
            }

            if (danoAdicional > 0) {
                event.setDamage(danoOriginal + danoAdicional);
                entity.getWorld().strikeLightning(entity.getLocation()); // Chama um raio na localização da entidade
                if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
                    event.setDamage(0);
                }
                player.sendMessage(Mensagem.formatar("&eVocê causou um dano adicional com um raio!"));
            }
        }
    }
}
