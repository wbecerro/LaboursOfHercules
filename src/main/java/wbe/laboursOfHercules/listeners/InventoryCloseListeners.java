package wbe.laboursOfHercules.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.util.Utilities;

import java.util.List;

public class InventoryCloseListeners implements Listener {

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleCrystalLossOnClose(InventoryCloseEvent event) {
        ItemStack bordeItem = event.getInventory().getItem(0);
        if(bordeItem == null) {
            return;
        }
        NamespacedKey currentPageKey = new NamespacedKey(LaboursOfHercules.getInstance(), "currentPage");

        if(!bordeItem.getItemMeta().getPersistentDataContainer().has(currentPageKey)) {
            return;
        }

        if(!(event.getPlayer() instanceof Player player)) {
            return;
        }

        List<ItemStack> crystals = utilities.getCrystalsStuck(event.getInventory());
        if(!crystals.isEmpty()) {
            for(ItemStack crystal : crystals) {
                utilities.addItemToInventory(player, crystal);
            }
        }
    }
}
