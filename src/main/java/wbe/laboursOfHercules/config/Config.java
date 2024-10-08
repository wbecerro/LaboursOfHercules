package wbe.laboursOfHercules.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import wbe.laboursOfHercules.labours.Crystal;
import wbe.laboursOfHercules.labours.Labour;
import wbe.laboursOfHercules.labours.tasks.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Config {

    private FileConfiguration config;

    public int labourDropChance;

    public int crystalDropChance;

    public Material randomLabourMaterial;

    public String randomLabourName;

    public List<String> randomLabourLore;

    public boolean randomLabourGlow;

    public Material randomCrystalMaterial;

    public String randomCrystalName;

    public List<String> randomCrystalLore;

    public boolean randomCrystalGlow;

    public HashMap<String, Labour> labours = new HashMap<>();

    public int laboursTotalWeight = 0;

    public HashMap<String, Crystal> crystals = new HashMap<>();

    public Config(FileConfiguration config) {
        this.config = config;

        labourDropChance = config.getInt("Config.labourDropChance");
        crystalDropChance = config.getInt("Config.crystalDropChance");
        randomLabourMaterial = Material.valueOf(config.getString("Items.randomLabour.material"));
        randomLabourName = config.getString("Items.randomLabour.name").replace("&", "§");
        randomLabourLore = config.getStringList("Items.randomLabour.lore");
        randomLabourGlow = config.getBoolean("Items.randomLabour.glow");
        randomCrystalMaterial = Material.valueOf(config.getString("Items.randomCrystal.material"));
        randomCrystalName = config.getString("Items.randomCrystal.name").replace("&", "§");
        randomCrystalLore = config.getStringList("Items.randomCrystal.lore");
        randomCrystalGlow = config.getBoolean("Items.randomCrystal.glow");

        loadLabours();
    }

    private void loadLabours() {
        Set<String> configLabours = config.getConfigurationSection("Labours").getKeys(false);
        for(String configLabour : configLabours) {
            Crystal crystal = loadCrystal(configLabour);
            Labour labour = loadLabour(configLabour);
            labours.put(configLabour, labour);
            crystals.put(configLabour, crystal);
        }
    }

    private Crystal loadCrystal(String labour) {
        String id = labour;
        Material material = Material.valueOf(config.getString("Labours." + labour + ".crystal.material").toUpperCase());
        String name = config.getString("Labours." + labour + ".crystal.name").replace("&", "§");
        List<String> lore = config.getStringList("Labours." + labour + ".crystal.lore");
        boolean glow = config.getBoolean("Labours." + labour + ".crystal.glow");
        return new Crystal(id, material, name, lore, glow);
    }

    private Labour loadLabour(String labour) {
        String id = labour;
        int weight = config.getInt("Labours." + labour + ".weight");
        laboursTotalWeight += weight;
        int minRewards = config.getInt("Labours." + labour + ".minRewards");
        int maxRewards = config.getInt("Labours." + labour + ".maxRewards");
        int minTasks = config.getInt("Labours." + labour + ".minTasks");
        int maxTasks = config.getInt("Labours." + labour + ".maxTasks");
        List<String> completeBroadcast = config.getStringList("Labours." + labour + ".completition.broadcast");
        String completeTitle = config.getString("Labours." + labour + ".completition.title").replace("&", "§");
        String completeSound = config.getString("Labours." + labour + ".completition.playerSound");
        String completeTaskTitle = config.getString("Labours." + labour + ".taskComplete.title").replace("&", "§");
        String completeTaskSound = config.getString("Labours." + labour + ".taskComplete.sound");
        Material material = Material.valueOf(config.getString("Labours." + labour + ".item.material").toUpperCase());
        String name = config.getString("Labours." + labour + ".item.name").replace("&", "§");
        List<String> lore = config.getStringList("Labours." + labour + ".item.lore");
        boolean glow = config.getBoolean("Labours." + labour + ".item.glow");
        List<String> rewards = config.getStringList("Labours." + labour + ".rewards");
        HashMap<String, Task> tasks = loadTasks(labour);
        return new Labour(id, weight, minRewards, maxRewards, minTasks, maxTasks, completeBroadcast, completeTitle,
                completeSound, completeTaskTitle, completeTaskSound, material, name, lore, glow, rewards, tasks);
    }

    private HashMap<String, Task> loadTasks(String labour) {
        Set<String> configTasks = config.getConfigurationSection("Labours." + labour + ".tasks").getKeys(false);
        HashMap<String, Task> tasks = new HashMap<>();
        for(String task : configTasks) {
            tasks.put(task, loadTask(labour, task));
        }

        return tasks;
    }

    private Task loadTask(String labour, String task) {
        String id = task;
        int minAmount = config.getInt("Labours." + labour + ".tasks." + task + ".minAmount");
        int maxAmount = config.getInt("Labours." + labour + ".tasks." + task + ".maxAmount");
        String lore = config.getString("Labours." + labour + ".tasks." + task + ".lore").replace("&", "§");
        String type = config.getString("Labours." + labour + ".tasks." + task + ".type").toUpperCase();

        switch(type) {
            case "KILL":
                List<String> entitiesStrings = config.getStringList("Labours." + labour + ".tasks." + task + ".entities");
                List<EntityType> entities = convertList(entitiesStrings, entity -> EntityType.valueOf(entity.toUpperCase()));
                return new KillTask(id, minAmount, maxAmount, lore, entities);
            case "CRAFT":
                List<String> itemsStrings = config.getStringList("Labours." + labour + ".tasks." + task + ".items");
                List<Material> items = convertList(itemsStrings, item -> Material.valueOf(item.toUpperCase()));
                return new CraftTask(id, minAmount, maxAmount, lore, items);
            case "ENCHANT":
                itemsStrings = config.getStringList("Labours." + labour + ".tasks." + task + ".items");
                items = convertList(itemsStrings, item -> Material.valueOf(item.toUpperCase()));
                return new EnchantTask(id, minAmount, maxAmount, lore, items);
            case "FISH":
                itemsStrings = config.getStringList("Labours." + labour + ".tasks." + task + ".items");
                items = convertList(itemsStrings, item -> Material.valueOf(item.toUpperCase()));
                return new FishTask(id, minAmount, maxAmount, lore, items);
            case "BREAK":
                List<String> blocksStrings = config.getStringList("Labours." + labour + ".tasks." + task + ".blocks");
                List<Material> blocks = convertList(blocksStrings, block -> Material.valueOf(block.toUpperCase()));
                return new BreakTask(id, minAmount, maxAmount, lore, blocks);
            case "SHEAR":
                entitiesStrings = config.getStringList("Labours." + labour + ".tasks." + task + ".entities");
                entities = convertList(entitiesStrings, entity -> EntityType.valueOf(entity.toUpperCase()));
                return new ShearTask(id, minAmount, maxAmount, lore, entities);
            case "TAME":
                entitiesStrings = config.getStringList("Labours." + labour + ".tasks." + task + ".entities");
                entities = convertList(entitiesStrings, entity -> EntityType.valueOf(entity.toUpperCase()));
                return new TameTask(id, minAmount, maxAmount, lore, entities);
            case "MMKILL":
                List<String> mobs = config.getStringList("Labours." + labour + ".tasks." + task + ".mobs");
                return new MMKillTask(id, minAmount, maxAmount, lore, mobs);
            case "FISHINGRARITY":
                List<String> rarities = config.getStringList("Labours." + labour + ".tasks." + task + ".rarities");
                return new FishRarityTask(id, minAmount, maxAmount, lore, rarities);
            case "WOODCUTTINGRARITY":
                rarities = config.getStringList("Labours." + labour + ".tasks." + task + ".rarities");
                return new WoodcuttingRarityTask(id, minAmount, maxAmount, lore, rarities);
            case "CRATE":
                List<String> crates = config.getStringList("Labours." + labour + ".tasks." + task + ".crates");
                return new CrateTask(id, minAmount, maxAmount, lore, crates);
        }

        return new BreakTask("error", 1, 1, "ERROR", new ArrayList<>(Arrays.asList(Material.STONE)));
    }

    private <T, U> List<U> convertList(List<T> list, Function<T, U> function) {
        return list.stream().map(function).collect(Collectors.toList());
    }
}
