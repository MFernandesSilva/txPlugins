package tx.rpg.events;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import tx.api.DBC;
import tx.api.NBT;
import tx.rpg.data.PlayerData;
import tx.rpg.txRPG;

public class DamageEvent implements Listener {

    @EventHandler
    public void damage(EntityDamageByEntityEvent event){
        Entity attacker = event.getDamager();
        Entity victim = event.getEntity();

        if (attacker instanceof Player){
            PlayerData attackerData = txRPG.getInstance().getPlayerData().get(attacker.getUniqueId());
            if (victim instanceof Player){
                PlayerData victimData = txRPG.getInstance().getPlayerData().get(victim.getUniqueId());
                double dano = event.getDamage() + attackerData.getDanoFinal() - victimData.getDefesaFinal();
                dano = Math.max(0, dano);

                double danoOriginal = event.getDamage();
                int vidaOriginal = txRPG.getInstance().getVidaArmazenada((Player) victim);
                //((Player) victim).sendMessage("vida original: " + vidaOriginal);
                int novaVida = (int) (vidaOriginal - dano);
                novaVida = Math.max(0, novaVida);
                //((Player) victim).sendMessage("vida nova: " + novaVida);
                DBC.setHealthCapped((Player) victim, novaVida);
                //((Player) victim).sendMessage("dano tomado: " + dano);
                if (attackerData.getRouboVida() > 0){
                    int rouboVida = attackerData.getRouboVida();
                    int vidaAttacker = txRPG.getInstance().getVidaArmazenada((Player) attacker);
                    int vidaAlvo = txRPG.getInstance().getVidaArmazenada((Player) victim);
                    int novaVidaAlvo = vidaAlvo - rouboVida;
                    novaVidaAlvo = Math.max(0, novaVidaAlvo);
                    DBC.setHealthCapped((Player) victim, novaVidaAlvo);
                    int novaVidaAttacker = vidaAttacker + rouboVida;
                    novaVidaAttacker = Math.max(0, novaVidaAttacker);
                    DBC.setHealthCapped((Player) attacker, novaVidaAttacker);
                    ((Player) attacker).sendMessage("log lifesteal");
                }
                event.setDamage(0);

            } else {
                double dano = event.getDamage() + attackerData.getDanoFinal();
                dano = Math.max(0, dano);
                ((Damageable) event.getEntity()).damage(dano);

                if (attackerData.getRouboVida() > 0){
                    int rouboVida = attackerData.getRouboVida();
                    int vidaAttacker = txRPG.getInstance().getVidaArmazenada((Player) attacker);
                    int vidaAlvo = (int) ((Damageable) event.getEntity()).getHealth();
                    int novaVidaAlvo = vidaAlvo - rouboVida;
                    novaVidaAlvo = Math.max(0, novaVidaAlvo);
                    ((Damageable) event.getEntity()).setHealth(novaVidaAlvo);
                    int novaVidaAttacker = vidaAttacker + rouboVida;
                    novaVidaAttacker = Math.max(0, novaVidaAttacker);
                    DBC.setHealthCapped((Player) attacker, novaVidaAttacker);
                    ((Player) attacker).sendMessage("log lifesteal");
                }
            }
        } else if (victim instanceof Player){
            PlayerData victimData = txRPG.getInstance().getPlayerData().get(victim.getUniqueId());
            double dano = event.getDamage() - victimData.getDefesaFinal();
            dano = Math.max(0, dano);
            int vidaOriginal = txRPG.getInstance().getVidaArmazenada((Player) victim);
            //(Player) victim).sendMessage("vida original: " + vidaOriginal);
            int novaVida = (int) (vidaOriginal - dano);
            novaVida = Math.max(0, novaVida);
            //((Player) victim).sendMessage("vida nova: " + novaVida);
            DBC.setHealthCapped((Player) victim, novaVida);
            //((Player) victim).sendMessage("dano tomado: " + dano);
            event.setDamage(0);
        }

        if (event.getEntity() instanceof Player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
                event.setCancelled(true);
            }
        }

    }
}
