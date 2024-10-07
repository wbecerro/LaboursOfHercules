package wbe.laboursOfHercules.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.events.CrystalUseEvent;
import wbe.laboursOfHercules.labours.Labour;
import wbe.laboursOfHercules.labours.tasks.Task;
import wbe.laboursOfHercules.util.Utilities;

public class InventoryClickListeners implements Listener {

    private LaboursOfHercules plugin = LaboursOfHercules.getInstance();

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void applyCrystalOnClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(!event.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)) {
            return;
        }

        ItemStack cursorItem = event.getCursor();
        ItemMeta cursorMeta = cursorItem.getItemMeta();
        if(cursorMeta == null) {
            return;
        }

        NamespacedKey baseKey = new NamespacedKey(plugin, "crystal");
        if(!cursorMeta.getPersistentDataContainer().has(baseKey)) {
            return;
        }

        String type = cursorMeta.getPersistentDataContainer().get(baseKey, PersistentDataType.STRING);
        ItemStack inventoryItem = event.getCurrentItem();
        ItemMeta inventoryMeta = inventoryItem.getItemMeta();
        if(inventoryMeta == null) {
            return;
        }

        if(inventoryItem.getAmount() != 1) {
            return;
        }

        NamespacedKey labourKey = new NamespacedKey(plugin, "labour");
        if(!inventoryMeta.getPersistentDataContainer().has(labourKey)) {
            return;
        }

        if(!type.equalsIgnoreCase(inventoryMeta.getPersistentDataContainer().get(labourKey, PersistentDataType.STRING))) {
            return;
        }

        ItemStack newItem = new ItemStack(inventoryItem.getType());
        newItem.setItemMeta(inventoryMeta);
        newItem.getItemMeta().setLore(inventoryMeta.getLore());

        Labour labour = LaboursOfHercules.config.labours.get(type);
        Task task = utilities.getFirstTask(inventoryItem, labour);
        NamespacedKey tasksKey = new NamespacedKey(plugin, "tasks");
        NamespacedKey taskKey = new NamespacedKey(plugin, task.getId());
        NamespacedKey taskMaxKey = new NamespacedKey(plugin, task.getId() + "_max");
        int line = utilities.findLine(task.getLore().split("%amount%"), inventoryMeta.getLore());
        int maxAmount = inventoryMeta.getPersistentDataContainer().get(taskMaxKey, PersistentDataType.INTEGER);
        boolean finished = utilities.updateTaskLine(player, newItem, taskKey, taskMaxKey, line, tasksKey, maxAmount, maxAmount, task, labour, true);

        cursorItem.setAmount(cursorItem.getAmount() - 1);
        player.sendMessage(LaboursOfHercules.messages.crystalApplied.replace("%crystal%", cursorMeta.getDisplayName()));
        player.setItemOnCursor(cursorItem);
        if(finished) {
            event.setCurrentItem(null);
        } else {
            event.setCurrentItem(newItem);
            event.setCancelled(true);
        }
        plugin.getServer().getPluginManager().callEvent(new CrystalUseEvent(player, newItem, cursorItem, labour));
        player.updateInventory();
    }
}
