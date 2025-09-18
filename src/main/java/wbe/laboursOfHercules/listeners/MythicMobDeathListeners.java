package wbe.laboursOfHercules.listeners;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.labours.PlayerLabour;
import wbe.laboursOfHercules.labours.PlayerLabourTask;
import wbe.laboursOfHercules.labours.tasks.MMKillTask;
import wbe.laboursOfHercules.labours.tasks.Task;
import wbe.laboursOfHercules.util.Utilities;

import java.util.*;

public class MythicMobDeathListeners implements Listener {

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void updateProgressOnMythicMobsDeath(MythicMobDeathEvent event) {
        LivingEntity entity = event.getKiller();
        if(!(entity instanceof Player player)) {
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

                if(!(task instanceof MMKillTask mmKillTask)) {
                    continue;
                }

                if(!mmKillTask.getMobs().contains(event.getMobType().getInternalName())) {
                    continue;
                }

                if(utilities.updateProgress(playerLabour, player, labourTask.getKey(), 1)) {
                    break;
                }

                if(!LaboursOfHercules.config.updateAllLabours) {
                    return;
                }
            }
        }
    }
}
