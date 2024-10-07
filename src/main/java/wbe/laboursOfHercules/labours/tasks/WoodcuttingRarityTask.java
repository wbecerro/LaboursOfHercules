package wbe.laboursOfHercules.labours.tasks;

import java.util.List;

public class WoodcuttingRarityTask extends Task {

    private List<String> rarities;

    public WoodcuttingRarityTask(String id, int min, int max, String lore, List<String> rarities) {
        super(id, min, max, lore);
        this.rarities = rarities;
    }

    public List<String> getRarities() {
        return rarities;
    }

    public void setRarities(List<String> rarities) {
        this.rarities = rarities;
    }
}
