package wbe.laboursOfHercules.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.labours.PlayerLabour;
import wbe.laboursOfHercules.labours.PlayerLabourTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LabourItem extends ItemStack {

    private LaboursOfHercules plugin = LaboursOfHercules.getInstance();

    private PlayerLabour labour;

    public LabourItem(PlayerLabour labour) {
        super(labour.getLabour().getMaterial());
        this.labour = labour;

        ItemMeta meta;
        if(hasItemMeta()) {
            meta = getItemMeta();
        } else {
            meta = Bukkit.getItemFactory().getItemMeta(labour.getLabour().getMaterial());
        }

        meta.setDisplayName(labour.getLabour().getName());

        ArrayList<String> lore = new ArrayList<>();
        for(String line : labour.getLabour().getLore()) {
            lore.add(line.replace("&", "§"));
        }
        meta.setLore(lore);

        NamespacedKey baseKey = new NamespacedKey(plugin, "labour");
        meta.getPersistentDataContainer().set(baseKey, PersistentDataType.STRING, labour.getUuid().toString());

        if(labour.getLabour().isGlow()) {
            meta.addEnchant(Enchantment.INFINITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        setItemMeta(meta);

        setTasks();
    }

    private void setTasks() {
        Set<PlayerLabourTask> tasks = labour.getPlayerTasks().keySet();
        ItemMeta meta = getItemMeta();
        List<String> lore = meta.getLore();

        for(PlayerLabourTask task : tasks) {
            if(task.isCompleted()) {
                lore.add(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + ChatColor.stripColor(task.getTask().getLore()
                        .replace("%amount%", String.valueOf(task.getMax()))
                        .replace("%completed%", String.valueOf(task.getMax()))));
            } else {
                lore.add(task.getTask().getLore()
                        .replace("%amount%", String.valueOf(task.getMax()))
                        .replace("%completed%", String.valueOf(labour.getPlayerTasks().get(task))));
            }
        }
        meta.setLore(lore);
        setItemMeta(meta);
    }
}
