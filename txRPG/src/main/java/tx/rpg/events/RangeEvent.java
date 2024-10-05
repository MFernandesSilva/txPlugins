package tx.rpg.events;

import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import tx.rpg.data.PlayerData;
import tx.rpg.txRPG;

public class RangeEvent implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            PlayerData playerData = txRPG.getInstance().getPlayerData().get(player.getUniqueId());

            LivingEntity target = null;
            double closestDistance = Double.MAX_VALUE;
            for (Entity entity : player.getNearbyEntities(playerData.getAlcance(), playerData.getAlcance(), playerData.getAlcance())) {
                if (entity instanceof LivingEntity && !entity.equals(player)) {
                    double distance = player.getLocation().distance(entity.getLocation());
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        target = (LivingEntity) entity;
                    }
                }
            }

            if (target != null) {
                Arrow arrow = player.launchProjectile(Arrow.class);

                ((CraftArrow) arrow).getHandle().setInvisible(true);

                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(arrow.getEntityId());
                    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
                }


                ((CraftArrow) arrow).getHandle().fromPlayer = 2;
                arrow.setVelocity(player.getLocation().getDirection().multiply(2));

                new BukkitRunnable() {
                    int ticks = 0;

                    @Override
                    public void run() {
                        ticks++;

                        if (ticks > 20) {
                            arrow.remove();
                            cancel();
                            return;
                        }

                        if (arrow.isValid()) {
                            for (Entity entity : arrow.getNearbyEntities(1, 1, 1)) {
                                if (entity instanceof LivingEntity && !entity.equals(player)) {
                                    LivingEntity hitEntity = (LivingEntity) entity;
                                    EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
                                    EntityLiving nmsTarget = ((CraftLivingEntity) hitEntity).getHandle();

                                    double distance = ((CraftPlayer) nmsPlayer.getBukkitEntity()).getLocation()
                                            .distance(((CraftLivingEntity) nmsTarget.getBukkitEntity()).getLocation());

                                    double horizontalDistance = Math.sqrt(
                                            Math.pow(nmsPlayer.locX - nmsTarget.locX, 2) +
                                                    Math.pow(nmsPlayer.locZ - nmsTarget.locZ, 2)
                                    );

                                    if (hitEntity instanceof Player){
                                        PlayerData victimData = txRPG.getInstance().getPlayerData().get(hitEntity.getUniqueId());
                                        if (horizontalDistance <= playerData.getAlcance() + 0.5){
                                            hitEntity.damage(calcularDano(playerData) - victimData.getDefesaFinal(), player);
                                        }
                                    } else {
                                        if (horizontalDistance <= playerData.getAlcance() + 0.5) {
                                            hitEntity.damage(calcularDano(playerData), player);
                                        }
                                    }

                                    arrow.remove();
                                    cancel();
                                    return;
                                }
                            }
                        }
                    }
                }.runTaskTimer(txRPG.getInstance(), 0, 1);
            }
        }
    }

    private double calcularDano(PlayerData playerData) {
        double danofinal = playerData.getDanoFinal();
        return danofinal;
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            event.getEntity().remove();
        }
    }
}