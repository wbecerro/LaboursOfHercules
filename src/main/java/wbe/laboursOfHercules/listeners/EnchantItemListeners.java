package wbe.laboursOfHercules.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.labours.PlayerLabour;
import wbe.laboursOfHercules.labours.PlayerLabourTask;
import wbe.laboursOfHercules.labours.tasks.EnchantTask;
import wbe.laboursOfHercules.labours.tasks.Task;
import wbe.laboursOfHercules.util.Utilities;

import java.util.*;

public class EnchantItemListeners implements Listener {

    private LaboursOfHercules plugin = LaboursOfHercules.getInstance();

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void updateProgressOnEnchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();

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

                if(!(task instanceof EnchantTask enchantTask)) {
                    continue;
                }

                if(!enchantTask.getItems().contains(event.getItem().getType())) {
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
