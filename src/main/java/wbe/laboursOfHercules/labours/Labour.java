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

    private List<Reward> rewards;

    private HashMap<String, Task> tasks;

    private int totalRewardsWeight = 0;

    public Labour(String id, int weight, int minRewards, int maxRewards, int minTasks, int maxTasks, List<String> completeBroadcast,
                  String completeTitle, String completeSound, String completeTaskTitle, String completeTaskSound, Material material,
                  String name, List<String> lore, boolean glow, List<Reward> rewards, HashMap<String, Task> tasks) {
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
        calculateTotalWeight();
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

    public List<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
        calculateTotalWeight();
    }

    public HashMap<String, Task> getTasks() {
        return tasks;
    }

    public void setTasks(HashMap<String, Task> tasks) {
        this.tasks = tasks;
    }

    public Task getRandomTask() {
        Object[] keys = tasks.keySet().toArray();
        String key = (String) keys[ThreadLocalRandom.current().nextInt(keys.length)];
        return tasks.get(key);
    }

    private void calculateTotalWeight() {
        totalRewardsWeight = 0;
        for(Reward reward : rewards) {
            totalRewardsWeight += reward.getWeight();
        }
    }

    public Reward getRandomReward() {
        Random random = new Random();
        int randomNumber = random.nextInt(totalRewardsWeight);
        int weight = 0;

        for(Reward reward : rewards) {
            weight += reward.getWeight();
            if(randomNumber < weight) {
                return reward;
            }
        }

        return rewards.get(rewards.size() - 1);
    }
}