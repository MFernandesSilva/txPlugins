package tx.rpg.events;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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

        if (attacker instanceof Player) {
            PlayerData attackerData = txRPG.getInstance().getPlayerData().get(attacker.getUniqueId());
            ItemStack itemInHand = ((Player) attacker).getItemInHand();

            if (itemInHand != null || itemInHand.hasItemMeta()){
                if (NBT.hasNBTKey(itemInHand, "aoe")){
                    int aoe = NBT.getNBT(itemInHand, "aoe", Integer.class);
                    List<Entity> nearbyEntities = attacker.getNearbyEntities(aoe, aoe, aoe);

                    for (Entity entity : nearbyEntities){
                        victim = entity;
                        if (victim instanceof Player) {
                            PlayerData victimData = txRPG.getInstance().getPlayerData().get(victim.getUniqueId());
                            int bloqueio = victimData.getBloqueio();
                            if (calcularBloqueio(bloqueio)) {
                                event.setCancelled(true);
                                ((Player) victim).sendMessage(Mensagem.formatar("&eBloqueio realizado!"));
                            } else {
                                double defesaVictim = victimData.getDefesaFinal();
                                double penDefesaAttacker = attackerData.getPenDefesa();
                                defesaVictim = Math.max(0, defesaVictim - penDefesaAttacker);

                                double dano = event.getDamage() + attackerData.getDanoFinal() - defesaVictim;
                                dano = Math.max(0, dano); // Garante que o dano não seja negativo

                                int novaVida = (int) (txRPG.getInstance().getVidaArmazenada((Player) victim) - dano);
                                novaVida = Math.max(0, novaVida); // Garante que a vida não fique negativa

                                // Aplica a nova vida ao jogador
                                DBC.setHealthCapped((Player) victim, novaVida);

                                // Roubo de vida
                                if (attackerData.getRouboVida() > 0) {
                                    int rouboVida = attackerData.getRouboVida();
                                    int novaVidaAttacker = txRPG.getInstance().getVidaArmazenada((Player) attacker) + rouboVida;
                                    int novaVidaVictim = txRPG.getInstance().getVidaArmazenada((Player) victim) - rouboVida;
                                    novaVidaVictim = Math.max(0, novaVidaVictim);
                                    novaVidaAttacker = Math.max(0, novaVidaAttacker);
                                    DBC.setHealthCapped((Player) attacker, novaVidaAttacker);
                                    DBC.setHealthCapped((Player) victim, novaVidaVictim);
                                }

                                ((Player) victim).sendMessage(Mensagem.formatar("&eBloqueio não realizado!"));
                                event.setDamage(0); // Cancela o dano padrão do evento, pois já foi aplicado manualmente
                            }
                        } else {
                            double penDef = attackerData.getPenDefesa() / 100 * 50;
                            double dano = event.getDamage() + attackerData.getDanoFinal() + penDef;
                            dano = Math.max(0, dano);
                            ((Damageable) event.getEntity()).damage(dano);

                            if (attackerData.getRouboVida() > 0) {
                                int rouboVida = attackerData.getRouboVida();
                                int novaVidaAttacker = txRPG.getInstance().getVidaArmazenada((Player) attacker) + rouboVida;
                                int novaVidaVictim = txRPG.getInstance().getVidaArmazenada((Player) victim) - rouboVida;
                                novaVidaVictim = Math.max(0, novaVidaVictim);
                                novaVidaAttacker = Math.max(0, novaVidaAttacker);
                                DBC.setHealthCapped((Player) attacker, novaVidaAttacker);
                                DBC.setHealthCapped((Player) victim, novaVidaVictim);
                            }
                        }
                    }
                }
            } else {
                if (victim instanceof Player) {
                    PlayerData victimData = txRPG.getInstance().getPlayerData().get(victim.getUniqueId());
                    int bloqueio = victimData.getBloqueio();
                    if (calcularBloqueio(bloqueio)) {
                        event.setCancelled(true);
                        ((Player) victim).sendMessage(Mensagem.formatar("&eBloqueio realizado!"));
                    } else {
                        double defesaVictim = victimData.getDefesaFinal();
                        double penDefesaAttacker = attackerData.getPenDefesa();
                        defesaVictim = Math.max(0, defesaVictim - penDefesaAttacker);

                        double dano = event.getDamage() + attackerData.getDanoFinal() - defesaVictim;
                        dano = Math.max(0, dano); // Garante que o dano não seja negativo

                        int novaVida = (int) (txRPG.getInstance().getVidaArmazenada((Player) victim) - dano);
                        novaVida = Math.max(0, novaVida); // Garante que a vida não fique negativa

                        // Aplica a nova vida ao jogador
                        DBC.setHealthCapped((Player) victim, novaVida);

                        // Roubo de vida
                        if (attackerData.getRouboVida() > 0) {
                            int rouboVida = attackerData.getRouboVida();
                            int novaVidaAttacker = txRPG.getInstance().getVidaArmazenada((Player) attacker) + rouboVida;
                            int novaVidaVictim = txRPG.getInstance().getVidaArmazenada((Player) victim) - rouboVida;
                            novaVidaVictim = Math.max(0, novaVidaVictim);
                            novaVidaAttacker = Math.max(0, novaVidaAttacker);
                            DBC.setHealthCapped((Player) attacker, novaVidaAttacker);
                            DBC.setHealthCapped((Player) victim, novaVidaVictim);
                        }

                        ((Player) victim).sendMessage(Mensagem.formatar("&eBloqueio não realizado!"));
                        event.setDamage(0); // Cancela o dano padrão do evento, pois já foi aplicado manualmente
                    }
                } else {
                    double penDef = attackerData.getPenDefesa() / 100 * 50;
                    double dano = event.getDamage() + attackerData.getDanoFinal() + penDef;
                    dano = Math.max(0, dano);
                    ((Damageable) event.getEntity()).damage(dano);

                    if (attackerData.getRouboVida() > 0) {
                        int rouboVida = attackerData.getRouboVida();
                        int novaVidaAttacker = txRPG.getInstance().getVidaArmazenada((Player) attacker) + rouboVida;
                        int novaVidaVictim = txRPG.getInstance().getVidaArmazenada((Player) victim) - rouboVida;
                        novaVidaVictim = Math.max(0, novaVidaVictim);
                        novaVidaAttacker = Math.max(0, novaVidaAttacker);
                        DBC.setHealthCapped((Player) attacker, novaVidaAttacker);
                        DBC.setHealthCapped((Player) victim, novaVidaVictim);
                    }
                }
            }
        } else if (victim instanceof Player) {
            PlayerData victimData = txRPG.getInstance().getPlayerData().get(victim.getUniqueId());
            int bloqueio = victimData.getBloqueio();
            if (calcularBloqueio(bloqueio)) {
                event.setDamage(0);
                ((Player) victim).sendMessage(Mensagem.formatar("&eBloqueio realizado!"));
            } else {
                double dano = event.getDamage() - victimData.getDefesaFinal();
                dano = Math.max(0, dano);

                int novaVida = (int) (txRPG.getInstance().getVidaArmazenada((Player) victim) - dano);
                novaVida = Math.max(0, novaVida);

                DBC.setHealthCapped((Player) victim, novaVida);

                ((Player) victim).sendMessage(Mensagem.formatar("&eBloqueio não realizado!"));
                event.setDamage(0); // Cancela o dano do evento, já que a vida foi aplicada manualmente
            }
        }
    }

    private boolean calcularBloqueio(int bloqueio) {
        int chance = new Random().nextInt(100);
        return chance < bloqueio;
    }
}
