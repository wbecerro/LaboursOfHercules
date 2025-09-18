package wbe.laboursOfHercules.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import wbe.laboursOfHercules.labours.Labour;
import wbe.laboursOfHercules.labours.tasks.Task;

public class CompleteTaskEvent extends Event implements Cancellable {

    private Player player;

    private Task task;

    private Labour type;

    private boolean isCancelled;

    private static final HandlerList handlers = new HandlerList();

    public CompleteTaskEvent(Player player, Labour type, Task task) {
        this.player = player;
        this.task = task;
        this.type = type;
    }

    public Player getPlayer() {
        return player;
    }

    public Task getTask() {
        return task;
    }

    public Labour getType() {
        return type;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
}
