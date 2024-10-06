package tx.rpg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

public class LightningEvent implements Listener {

    @EventHandler
    public void onLightningStrike(LightningStrikeEvent event) {
        event.getLightning().setFireTicks(0);
    }
}
