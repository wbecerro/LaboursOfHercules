package wbe.laboursOfHercules.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import wbe.laboursOfHercules.labours.Labour;

public class CrystalUseEvent extends Event implements Cancellable {

    private Player player;

    private ItemStack labourItem;

    private ItemStack crystalItem;

    private Labour type;

    private boolean isCancelled;

    private static final HandlerList handlers = new HandlerList();

    public CrystalUseEvent(Player player, ItemStack labourItem, ItemStack crystalItem, Labour type) {
        this.player = player;
        this.labourItem = labourItem;
        this.crystalItem = crystalItem;
        this.type = type;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getLabourItem() {
        return labourItem;
    }

    public ItemStack getCrystalItem() {
        return crystalItem;
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
