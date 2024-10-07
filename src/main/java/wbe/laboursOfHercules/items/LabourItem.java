package wbe.laboursOfHercules.items;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.labours.Labour;
import wbe.laboursOfHercules.labours.tasks.Task;
import wbe.laboursOfHercules.util.Utilities;

import java.util.ArrayList;
import java.util.List;

public class LabourItem extends ItemStack {

    private LaboursOfHercules plugin = LaboursOfHercules.getInstance();

    private Labour labour;

    private Utilities utilities = new Utilities();

    public LabourItem(Labour labour) {
        super(labour.getMaterial());
        this.labour = labour;

        ItemMeta meta;
        if(hasItemMeta()) {
            meta = getItemMeta();
        } else {
            meta = Bukkit.getItemFactory().getItemMeta(labour.getMaterial());
        }

        meta.setDisplayName(labour.getName());

        ArrayList<String> lore = new ArrayList<>();
        for(String line : labour.getLore()) {
            lore.add(line.replace("&", "§"));
        }
        meta.setLore(lore);

        NamespacedKey baseKey = new NamespacedKey(plugin, "labour");
        meta.getPersistentDataContainer().set(baseKey, PersistentDataType.STRING, labour.getId());

        if(labour.isGlow()) {
            meta.addEnchant(Enchantment.INFINITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        setItemMeta(meta);

        setTasks();
    }

    private void setTasks() {
        List<Task> tasks = getTasks();
        ItemMeta meta = getItemMeta();
        List<String> lore = meta.getLore();
        StringBuilder tasksString = new StringBuilder();

        for(Task task : tasks) {
            int amount = task.getAmount();
            tasksString.append(task.getId()).append(".");
            lore.add(task.getLore()
                    .replace("%amount%", String.valueOf(amount))
                    .replace("%completed%", String.valueOf(0)));
            NamespacedKey taskKey = new NamespacedKey(plugin, task.getId());
            NamespacedKey taskMaxKey = new NamespacedKey(plugin, task.getId() + "_max");
            meta.getPersistentDataContainer().set(taskKey, PersistentDataType.INTEGER, 0);
            meta.getPersistentDataContainer().set(taskMaxKey, PersistentDataType.INTEGER, amount);
        }
        meta.setLore(lore);

        NamespacedKey tasksKey = new NamespacedKey(plugin, "tasks");
        meta.getPersistentDataContainer().set(tasksKey, PersistentDataType.STRING,
                tasksString.toString().toString().substring(0, tasksString.toString().toString().length() - 1));

        setItemMeta(meta);
    }

    private List<Task> getTasks() {
        int taskAmount = utilities.getRandomNumber(labour.getMinTasks(), labour.getMaxTasks());
        List<Task> tasks = new ArrayList<>();
        for(int i=0;i<taskAmount;i++) {
            Task task = getRandomTask(tasks);
            if(task == null) {
                continue;
            }
            tasks.add(task);
        }

        return tasks;
    }

    private Task getRandomTask(List<Task> tasks) {
        boolean found = true;

        int maxTries = 5;
        for(int i=0;i<maxTries;i++) {
            Task randomTask = labour.getRandomTask();
            for(Task task : tasks) {
                if(randomTask.getId().equalsIgnoreCase(task.getId())) {
                    found = false;
                    break;
                }
            }

            if(found) {
                return randomTask;
            }
        }

        return null;
    }
}
