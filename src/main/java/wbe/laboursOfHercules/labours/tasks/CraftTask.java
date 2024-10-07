package wbe.laboursOfHercules.labours.tasks;

import org.bukkit.Material;

import java.util.List;

public class CraftTask extends Task {

    private List<Material> items;

    public CraftTask(String id, int min, int max, String lore, List<Material> items) {
        super(id, min, max, lore);
        this.items = items;
    }

    public List<Material> getItems() {
        return items;
    }

    public void setItems(List<Material> items) {
        this.items = items;
    }
}
