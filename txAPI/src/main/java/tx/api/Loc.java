package tx.api;

import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class Loc {
    public static Location getLocalizacao(Entity entidade) {
        if (entidade == null)
            throw new IllegalArgumentException("Entidade não pode ser nula.");
        return entidade.getLocation();
    }

    public static double calcularDistancia(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null)
            throw new IllegalArgumentException("Localizações não podem ser nulas.");
        if (!mesmoMundo(loc1, loc2))
            throw new IllegalArgumentException("Localizações devem estar no mesmo mundo.");
        return loc1.distance(loc2);
    }

    public static double calcularDistanciaQuadrada(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null)
            throw new IllegalArgumentException("Localizações não podem ser nulas.");
        if (!mesmoMundo(loc1, loc2))
            throw new IllegalArgumentException("Localizações devem estar no mesmo mundo.");
        return loc1.distanceSquared(loc2);
    }

    public static boolean mesmoMundo(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null)
            throw new IllegalArgumentException("Localizações não podem ser nulas.");
        return (loc1.getWorld() == loc2.getWorld());
    }

    public static boolean estaDentroDaArea(Location loc, Location canto1, Location canto2) {
        if (loc == null || canto1 == null || canto2 == null)
            throw new IllegalArgumentException("Localizações não podem ser nulas.");
        if (!mesmoMundo(loc, canto1) || !mesmoMundo(loc, canto2))
            throw new IllegalArgumentException("Localizações devem estar no mesmo mundo.");
        double minX = Math.min(canto1.getX(), canto2.getX());
        double maxX = Math.max(canto1.getX(), canto2.getX());
        double minY = Math.min(canto1.getY(), canto2.getY());
        double maxY = Math.max(canto1.getY(), canto2.getY());
        double minZ = Math.min(canto1.getZ(), canto2.getZ());
        double maxZ = Math.max(canto1.getZ(), canto2.getZ());
        return (loc.getX() >= minX && loc.getX() <= maxX && loc
                .getY() >= minY && loc.getY() <= maxY && loc
                .getZ() >= minZ && loc.getZ() <= maxZ);
    }

    public static Location getLocalizacaoCentral(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null)
            throw new IllegalArgumentException("Localizações não podem ser nulas.");
        if (!mesmoMundo(loc1, loc2))
            throw new IllegalArgumentException("Localizações devem estar no mesmo mundo.");
        double x = (loc1.getX() + loc2.getX()) / 2.0D;
        double y = (loc1.getY() + loc2.getY()) / 2.0D;
        double z = (loc1.getZ() + loc2.getZ()) / 2.0D;
        World mundo = loc1.getWorld();
        return new Location(mundo, x, y, z);
    }

    public static Location encontrarLocalizacaoMaisProxima(Location origem, Collection<Location> localizacoes) {
        if (origem == null || localizacoes == null || localizacoes.isEmpty())
            throw new IllegalArgumentException("Parâmetros inválidos.");
        Location localizacaoMaisProxima = null;
        double menorDistancia = Double.MAX_VALUE;
        for (Location loc : localizacoes) {
            if (!mesmoMundo(origem, loc))
                continue;
            double distancia = calcularDistanciaQuadrada(origem, loc);
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                localizacaoMaisProxima = loc;
            }
        }
        return localizacaoMaisProxima;
    }

    public static Vector calcularDirecao(Location de, Location para) {
        if (de == null || para == null)
            throw new IllegalArgumentException("Localizações não podem ser nulas.");
        if (!mesmoMundo(de, para))
            throw new IllegalArgumentException("Localizações devem estar no mesmo mundo.");
        return para.toVector().subtract(de.toVector()).normalize();
    }
}

