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
        int amount = utilities.getCraftedAmount(event);
        for(PlayerLabour playerLabour : labours) {
            for(PlayerLabourTask labourTask : playerLabour.getPlayerTasks()) {
                Task task = labourTask.getTask();
                if(labourTask.isCompleted()) {
                    continue;
                }

                if(!(task instanceof CraftTask craftTask)) {
                    continue;
                }

                if(!craftTask.getItems().contains(event.getRecipe().getResult().getType())) {
                    continue;
                }

                int remaining = labourTask.getMax() - labourTask.getProgress();
                int extra = amount - remaining;
                if(LaboursOfHercules.config.updateAllLabours) {
                    utilities.updateProgress(playerLabour, player, labourTask, amount);
                    break;
                }

                if(extra > 0) {
                    utilities.updateProgress(playerLabour, player, labourTask, amount);
                    amount -= remaining;
                    break;
                } else {
                    utilities.updateProgress(playerLabour, player, labourTask, amount);
                    return;
                }
            }
        }
    }
}
