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
            for(PlayerLabourTask labourTask : playerLabour.getPlayerTasks()) {
                Task task = labourTask.getTask();
                if(labourTask.isCompleted()) {
                    continue;
                }

                if(task instanceof MoveTask) {
                    utilities.updateProgress(playerLabour, player, labourTask, progress);

                    if(!LaboursOfHercules.config.updateAllLabours) {
                        return;
                    } else {
                        break;
                    }
                } else if(task instanceof SwimTask && player.isSwimming()) {
                    utilities.updateProgress(playerLabour, player, labourTask, progress);

                    if(!LaboursOfHercules.config.updateAllLabours) {
                        return;
                    } else {
                        break;
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
