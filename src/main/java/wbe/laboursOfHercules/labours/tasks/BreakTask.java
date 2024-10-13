package wbe.laboursOfHercules.labours.tasks;

import org.bukkit.Material;

import java.util.List;

public class BreakTask extends Task {

    private List<Material> blocks;

    public BreakTask(String id, int min, int max, String name, String lore, List<Material> blocks) {
        super(id, min, max, name, lore);
        this.blocks = blocks;
    }

    public List<Material> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Material> blocks) {
        this.blocks = blocks;
    }
}
