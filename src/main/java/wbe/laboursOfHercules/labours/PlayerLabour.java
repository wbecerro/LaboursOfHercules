package wbe.laboursOfHercules.labours;

import java.util.HashMap;
import java.util.UUID;

public class PlayerLabour {

    private UUID uuid = UUID.randomUUID();

    private Labour labour;

    private HashMap<PlayerLabourTask, Integer> playerTasks;

    public PlayerLabour(Labour labour, HashMap<PlayerLabourTask, Integer> playerTasks) {
        this.labour = labour;
        this.playerTasks = playerTasks;
    }

    public PlayerLabour(UUID uuid, Labour labour, HashMap<PlayerLabourTask, Integer> playerTasks) {
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

    public HashMap<PlayerLabourTask, Integer> getPlayerTasks() {
        return playerTasks;
    }

    public void setPlayerTasks(HashMap<PlayerLabourTask, Integer> playerTasks) {
        this.playerTasks = playerTasks;
    }

    public boolean areTasksCompleted() {
        for(PlayerLabourTask task : playerTasks.keySet()) {
            if(!task.isCompleted()) {
                return false;
            }
        }

        return true;
    }
}
