package tx.rpg.events;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import tx.api.DBC;
import tx.api.Mensagem;
import tx.api.NBT;
import tx.rpg.data.PlayerData;
import tx.rpg.txRPG;

import java.util.List;
import java.util.Random;

public class DamageEvent implements Listener {

    @EventHandler
    public void damage(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity victim = event.getEntity();

        // Verifica se o atacante é um jogador e se não é um ataque de flecha
        if (attacker instanceof Player && !(attacker instanceof Arrow)) {
            handlePlayerDamage(event, (Player) attacker, victim);
        } else if (victim instanceof Player && !(event.getDamager() instanceof Arrow)) {
            handlePlayerDamage(event, (Player) victim, attacker);
        }
    }

    private void handlePlayerDamage(EntityDamageByEntityEvent event, Player attacker, Entity victim) {
        PlayerData attackerData = txRPG.getInstance().getPlayerData().get(attacker.getUniqueId());
        ItemStack itemInHand = attacker.getInventory().getItemInHand(); // Atualizado para o método correto

        if (itemInHand != null && itemInHand.hasItemMeta() && NBT.hasNBTKey(itemInHand, "aoe")) {
            handleAoeDamage(event, attacker, victim, attackerData, itemInHand);
        } else {
            if (victim instanceof Player) {
                handlePlayerToPlayerDamage(event, attacker, (Player) victim, attackerData);
            } else {
                handlePlayerToEntityDamage(event, attacker, victim, attackerData);
            }
        }
    }

    private void handleAoeDamage(EntityDamageByEntityEvent event, Player attacker, Entity victim, PlayerData attackerData, ItemStack itemInHand) {
        int aoe = NBT.getNBT(itemInHand, "aoe", Integer.class);
        List<Entity> nearbyEntities = attacker.getNearbyEntities(aoe, aoe, aoe);

        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity && entity != victim) { // Evita atacar a vítima original
                if (entity instanceof Player) {
                    handlePlayerToPlayerDamage(event, attacker, (Player) entity, attackerData);
                } else {
                    handlePlayerToEntityDamage(event, attacker, entity, attackerData);
                }
            }
        }
    }

    private void handlePlayerToPlayerDamage(EntityDamageByEntityEvent event, Player attacker, Player victim, PlayerData attackerData) {
        PlayerData victimData = txRPG.getInstance().getPlayerData().get(victim.getUniqueId());
        if (calcularBloqueio(victimData.getBloqueio())) {
            int vidaVictim = txRPG.getInstance().getVidaArmazenada(victim);
            DBC.setHealthCapped(victim, vidaVictim);
            event.setDamage(0);
            victim.sendMessage(Mensagem.formatar("&eBloqueio realizado!"));
        } else {
            applyDamage(event, attacker, victim, attackerData, victimData);
        }
    }

    private void handlePlayerToEntityDamage(EntityDamageByEntityEvent event, Player attacker, Entity victim, PlayerData attackerData) {
        double penDef = attackerData.getPenDefesa() / 100 * 50;
        double dano = event.getDamage() + attackerData.getDanoFinal() + penDef;
        dano = Math.max(0, dano);
        ((Damageable) victim).damage(dano);
        applyLifeSteal(attacker, victim, attackerData);
    }

    private void applyDamage(EntityDamageByEntityEvent event, Player attacker, Player victim, PlayerData attackerData, PlayerData victimData) {
        double defesaVictim = Math.max(0, victimData.getDefesaFinal() - attackerData.getPenDefesa());
        double dano = Math.max(0, event.getDamage() + attackerData.getDanoFinal() - defesaVictim);
        int novaVida = (int) (txRPG.getInstance().getVidaArmazenada(victim) - dano);
        novaVida = Math.max(0, novaVida);

        DBC.setHealthCapped(victim, novaVida);
        applyLifeSteal(attacker, victim, attackerData);
        victim.sendMessage(Mensagem.formatar("&eBloqueio não realizado!"));
        event.setDamage(0); // Cancela o dano padrão do evento
    }

    private void applyLifeSteal(Player attacker, Entity victim, PlayerData attackerData) {
        if (attackerData.getRouboVida() > 0) {
            if (victim instanceof Player) {

                int rouboVida = attackerData.getRouboVida();
                int novaVidaAttacker = txRPG.getInstance().getVidaArmazenada(attacker) + rouboVida;
                int novaVidaVictim = txRPG.getInstance().getVidaArmazenada((Player) victim) - rouboVida;

                novaVidaVictim = Math.max(0, novaVidaVictim);
                novaVidaAttacker = Math.max(0, novaVidaAttacker);
                DBC.setHealthCapped(attacker, novaVidaAttacker);
                DBC.setHealthCapped((Player) victim, novaVidaVictim);
            } else {
                int rouboVida = attackerData.getRouboVida();
                int novaVidaAttacker = txRPG.getInstance().getVidaArmazenada(attacker) + rouboVida;
                novaVidaAttacker = Math.max(0, novaVidaAttacker);
                ((Damageable)victim).damage(rouboVida);
                DBC.setHealthCapped(attacker, novaVidaAttacker);
            }
        }
    }

    private boolean calcularBloqueio(int bloqueio) {
        return new Random().nextInt(100) < bloqueio;
    }
}
