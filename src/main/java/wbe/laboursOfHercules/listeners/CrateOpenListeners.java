package wbe.laboursOfHercules.listeners;

import com.badbones69.crazycrates.paper.api.events.CrateOpenEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.labours.PlayerLabour;
import wbe.laboursOfHercules.labours.PlayerLabourTask;
import wbe.laboursOfHercules.labours.tasks.CrateTask;
import wbe.laboursOfHercules.labours.tasks.Task;
import wbe.laboursOfHercules.util.Utilities;

import java.util.*;

public class CrateOpenListeners implements Listener {

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void updateProgressOnOpenCrate(CrateOpenEvent event) {
        Player player = event.getPlayer();

        if(event.isCancelled()) {
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

                if(!(task instanceof CrateTask crateTask)) {
                    continue;
                }

                if(!crateTask.getCrates().contains(event.getCrate().getFileName())) {
                    continue;
                }

                utilities.updateProgress(playerLabour, player, labourTask.getKey(), 1);

                if(!LaboursOfHercules.config.updateAllLabours) {
                    return;
                } else {
                    break;
                }
            }
        }
    }
}
