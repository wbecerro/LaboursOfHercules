package wbe.laboursOfHercules.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.labours.Labour;
import wbe.laboursOfHercules.labours.tasks.GemTask;
import wbe.laboursOfHercules.labours.tasks.Task;
import wbe.laboursOfHercules.util.Utilities;
import wbe.tartarusRiches.events.PlayerReceiveGemEvent;

import java.util.List;

public class PlayerReceiveGemListeners implements Listener {

    private LaboursOfHercules plugin = LaboursOfHercules.getInstance();

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void updateProgressOnOpenCrate(PlayerReceiveGemEvent event) {
        Player player = event.getPlayer();
        if(event.isCancelled()) {
            return;
        }

        List<ItemStack> labours = utilities.getLaboursFromInventory(player);
        if(labours.isEmpty()) {
            return;
        }

        NamespacedKey baseKey = new NamespacedKey(plugin, "labour");
        for(ItemStack item : labours) {
            String tier = item.getItemMeta().getPersistentDataContainer().get(baseKey, PersistentDataType.STRING);
            Labour labour = LaboursOfHercules.config.labours.get(tier);
            NamespacedKey tasksKey = new NamespacedKey(plugin, "tasks");
            String tasks = item.getItemMeta().getPersistentDataContainer().get(tasksKey, PersistentDataType.STRING);
            String[] tasksParts = tasks.split("\\.");
            for(String taskId : tasksParts) {
                Task task = labour.getTasks().get(taskId);
                if(!(task instanceof GemTask)) {
                    continue;
                }

                utilities.updateProgress(labour, player, item, task);
            }
        }
    }
}
