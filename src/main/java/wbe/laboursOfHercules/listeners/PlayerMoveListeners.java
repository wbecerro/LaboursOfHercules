package wbe.laboursOfHercules.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.labours.PlayerLabour;
import wbe.laboursOfHercules.labours.PlayerLabourTask;
import wbe.laboursOfHercules.labours.tasks.MoveTask;
import wbe.laboursOfHercules.labours.tasks.SwimTask;
import wbe.laboursOfHercules.labours.tasks.Task;
import wbe.laboursOfHercules.util.Utilities;

import java.util.*;

public class PlayerMoveListeners implements Listener {

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void updateProgressOnOpenCrate(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(event.isCancelled()) {
            return;
        }

        int progress = getMovedAmount(event.getFrom().clone(), event.getTo().clone());
        if(progress < 1) {
            return;
        }

        HashMap<UUID, PlayerLabour> playerLabours = LaboursOfHercules.activePlayers.get(player);
        if(playerLabours.isEmpty()) {
            return;
        }

        Collection<PlayerLabour> labours = new ArrayList<>(playerLabours.values());
        for(PlayerLabour playerLabour : labours) {
            for(Map.Entry<PlayerLabourTask, Integer> labourTask : playerLabour.getPlayerTasks().entrySet()) {
                Task task = labourTask.getKey().getTask();
                if(labourTask.getKey().isCompleted()) {
                    continue;
                }

                if(task instanceof MoveTask) {
                    if(utilities.updateProgress(playerLabour, player, labourTask.getKey(), progress)) {
                        break;
                    }

                    if(!LaboursOfHercules.config.updateAllLabours) {
                        return;
                    }
                } else if(task instanceof SwimTask && player.isSwimming()) {
                    if(utilities.updateProgress(playerLabour, player, labourTask.getKey(), progress)) {
                        break;
                    }

                    if(!LaboursOfHercules.config.updateAllLabours) {
                        return;
                    }
                }
            }
        }
    }

    private int getMovedAmount(Location from, Location to) {
        int fromX = from.getBlockX();
        int fromZ = from.getBlockZ();
        int toX = to.getBlockX();
        int toZ = to.getBlockZ();

        if(fromX != toX) {
            return 1;
        }

        if(fromZ != toZ) {
            return 1;
        }

        return 0;
    }
}
