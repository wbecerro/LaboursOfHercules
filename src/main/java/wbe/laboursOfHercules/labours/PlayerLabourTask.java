package wbe.laboursOfHercules.labours;

import wbe.laboursOfHercules.labours.tasks.Task;

public class PlayerLabourTask {

    private Task task;

    private int max;

    private boolean completed = false;

    public PlayerLabourTask(Task task, int max) {
        this.task = task;
        this.max = max;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void complete() {
        completed = true;
    }
}
