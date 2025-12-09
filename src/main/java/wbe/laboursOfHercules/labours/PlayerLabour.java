package wbe.laboursOfHercules.labours;

import java.util.List;
import java.util.UUID;

public class PlayerLabour {

    private UUID uuid = UUID.randomUUID();

    private Labour labour;

    private List<PlayerLabourTask> playerTasks;

    public PlayerLabour(Labour labour, List<PlayerLabourTask> playerTasks) {
        this.labour = labour;
        this.playerTasks = playerTasks;
    }

    public PlayerLabour(UUID uuid, Labour labour, List<PlayerLabourTask> playerTasks) {
        this.uuid = uuid;
        this.labour = labour;
        this.playerTasks = playerTasks;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Labour getLabour() {
        return labour;
    }

    public void setLabour(Labour labour) {
        this.labour = labour;
    }

    public List<PlayerLabourTask> getPlayerTasks() {
        return playerTasks;
    }

    public List<PlayerLabourTask> getUnfinishedTasks() {
        return playerTasks.stream().filter(task -> !task.isCompleted()).toList();
    }

    public void setPlayerTasks(List<PlayerLabourTask> playerTasks) {
        this.playerTasks = playerTasks;
    }

    public boolean areTasksCompleted() {
        for(PlayerLabourTask task : playerTasks) {
            if(!task.isCompleted()) {
                return false;
            }
        }

        return true;
    }
}
