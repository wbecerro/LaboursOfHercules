package wbe.laboursOfHercules.util;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.events.CompleteLabourEvent;
import wbe.laboursOfHercules.events.CompleteTaskEvent;
import wbe.laboursOfHercules.labours.Labour;
import wbe.laboursOfHercules.labours.PlayerLabour;
import wbe.laboursOfHercules.labours.PlayerLabourTask;
import wbe.laboursOfHercules.labours.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Utilities {

    private LaboursOfHercules plugin = LaboursOfHercules.getInstance();

    public int getRandomNumber(int min, int max) {
        max += 1;
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static void savePlayerData(Player player) {
        try {
            File playerFile = new File(
                    LaboursOfHercules.getInstance().getDataFolder(), "saves/" + player.getUniqueId() + ".yml"
            );
            boolean fileCreated = playerFile.createNewFile();
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
            HashMap<UUID, PlayerLabour> labours = LaboursOfHercules.activePlayers.get(player);

            playerConfig.set("labours", null);

            labours.forEach((uuid, labour) -> {
                String labourId = labour.getLabour().getId();
                // Guardar tareas en la labor
                playerConfig.set("labours." + uuid.toString() + ".id", labourId);
                for(Map.Entry<PlayerLabourTask, Integer> task : labour.getPlayerTasks().entrySet()) {
                    PlayerLabourTask labourTask = task.getKey();
                    playerConfig.set("labours." + uuid.toString() + ".tasks." + labourTask.getTask().getId() + ".progress", task.getValue());
                    playerConfig.set("labours." + uuid.toString() + ".tasks." + labourTask.getTask().getId() + ".max", labourTask.getMax());
                    playerConfig.set("labours." + uuid.toString() + ".tasks." + labourTask.getTask().getId() + ".completed", labourTask.isCompleted());
                }
            });

            playerConfig.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while saving the " + player.getName() + " data.");
        }
    }

    public static void loadPlayerData(Player player) {
        File playerFile = new File(
                LaboursOfHercules.getInstance().getDataFolder(), "saves/" + player.getUniqueId() + ".yml"
        );
        HashMap<UUID ,PlayerLabour> labours = new HashMap<>();
        if(!playerFile.exists()) {
            LaboursOfHercules.activePlayers.put(player, labours);
            return;
        }

        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
        if(playerConfig.getKeys(false).isEmpty()) {
            LaboursOfHercules.activePlayers.put(player, labours);
            return;
        }

        Set<String> labourUUIDs = playerConfig.getConfigurationSection("labours").getKeys(false);
        for(String uuid : labourUUIDs) {
            Labour labour = LaboursOfHercules.config.labours.get(playerConfig.getString("labours." + uuid + ".id"));
            if(labour == null) {
                continue;
            }

            Set<String> configTasks = playerConfig.getConfigurationSection("labours." + uuid + ".tasks").getKeys(false);
            HashMap<PlayerLabourTask, Integer> tasks = new HashMap<>();
            for(String configTask : configTasks) {
                Task task = labour.getTasks().get(configTask);
                int max = playerConfig.getInt("labours." + uuid + ".tasks." + configTask + ".max");
                int progress = playerConfig.getInt("labours." + uuid + ".tasks." + configTask + ".progress");
                boolean completed = playerConfig.getBoolean("labours." + uuid + ".tasks." + configTask + ".completed");
                PlayerLabourTask labourTask = new PlayerLabourTask(task, max);
                if(completed) {
                    labourTask.complete();
                }
                tasks.put(labourTask, progress);
            }
            PlayerLabour playerLabour = new PlayerLabour(UUID.fromString(uuid), labour, tasks);
            labours.put(UUID.fromString(uuid), playerLabour);
        }

        LaboursOfHercules.activePlayers.put(player, labours);
    }

    public PlayerLabour createPlayerLabour(Labour labour) {
        HashMap<PlayerLabourTask, Integer> tasks = new HashMap<>();
        for(Task task : getTasks(labour)) {
            tasks.put(new PlayerLabourTask(task, task.getAmount()), 0);
        }

        return new PlayerLabour(labour, tasks);
    }

    public List<Task> getTasks(Labour labour) {
        int taskAmount = getRandomNumber(labour.getMinTasks(), labour.getMaxTasks());
        List<Task> tasks = new ArrayList<>();
        for(int i=0;i<taskAmount;i++) {
            Task task = getRandomTask(tasks, labour);
            if(task == null) {
                continue;
            }
            tasks.add(task);
        }

        return tasks;
    }

    private Task getRandomTask(List<Task> tasks, Labour labour) {
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

    public boolean updateProgress(PlayerLabour labour, Player player, PlayerLabourTask task, int amount) {
        int newProgress = labour.getPlayerTasks().get(task) + amount;
        labour.getPlayerTasks().put(task, newProgress);
        // Se completa la tarea
        if(newProgress >= task.getMax()) {
            task.complete();
            player.sendTitle(labour.getLabour().getCompleteTaskTitle(), task.getTask().getName(), 10, 70, 20);
            if(!player.hasPermission("laboursofhercules.skip.sounds")) {
                player.playSound(player, Sound.valueOf(labour.getLabour().getCompleteTaskSound()), 10, 1);
            }

            plugin.getServer().getPluginManager().callEvent(new CompleteTaskEvent(player, labour.getLabour(), task.getTask()));
        }

        // Se completa la labor
        if(labour.areTasksCompleted()) {
            player.sendTitle(labour.getLabour().getCompleteTitle(), "", 10, 70, 20);
            if(!player.hasPermission("laboursofhercules.skip.sounds")) {
                player.playSound(player, Sound.valueOf(labour.getLabour().getCompleteSound()), 10, 1);
            }

            for(String broadcast : labour.getLabour().getCompleteBroadcast()) {
                Bukkit.broadcastMessage(broadcast.replace("&", "§")
                        .replace("%player%", player.getName()));
            }

            giveRewards(player, labour.getLabour());

            HashMap<UUID, PlayerLabour> labours = LaboursOfHercules.activePlayers.get(player);
            labours.remove(labour.getUuid());
            LaboursOfHercules.activePlayers.put(player, labours);
            return true;
        }

        return false;
    }

    public boolean applyCrystal(PlayerLabour labour, Player player) {
        boolean completed = false;
        for(PlayerLabourTask task : labour.getPlayerTasks().keySet()) {
            if(!task.isCompleted()) {
                task.complete();
                break;
            }
        }

        if(labour.areTasksCompleted()) {
            player.sendTitle(labour.getLabour().getCompleteTitle(), "", 10, 70, 20);
            if(!player.hasPermission("laboursofhercules.skip.sounds")) {
                player.playSound(player, Sound.valueOf(labour.getLabour().getCompleteSound()), 10, 1);
            }

            for(String broadcast : labour.getLabour().getCompleteBroadcast()) {
                Bukkit.broadcastMessage(broadcast.replace("&", "§")
                        .replace("%player%", player.getName()));
            }

            giveRewards(player, labour.getLabour());

            HashMap<UUID, PlayerLabour> labours = LaboursOfHercules.activePlayers.get(player);
            labours.remove(labour.getUuid());
            LaboursOfHercules.activePlayers.put(player, labours);
            completed = true;
        }

        return completed;
    }

    public boolean removeLabour(Player player, UUID uuid) {
        HashMap<UUID, PlayerLabour> labours = LaboursOfHercules.activePlayers.get(player);
        boolean removed = labours.remove(uuid) != null;
        LaboursOfHercules.activePlayers.put(player, labours);

        return removed;
    }

    public List<ItemStack> getCrystalsStuck(Inventory inventory) {
        NamespacedKey crystalKey = new NamespacedKey(LaboursOfHercules.getInstance(), "crystal");
        List<ItemStack> crystals = new ArrayList<>();
        for(ItemStack item : inventory.getContents()) {
            if(isAir(item)) {
                continue;
            }

            ItemMeta meta = item.getItemMeta();
            if(meta == null) {
                continue;
            }

            if(meta.getPersistentDataContainer().has(crystalKey)) {
                crystals.add(item);
            }
        }

        return crystals;
    }

    public void addItemToInventory(Player player, ItemStack item) {
        if(player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), item);
        } else {
            player.getInventory().addItem(item);
        }
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

    private void giveRewards(Player player, Labour labour) {
        int rewardsAmount = getRandomNumber(labour.getMinRewards(), labour.getMaxRewards());
        List<String> rewards = new ArrayList<>();
        for(int i=0;i<rewardsAmount;i++) {
            String reward = labour.getRandomReward().getCommand().replace("%player%", player.getName());
            rewards.add(reward);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), reward);
        }
        plugin.getServer().getPluginManager().callEvent(new CompleteLabourEvent(player, labour, rewards));
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
}
