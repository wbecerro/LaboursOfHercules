package wbe.laboursOfHercules.labours.tasks;

import org.bukkit.Material;

import java.util.List;

public class FishTask extends Task {

    private List<Material> items;

    public FishTask(String id, int min, int max, String name, String lore, List<Material> items) {
        super(id, min, max, name, lore);
        this.items = items;
    }

    public List<Material> getItems() {
        return items;
    }

    public void setItems(List<Material> items) {
        this.items = items;
    }
}
