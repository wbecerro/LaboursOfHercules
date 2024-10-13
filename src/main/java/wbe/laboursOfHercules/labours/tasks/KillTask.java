package wbe.laboursOfHercules.labours.tasks;

import org.bukkit.entity.EntityType;

import java.util.List;

public class KillTask extends Task {

    private List<EntityType> entities;

    public KillTask(String id, int min, int max, String name, String lore, List<EntityType> entities) {
        super(id, min, max, name, lore);
        this.entities = entities;
    }

    public List<EntityType> getEntities() {
        return entities;
    }

    public void setEntities(List<EntityType> entities) {
        this.entities = entities;
    }
}
