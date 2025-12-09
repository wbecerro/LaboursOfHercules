package wbe.laboursOfHercules.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.events.CrystalUseEvent;
import wbe.laboursOfHercules.items.LabourItem;
import wbe.laboursOfHercules.items.SummaryItem;
import wbe.laboursOfHercules.labours.PlayerLabour;
import wbe.laboursOfHercules.labours.PlayerLabourTask;
import wbe.laboursOfHercules.util.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MenuListener implements Listener {

    private static Utilities utilities = new Utilities();

    private static void fillBorders(Inventory inventory, int page) {
        ItemStack borde = new ItemStack(LaboursOfHercules.config.menuBorder);
        ItemMeta bordeMeta = borde.getItemMeta();
        NamespacedKey currentPage = new NamespacedKey(LaboursOfHercules.getInstance(), "currentPage");
        bordeMeta.setDisplayName(" ");
        bordeMeta.getPersistentDataContainer().set(currentPage, PersistentDataType.INTEGER, page);
        borde.setItemMeta(bordeMeta);

        for(int i = 0; i < inventory.getSize(); i++) {
            // Primera fila
            if(i<9) {
                inventory.setItem(i, borde);
            }
            // Columna izquierda
            if(i % 9 == 0) {
                inventory.setItem(i, borde);
            }
            // Columna derecha
            if(i % 9 == 8) {
                inventory.setItem(i, borde);
            }
            // Última fila
            if(i >= 45){
                inventory.setItem(i, borde);
            }
        }
    }

    public static void fillLabours(Inventory inventory, int page, ArrayList<PlayerLabour> labours) {
        int maxTalismanToShow = 28*page;
        int talismanIndex = 28*(page-1);
        if(28*page > labours.size()) {
            maxTalismanToShow = labours.size();
        }

        for(int i = 0; i < inventory.getSize(); i++) {
            // Primera fila
            if(i<9) {
                continue;
            }
            // Columna izquierda
            if(i % 9 == 0) {
                continue;
            }
            // Columna derecha
            if(i % 9 == 8) {
                continue;
            }
            // Última fila
            if(i >= 45){
                continue;
            }

            if(talismanIndex < maxTalismanToShow) {
                PlayerLabour labour = labours.get(talismanIndex);
                ItemStack labourItem = new LabourItem(labour);
                inventory.setItem(i, labourItem);
                talismanIndex++;
            }
        }
    }

    public static void openMenu(Player player, int page) throws Exception {
        ArrayList<PlayerLabour> playerLabours = new ArrayList<>(LaboursOfHercules.activePlayers.get(player).values());
        if(playerLabours.size() == 0) {
            throw new Exception(LaboursOfHercules.messages.noTalismansFound);
        }

        int necesaryPages = (int) Math.ceil((double) playerLabours.size() / 28);
        if(page > necesaryPages) {
            throw new Exception(LaboursOfHercules.messages.pageNotFound);
        }
        NamespacedKey goToPage = new NamespacedKey(LaboursOfHercules.getInstance(), "goToPage");

        Inventory inventory = Bukkit.createInventory(null, 54, LaboursOfHercules.config.menuTitle
                .replace("%active%", String.valueOf(playerLabours.size()))
                .replace("%total%", String.valueOf(LaboursOfHercules.config.maxLaboursPerPlayer)));
        fillBorders(inventory, page);
        fillLabours(inventory, page, playerLabours);

        List<PlayerLabourTask> tasks = utilities.getGroupedTasks(player);
        inventory.setItem(LaboursOfHercules.config.summarySlot, new SummaryItem(tasks, 1));
        if(necesaryPages > page) {
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta nextPageMeta = nextPage.getItemMeta();
            nextPageMeta.setDisplayName(
                LaboursOfHercules.messages.menuNextPage.replace("%next_page%", String.valueOf(page+1))
            );
            nextPageMeta.getPersistentDataContainer().set(goToPage, PersistentDataType.INTEGER, page+1);
            nextPage.setItemMeta(nextPageMeta);
            inventory.setItem(53, nextPage);
        }

        if(page > 1) {
            ItemStack backPage = new ItemStack(Material.ARROW);
            ItemMeta backPageMeta = backPage.getItemMeta();
            backPageMeta.setDisplayName(
                LaboursOfHercules.messages.menuPreviousPage.replace("%previous_page%", String.valueOf(page-1))
            );
            backPageMeta.getPersistentDataContainer().set(goToPage, PersistentDataType.INTEGER, page-1);

            backPage.setItemMeta(backPageMeta);
            inventory.setItem(45, backPage);
        }

        player.openInventory(inventory);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack bordeItem = event.getInventory().getItem(0);
        Inventory inventory = event.getInventory();
        if(bordeItem == null) {
            return;
        }
        NamespacedKey currentPageKey = new NamespacedKey(LaboursOfHercules.getInstance(), "currentPage");

        if(!bordeItem.getItemMeta().getPersistentDataContainer().has(currentPageKey)) {
            return;
        }

        int currentPage = bordeItem.getItemMeta().getPersistentDataContainer().get(
            currentPageKey, PersistentDataType.INTEGER
        );

        if(!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        ItemStack item = event.getCurrentItem();
        if(item == null || item.getType() == Material.AIR) {
            return;
        }
        NamespacedKey goToPage = new NamespacedKey(LaboursOfHercules.getInstance(), "goToPage");

        // Clic en flecha de cambio de página
        ItemMeta meta = item.getItemMeta();
        if(meta.getPersistentDataContainer().has(goToPage)) {
            int page = meta.getPersistentDataContainer().get(goToPage, PersistentDataType.INTEGER);
            try {
                openMenu(player, page);
            } catch(Exception e) {
                player.sendMessage(LaboursOfHercules.messages.pageNotFound);
            }
        }

        NamespacedKey summaryKey = new NamespacedKey(LaboursOfHercules.getInstance(), "summaryPage");
        if(event.getClick().equals(ClickType.LEFT) && meta.getPersistentDataContainer().has(summaryKey)) {
            int page = meta.getPersistentDataContainer().get(summaryKey, PersistentDataType.INTEGER) + 1;
            List<PlayerLabourTask> tasks = utilities.getGroupedTasks(player);
            inventory.setItem(LaboursOfHercules.config.summarySlot, new SummaryItem(tasks, page));
            event.setCancelled(true);
            return;
        } else if(event.getClick().equals(ClickType.RIGHT) && meta.getPersistentDataContainer().has(summaryKey)) {
            int page = meta.getPersistentDataContainer().get(summaryKey, PersistentDataType.INTEGER) - 1;
            List<PlayerLabourTask> tasks = utilities.getGroupedTasks(player);
            inventory.setItem(LaboursOfHercules.config.summarySlot, new SummaryItem(tasks, page));
            event.setCancelled(true);
            return;
        }

        NamespacedKey labourKey = new NamespacedKey(LaboursOfHercules.getInstance(), "labour");
        if(event.getClick().equals(ClickType.RIGHT) && meta.getPersistentDataContainer().has(labourKey)) {
            UUID uuid = UUID.fromString(meta.getPersistentDataContainer().get(labourKey, PersistentDataType.STRING));
            utilities.removeLabour(player, uuid);
            event.setCancelled(true);
            try {
                openMenu(player, currentPage);
            } catch(Exception ex) {
                player.closeInventory();
            }
            return;
        }

        // Aplicación del cristal
        NamespacedKey crystalKey = new NamespacedKey(LaboursOfHercules.getInstance(), "crystal");
        if(event.getAction().equals(InventoryAction.SWAP_WITH_CURSOR) && item.getType() != Material.AIR) {
            ItemStack cursorItem = event.getCursor();
            ItemMeta cursorMeta = cursorItem.getItemMeta();
            if(cursorMeta == null) {
                event.setCancelled(true);
                return;
            }

            // Comprobación de si es cristal
            if(!cursorMeta.getPersistentDataContainer().has(crystalKey)) {
                event.setCancelled(true);
                return;
            }

            String crystalType = cursorMeta.getPersistentDataContainer().get(crystalKey, PersistentDataType.STRING);
            ItemMeta inventoryMeta = item.getItemMeta();
            NamespacedKey uuidKey = new NamespacedKey(LaboursOfHercules.getInstance(), "labour");
            // Comprobación de si es una labor
            if(!inventoryMeta.getPersistentDataContainer().has(uuidKey)) {
                event.setCancelled(true);
                return;
            }

            UUID uuid = UUID.fromString(inventoryMeta.getPersistentDataContainer().get(uuidKey, PersistentDataType.STRING));
            PlayerLabour playerLabour = LaboursOfHercules.activePlayers.get(player).get(uuid);
            // Comprobación de si es el mismo tipo
            if(!crystalType.equalsIgnoreCase(playerLabour.getLabour().getId())) {
                event.setCancelled(true);
                return;
            }

            cursorItem.setAmount(cursorItem.getAmount() - 1);
            player.sendMessage(LaboursOfHercules.messages.crystalApplied.replace("%crystal%", cursorMeta.getDisplayName()));
            boolean finished = utilities.applyCrystal(playerLabour, player);
            if(finished) {
                try {
                    openMenu(player, currentPage);
                } catch(Exception e) {
                    player.sendMessage(LaboursOfHercules.messages.pageNotFound);
                }
            } else {
                event.setCurrentItem(new LabourItem(playerLabour));
            }

            event.setCurrentItem(new LabourItem(playerLabour));

            LaboursOfHercules.getInstance().getServer().getPluginManager().callEvent(new CrystalUseEvent(player, cursorItem, playerLabour.getLabour()));
            event.setCancelled(true);
        }

        // Clic en cristal
        if(!meta.getPersistentDataContainer().has(crystalKey)) {
            event.setCancelled(true);
            return;
        }

        switch(event.getAction()) {
            case PLACE_ALL:
            case PLACE_ONE:
            case PLACE_SOME:
            case SWAP_WITH_CURSOR:
            case MOVE_TO_OTHER_INVENTORY:
            case HOTBAR_SWAP:
            case HOTBAR_MOVE_AND_READD:
                event.setCancelled(true);
                break;
        }
    }
}
