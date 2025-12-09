package wbe.laboursOfHercules.labours;

import wbe.laboursOfHercules.labours.tasks.Task;

import java.util.Objects;

public class PlayerLabourTask {

    private Task task;

    private int max;

    private int progress = 0;

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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void complete() {
        completed = true;
    }

    public boolean isEqual(PlayerLabourTask other) {
        return task.getId().equalsIgnoreCase(other.getTask().getId());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlayerLabourTask other = (PlayerLabourTask) o;
        return task.getId().equalsIgnoreCase(other.task.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(task.getId());
    }
}
