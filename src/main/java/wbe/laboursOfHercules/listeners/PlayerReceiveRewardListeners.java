package wbe.laboursOfHercules.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import wbe.acuaticLostWealth.events.PlayerReceiveRewardEvent;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.labours.Labour;
import wbe.laboursOfHercules.labours.tasks.FishRarityTask;
import wbe.laboursOfHercules.labours.tasks.Task;
import wbe.laboursOfHercules.labours.tasks.WoodcuttingRarityTask;
import wbe.laboursOfHercules.util.Utilities;

import java.util.List;

public class PlayerReceiveRewardListeners implements Listener {

    private LaboursOfHercules plugin = LaboursOfHercules.getInstance();

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void updateProgressOnFishReward(PlayerReceiveRewardEvent event) {
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
                if(!(task instanceof FishRarityTask)) {
                    continue;
                }

                for(String rarity : ((FishRarityTask) task).getRarities()) {
                    if(!rarity.equals(event.getRarity().getInternalName())) {
                        continue;
                    }

                    utilities.updateProgress(labour, player, item, task);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void updateProgressOnWoodcuttingReward(wbe.yggdrasilsBark.events.PlayerReceiveRewardEvent event) {
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
                if(!(task instanceof WoodcuttingRarityTask)) {
                    continue;
                }

                for(String rarity : ((WoodcuttingRarityTask) task).getRarities()) {
                    if(!rarity.equals(event.getRarity().getInternalName())) {
                        continue;
                    }

                    utilities.updateProgress(labour, player, item, task);
                }
            }
        }
    }
}
