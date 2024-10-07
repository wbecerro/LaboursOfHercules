package wbe.laboursOfHercules.labours;

import org.bukkit.Material;
import wbe.laboursOfHercules.labours.tasks.Task;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Labour {

    private String id;

    private int weight;

    private int minRewards;

    private int maxRewards;

    private int minTasks;

    private int maxTasks;

    private List<String> completeBroadcast;

    private String completeTitle;

    private String completeSound;

    private String completeTaskTitle;

    private String completeTaskSound;

    private Material material;

    private String name;

    private List<String> lore;

    private boolean glow;

    private List<String> rewards;

    private HashMap<String, Task> tasks;

    private int rewardsSize = 0;

    public Labour(String id, int weight, int minRewards, int maxRewards, int minTasks, int maxTasks, List<String> completeBroadcast,
                  String completeTitle, String completeSound, String completeTaskTitle, String completeTaskSound, Material material,
                  String name, List<String> lore, boolean glow, List<String> rewards, HashMap<String, Task> tasks) {
        this.id = id;
        this.weight = weight;
        this.minRewards = minRewards;
        this.maxRewards = maxRewards;
        this.minTasks = minTasks;
        this.maxTasks = maxTasks;
        this.completeBroadcast = completeBroadcast;
        this.completeTitle = completeTitle;
        this.completeSound = completeSound;
        this.completeTaskTitle = completeTaskTitle;
        this.completeTaskSound = completeTaskSound;
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.glow = glow;
        this.tasks = tasks;
        this.rewards = rewards;
        rewardsSize = rewards.size();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getMinRewards() {
        return minRewards;
    }

    public void setMinRewards(int minRewards) {
        this.minRewards = minRewards;
    }

    public int getMaxRewards() {
        return maxRewards;
    }

    public void setMaxRewards(int maxRewards) {
        this.maxRewards = maxRewards;
    }

    public int getMinTasks() {
        return minTasks;
    }

    public void setMinTasks(int minTasks) {
        this.minTasks = minTasks;
    }

    public int getMaxTasks() {
        return maxTasks;
    }

    public void setMaxTasks(int maxTasks) {
        this.maxTasks = maxTasks;
    }

    public List<String> getCompleteBroadcast() {
        return completeBroadcast;
    }

    public void setCompleteBroadcast(List<String> completeBroadcast) {
        this.completeBroadcast = completeBroadcast;
    }

    public String getCompleteTitle() {
        return completeTitle;
    }

    public void setCompleteTitle(String completeTitle) {
        this.completeTitle = completeTitle;
    }

    public String getCompleteSound() {
        return completeSound;
    }

    public void setCompleteSound(String completeSound) {
        this.completeSound = completeSound;
    }

    public String getCompleteTaskTitle() {
        return completeTaskTitle;
    }

    public void setCompleteTaskTitle(String completeTaskTitle) {
        this.completeTaskTitle = completeTaskTitle;
    }

    public String getCompleteTaskSound() {
        return completeTaskSound;
    }

    public void setCompleteTaskSound(String completeTaskSound) {
        this.completeTaskSound = completeTaskSound;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public boolean isGlow() {
        return glow;
    }

    public void setGlow(boolean glow) {
        this.glow = glow;
    }

    public List<String> getRewards() {
        return rewards;
    }

    public void setRewards(List<String> rewards) {
        this.rewards = rewards;
        this.rewardsSize = rewards.size();
    }

    public HashMap<String, Task> getTasks() {
        return tasks;
    }

    public void setTasks(HashMap<String, Task> tasks) {
        this.tasks = tasks;
    }

    public Task getRandomTask() {
        Random random = new Random();
        Object[] keys = tasks.keySet().toArray();
        String key = (String) keys[ThreadLocalRandom.current().nextInt(keys.length)];
        return tasks.get(key);
    }

    public String getRandomReward() {
        Random random = new Random();
        return rewards.get(random.nextInt(rewardsSize));
    }
}