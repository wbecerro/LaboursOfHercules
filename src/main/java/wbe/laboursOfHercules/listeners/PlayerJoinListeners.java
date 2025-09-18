package wbe.laboursOfHercules.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import wbe.laboursOfHercules.util.Utilities;

public class PlayerJoinListeners implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleDataSaveOnJoin(PlayerJoinEvent event) {
        Utilities.loadPlayerData(event.getPlayer());
    }
}
