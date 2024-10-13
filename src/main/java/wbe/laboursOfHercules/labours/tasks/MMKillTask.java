package wbe.laboursOfHercules.labours.tasks;

import java.util.List;

public class MMKillTask extends Task {

    private List<String> mobs;

    public MMKillTask(String id, int min, int max, String name, String lore, List<String> mobs) {
        super(id, min, max, name, lore);
        this.mobs = mobs;
    }

    public List<String> getMobs() {
        return mobs;
    }

    public void setMobs(List<String> mobs) {
        this.mobs = mobs;
    }
}
