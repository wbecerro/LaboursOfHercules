package wbe.laboursOfHercules.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.labours.PlayerLabour;
import wbe.laboursOfHercules.labours.PlayerLabourTask;
import wbe.laboursOfHercules.labours.tasks.CraftTask;
import wbe.laboursOfHercules.labours.tasks.Task;
import wbe.laboursOfHercules.util.Utilities;

import java.util.*;

public class CraftItemListeners implements Listener {

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void updateProgressOnCraftItem(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.isCancelled()) {
            return;
        }

        if(event.getSlot() != 0) {
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

                if(!(task instanceof CraftTask craftTask)) {
                    continue;
                }

                if(!craftTask.getItems().contains(event.getRecipe().getResult().getType())) {
                    continue;
                }

                int amount = utilities.getCraftedAmount(event);
                if(utilities.updateProgress(playerLabour, player, labourTask.getKey(), amount)) {
                    break;
                }

                if(!LaboursOfHercules.config.updateAllLabours) {
                    return;
                }
            }
        }
    }
}
