package wbe.laboursOfHercules.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.labours.Labour;
import wbe.laboursOfHercules.labours.tasks.BreakTask;
import wbe.laboursOfHercules.labours.tasks.Task;
import wbe.laboursOfHercules.util.Utilities;

import java.util.List;

public class BlockBreakListeners implements Listener {

    private LaboursOfHercules plugin = LaboursOfHercules.getInstance();

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void updateProgressOnBreakBlock(BlockBreakEvent event) {
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
                if(!(task instanceof BreakTask)) {
                    continue;
                }

                for(Material material : ((BreakTask) task).getBlocks()) {
                    if(!material.equals(event.getBlock().getType())) {
                        continue;
                    }

                    utilities.updateProgress(labour, player, item, task);
                }
            }
        }
    }
}
