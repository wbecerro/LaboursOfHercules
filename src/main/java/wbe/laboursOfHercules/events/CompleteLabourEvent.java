package wbe.laboursOfHercules.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import wbe.laboursOfHercules.labours.Labour;

import java.util.List;

public class CompleteLabourEvent extends Event implements Cancellable {

    private Player player;

    private ItemStack labourItem;

    private List<String> rewards;

    private Labour type;

    private boolean isCancelled;

    private static final HandlerList handlers = new HandlerList();

    public CompleteLabourEvent(Player player, ItemStack labourItem, Labour type, List<String> rewards) {
        this.player = player;
        this.labourItem = labourItem;
        this.rewards = rewards;
        this.type = type;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getLabourItem() {
        return labourItem;
    }

    public List<String> getRewards() {
        return rewards;
    }

    public Labour getType() {
        return type;
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
