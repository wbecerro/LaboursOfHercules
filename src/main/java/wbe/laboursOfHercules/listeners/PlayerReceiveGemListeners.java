package wbe.laboursOfHercules.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.labours.PlayerLabour;
import wbe.laboursOfHercules.labours.PlayerLabourTask;
import wbe.laboursOfHercules.labours.tasks.GemTask;
import wbe.laboursOfHercules.labours.tasks.Task;
import wbe.laboursOfHercules.util.Utilities;
import wbe.tartarusRiches.events.PlayerReceiveGemEvent;

import java.util.*;

public class PlayerReceiveGemListeners implements Listener {

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void updateProgressOnOpenCrate(PlayerReceiveGemEvent event) {
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
            for(PlayerLabourTask labourTask : playerLabour.getPlayerTasks()) {
                Task task = labourTask.getTask();
                if(labourTask.isCompleted()) {
                    continue;
                }

                if(!(task instanceof GemTask)) {
                    continue;
                }

                utilities.updateProgress(playerLabour, player, labourTask, 1);

                if(!LaboursOfHercules.config.updateAllLabours) {
                    return;
                } else {
                    break;
                }
            }
        }
    }
}
