package wbe.laboursOfHercules.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.items.CrystalItem;
import wbe.laboursOfHercules.items.LabourItem;
import wbe.laboursOfHercules.labours.Crystal;
import wbe.laboursOfHercules.labours.Labour;
import wbe.laboursOfHercules.util.Utilities;

public class PlayerInteractListeners implements Listener {

    private LaboursOfHercules plugin = LaboursOfHercules.getInstance();

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void useRandomLabourOnInteract(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }

        ItemStack item = event.getItem();
        if(item == null) {
            return;
        }

        if(item.getType().equals(Material.AIR)) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            return;
        }

        NamespacedKey labourKey = new NamespacedKey(plugin, "randomLabour");
        NamespacedKey crystalKey = new NamespacedKey(plugin, "randomCrystal");
        Labour labour = utilities.getRandomLabour();
        if(meta.getPersistentDataContainer().has(labourKey)) {
            item.setAmount(item.getAmount() - 1);
            event.getPlayer().getInventory().addItem(new LabourItem(labour));
            event.setCancelled(true);
        } else if(meta.getPersistentDataContainer().has(crystalKey)) {
            item.setAmount(item.getAmount() - 1);
            Crystal crystal = LaboursOfHercules.config.crystals.get(labour.getId());
            event.getPlayer().getInventory().addItem(new CrystalItem(crystal));
            event.setCancelled(true);
        }
    }
}
