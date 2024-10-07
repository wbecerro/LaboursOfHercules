package wbe.laboursOfHercules.labours.tasks;

import wbe.laboursOfHercules.util.Utilities;

public class Task {

    private String id;

    private int min;

    private int max;

    private String lore;

    private Utilities utilities = new Utilities();

    public Task(String id, int min, int max, String lore) {
        this.id = id;
        this.min = min;
        this.max = max;
        this.lore = lore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public int getAmount() {
        return utilities.getRandomNumber(min, max);
    }
}
