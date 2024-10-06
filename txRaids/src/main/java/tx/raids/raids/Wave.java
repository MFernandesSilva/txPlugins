package tx.raids.raids;

import org.bukkit.World;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;

public class Wave {
    private int zombieCount;
    private int zombieHealth;
    private int zombieDamage;
    private int skeletonCount;
    private int skeletonHealth;
    private int skeletonDamage;
    private List<Entity> spawnedMobs;

    public Wave(int zombieCount, int zombieHealth, int zombieDamage) {
        this.zombieCount = zombieCount;
        this.zombieHealth = zombieHealth;
        this.zombieDamage = zombieDamage;
        this.spawnedMobs = new ArrayList<>();
    }

    public Wave(int zombieCount, int zombieHealth, int zombieDamage, int skeletonCount, int skeletonHealth, int skeletonDamage) {
        this(zombieCount, zombieHealth, zombieDamage);
        this.skeletonCount = skeletonCount;
        this.skeletonHealth = skeletonHealth;
        this.skeletonDamage = skeletonDamage;
    }

    public void spawnMobs(Player player) {
        World world = player.getWorld();
        for (int i = 0; i < zombieCount; i++) {
            Zombie zombie = world.spawn(player.getLocation(), Zombie.class);
            zombie.setHealth(zombieHealth);
            zombie.setCustomName("Zombie");
            zombie.setCustomNameVisible(true);
            spawnedMobs.add(zombie);
        }

        for (int i = 0; i < skeletonCount; i++) {
            Skeleton skeleton = world.spawn(player.getLocation(), Skeleton.class);
            skeleton.setHealth(skeletonHealth);
            skeleton.setCustomName("Skeleton");
            skeleton.setCustomNameVisible(true);
            spawnedMobs.add(skeleton);
        }
    }

    public boolean areAllMobsDefeated() {
        return spawnedMobs.stream().allMatch(Entity::isDead);
    }
}