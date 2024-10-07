package wbe.laboursOfHercules.labours.tasks;

import org.bukkit.entity.EntityType;

import java.util.List;

public class ShearTask extends Task {

    private List<EntityType> entities;

    public ShearTask(String id, int min, int max, String lore, List<EntityType> entities) {
        super(id, min, max, lore);
        this.entities = entities;
    }

    public List<EntityType> getEntities() {
        return entities;
    }

    public void setEntities(List<EntityType> entities) {
        this.entities = entities;
    }
}
