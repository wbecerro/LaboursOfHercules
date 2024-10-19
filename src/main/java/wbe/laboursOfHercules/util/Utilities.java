package wbe.laboursOfHercules.util;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.events.CompleteLabourEvent;
import wbe.laboursOfHercules.events.CompleteTaskEvent;
import wbe.laboursOfHercules.labours.Labour;
import wbe.laboursOfHercules.labours.tasks.Task;

import java.util.*;

public class Utilities {

    private LaboursOfHercules plugin = LaboursOfHercules.getInstance();

    public int getRandomNumber(int min, int max) {
        max += 1;
        return (int) ((Math.random() * (max - min)) + min);
    }

    public List<ItemStack> getLaboursFromInventory(Player player) {
        PlayerInventory inventory = player.getInventory();
        List<ItemStack> labours = new ArrayList<>();
        NamespacedKey baseKey = new NamespacedKey(plugin, "labour");
        for(ItemStack item : inventory.getContents()) {
            if(item == null) {
                continue;
            }
            ItemMeta meta = item.getItemMeta();
            if(meta.getPersistentDataContainer().has(baseKey)) {
                labours.add(item);
            }
        }

        return labours;
    }

    public void updateProgress(Labour labour, Player player, ItemStack item, Task task) {
        NamespacedKey tasksKey = new NamespacedKey(plugin, "tasks");
        NamespacedKey taskKey = new NamespacedKey(plugin, task.getId());
        NamespacedKey taskMaxKey = new NamespacedKey(plugin, task.getId() + "_max");
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        int line = findLine(task.getLore().split("%amount%"), lore);

        int newAmount = meta.getPersistentDataContainer().get(taskKey, PersistentDataType.INTEGER) + 1;
        int maxAmount = meta.getPersistentDataContainer().get(taskMaxKey, PersistentDataType.INTEGER);
        boolean end = false;
        if(newAmount >= maxAmount) {
            end = true;
        }

        updateTaskLine(player, item, taskKey, taskMaxKey, line, tasksKey, newAmount, maxAmount, task, labour, end);
    }

    public void updateProgress(Labour labour, Player player, ItemStack item, Task task, int amount) {
        NamespacedKey tasksKey = new NamespacedKey(plugin, "tasks");
        NamespacedKey taskKey = new NamespacedKey(plugin, task.getId());
        NamespacedKey taskMaxKey = new NamespacedKey(plugin, task.getId() + "_max");
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        int line = findLine(task.getLore().split("%amount%"), lore);

        int newAmount = meta.getPersistentDataContainer().get(taskKey, PersistentDataType.INTEGER) + amount;
        int maxAmount = meta.getPersistentDataContainer().get(taskMaxKey, PersistentDataType.INTEGER);
        boolean end = false;
        if(newAmount >= maxAmount) {
            end = true;
        }

        updateTaskLine(player, item, taskKey, taskMaxKey, line, tasksKey, newAmount, maxAmount, task, labour, end);
    }

    public boolean updateTaskLine(Player player, ItemStack item, NamespacedKey taskKey, NamespacedKey taskMaxKey, int line,
                               NamespacedKey tasksKey, int amount, int max, Task task, Labour labour, boolean end) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if(end) {
            amount = max;
            meta.getPersistentDataContainer().remove(taskKey);
            meta.getPersistentDataContainer().remove(taskMaxKey);
            String tasks = item.getItemMeta().getPersistentDataContainer().get(tasksKey, PersistentDataType.STRING);

            String[] tasksParts = tasks.split("\\.");
            if(tasksParts.length == 1) {
                player.sendTitle(labour.getCompleteTitle(), "", 10, 70, 20);
                if(!player.hasPermission("laboursofhercules.skip.sounds")) {
                    player.playSound(player, Sound.valueOf(labour.getCompleteSound()), 10, 1);
                }
                for(String broadcast : labour.getCompleteBroadcast()) {
                    Bukkit.broadcastMessage(broadcast.replace("&", "§")
                            .replace("%player%", player.getName()));
                }
                giveRewards(player, labour, item);
                player.getInventory().removeItem(item);
                player.updateInventory();
                return true;
            }

            player.sendTitle(labour.getCompleteTaskTitle(), task.getName(), 10, 70, 20);
            if(!player.hasPermission("laboursofhercules.skip.sounds")) {
                player.playSound(player, Sound.valueOf(labour.getCompleteTaskSound()), 10, 1);
            }

            StringBuilder tasksString = new StringBuilder();
            for(String taskPart : tasksParts) {
                if(!taskPart.equalsIgnoreCase(task.getId())) {
                    tasksString.append(taskPart).append(".");
                }
            }

            meta.getPersistentDataContainer().set(tasksKey, PersistentDataType.STRING,
                    tasksString.toString().toString().substring(0, tasksString.toString().toString().length() - 1));
            plugin.getServer().getPluginManager().callEvent(new CompleteTaskEvent(player, item, labour, task));
        } else {
            meta.getPersistentDataContainer().set(taskKey, PersistentDataType.INTEGER, amount);
        }

        if(!end) {
            lore.set(line, task.getLore()
                    .replace("%amount%", String.valueOf(max))
                    .replace("%completed%", String.valueOf(amount)));
        } else {
            lore.set(line, ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + ChatColor.stripColor(task.getLore())
                    .replace("%amount%", String.valueOf(max))
                    .replace("%completed%", String.valueOf(amount)));
        }
        meta.setLore(lore);

        item.setItemMeta(meta);

        return false;
    }

    public Labour getRandomLabour() {
        Random random = new Random();
        int randomNumber = random.nextInt(LaboursOfHercules.config.laboursTotalWeight);
        int weight = 0;
        Collection<Labour> labours = LaboursOfHercules.config.labours.values();

        for(Labour labour : labours) {
            weight += labour.getWeight();
            if(randomNumber < weight) {
                return labour;
            }
        }

        return (Labour) labours.toArray()[labours.size() - 1];
    }

    public Task getFirstTask(ItemStack item, Labour labour) {
        NamespacedKey tasksKey = new NamespacedKey(plugin, "tasks");
        String tasks = item.getItemMeta().getPersistentDataContainer().get(tasksKey, PersistentDataType.STRING);
        String[] tasksParts = tasks.split("\\.");
        Task task = labour.getTasks().get(tasksParts[0]);
        return task;
    }

    private void giveRewards(Player player, Labour labour, ItemStack item) {
        int rewardsAmount = getRandomNumber(labour.getMinRewards(), labour.getMaxRewards());
        List<String> rewards = new ArrayList<>();
        for(int i=0;i<rewardsAmount;i++) {
            String reward = labour.getRandomReward().getCommand().replace("%player%", player.getName());
            rewards.add(reward);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), reward);
        }
        plugin.getServer().getPluginManager().callEvent(new CompleteLabourEvent(player, item, labour, rewards));
    }
    public int getCraftedAmount(CraftItemEvent event) {
        int amount = event.getRecipe().getResult().getAmount();
        ClickType click = event.getClick();

        switch(click) {
            case NUMBER_KEY:
                if(event.getWhoClicked().getInventory().getItem(event.getHotbarButton()) != null) {
                    amount = 0;
                }
                break;
            case DROP:
            case CONTROL_DROP:
                ItemStack cursor = event.getCursor();
                if(!isAir(cursor)) {
                    amount = 0;
                }
                break;
            case SHIFT_RIGHT:
            case SHIFT_LEFT:
                if(amount == 0) {
                    break;
                }

                int maxCraftable = getMaxCraftAmount(event.getInventory());
                int capacity = getMaxFitInInventory(event.getRecipe().getResult(), event.getView().getBottomInventory());
                if(capacity < maxCraftable) {
                    maxCraftable = ((capacity + amount - 1) / amount) * amount;
                }

                amount = maxCraftable;
                break;
            default:
        }

        if(amount == 0) {
            return 0;
        }

        return amount;
    }

    private int getMaxFitInInventory(ItemStack item, Inventory inventory) {
        ItemStack[] contents = inventory.getContents();
        int result = 0;

        for(ItemStack itemStack : contents) {
            if(itemStack == null) {
                result += item.getMaxStackSize();
            } else if(itemStack.isSimilar(item)) {
                result += Math.max(item.getMaxStackSize() - item.getAmount(), 0);
            }
        }

        return result;
    }

    private int getMaxCraftAmount(CraftingInventory inventory) {
        if(inventory.getResult() == null) {
            return 0;
        }

        int count = inventory.getResult().getAmount();
        int materialCount = Integer.MAX_VALUE;

        for(ItemStack item : inventory.getMatrix()) {
            if(item != null && item.getAmount() < materialCount) {
                materialCount = item.getAmount();
            }
        }

        return count * materialCount;
    }

    private boolean isAir(ItemStack item) {
        return item == null || item.getType().equals(Material.AIR);
    }


    public int findLine(String[] parts, List<String> lore) {
        int size = lore.size();
        for(int i=0;i<size;i++) {
            if(lore.get(i).contains(parts[0].replace("%completed%", "")) &&
                    lore.get(i).contains(parts[1].replace("%completed%", ""))) {
                return i;
            }
        }

        return -1;
    }
}
