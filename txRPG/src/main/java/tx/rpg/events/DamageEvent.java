package tx.rpg.events;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
                event.setDamage(0);

            } else {
                double dano = event.getDamage() + attackerData.getDanoFinal();
                dano = Math.max(0, dano);
                ((Damageable) event.getEntity()).damage(dano);
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

    }
}
