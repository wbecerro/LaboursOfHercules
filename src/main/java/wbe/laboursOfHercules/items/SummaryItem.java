package wbe.laboursOfHercules.items;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.labours.PlayerLabourTask;

import java.util.ArrayList;
import java.util.List;

public class SummaryItem extends ItemStack {

    private LaboursOfHercules plugin = LaboursOfHercules.getInstance();

    private int maxPage;

    public SummaryItem(List<PlayerLabourTask> tasks, int page) {
        super(LaboursOfHercules.config.summaryItemMaterial);
        maxPage = (int) Math.ceil((double) tasks.size() / LaboursOfHercules.config.summaryMaxTasksPerPage);

        if(page > maxPage) {
            page = 1;
        } else if(page < 1) {
            page = maxPage;
        }

        ItemMeta meta;
        if(hasItemMeta()) {
            meta = getItemMeta();
        } else {
            meta = Bukkit.getItemFactory().getItemMeta(LaboursOfHercules.config.summaryItemMaterial);
        }

        meta.setDisplayName(LaboursOfHercules.config.summaryItemName
                .replace("%current%", String.valueOf(page))
                .replace("%max%", String.valueOf(maxPage)));

        ArrayList<String> lore = new ArrayList<>();
        for(String line : LaboursOfHercules.config.summaryItemLore) {
            lore.add(line.replace("&", "§")
                    .replace("%length%", String.valueOf(tasks.size())));
        }
        meta.setLore(lore);

        NamespacedKey baseKey = new NamespacedKey(plugin, "summaryPage");
        meta.getPersistentDataContainer().set(baseKey, PersistentDataType.INTEGER, page);

        if(LaboursOfHercules.config.summaryItemGlow) {
            meta.addEnchant(Enchantment.INFINITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        setItemMeta(meta);

        setTasks(tasks, page);
    }

    private void setTasks(List<PlayerLabourTask> tasks, int page) {
        ItemMeta meta = getItemMeta();
        List<String> lore = meta.getLore();

        int start = LaboursOfHercules.config.summaryMaxTasksPerPage * (page - 1);
        int max = Math.min(LaboursOfHercules.config.summaryMaxTasksPerPage * page, tasks.size());
        for (int i = start;i<max;i++) {
            PlayerLabourTask task = tasks.get(i);
            lore.add(task.getTask().getLore()
                    .replace("%amount%", String.valueOf(task.getMax()))
                    .replace("%completed%", String.valueOf(task.getProgress())));
        }

        lore.add("");
        lore.add(LaboursOfHercules.config.summaryItemNextPage);
        lore.add(LaboursOfHercules.config.summaryItemPreviousPage);

        meta.setLore(lore);
        setItemMeta(meta);
    }
}
