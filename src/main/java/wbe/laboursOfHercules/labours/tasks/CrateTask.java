package wbe.laboursOfHercules.labours.tasks;

import java.util.List;

public class CrateTask extends Task {

    private List<String> crates;

    public CrateTask(String id, int min, int max, String lore, List<String> crates) {
        super(id, min, max, lore);
        this.crates = crates;
    }

    public List<String> getCrates() {
        return crates;
    }

    public void setCrates(List<String> crate) {
        this.crates = crates;
    }
}
