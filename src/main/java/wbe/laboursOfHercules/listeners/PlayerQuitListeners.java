package wbe.laboursOfHercules.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.util.Utilities;

public class PlayerQuitListeners implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleDataSaveOnQuit(PlayerQuitEvent event) {
        Utilities.savePlayerData(event.getPlayer());
        LaboursOfHercules.activePlayers.remove(event.getPlayer());
    }
}
