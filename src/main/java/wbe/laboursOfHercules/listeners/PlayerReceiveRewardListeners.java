package wbe.laboursOfHercules.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import wbe.acuaticLostWealth.events.PlayerReceiveRewardEvent;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.labours.PlayerLabour;
import wbe.laboursOfHercules.labours.PlayerLabourTask;
import wbe.laboursOfHercules.labours.tasks.FishRarityTask;
import wbe.laboursOfHercules.labours.tasks.Task;
import wbe.laboursOfHercules.labours.tasks.WoodcuttingRarityTask;
import wbe.laboursOfHercules.util.Utilities;

import java.util.*;

public class PlayerReceiveRewardListeners implements Listener {

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void updateProgressOnFishReward(PlayerReceiveRewardEvent event) {
        Player player = event.getPlayer();

        if(event.isCancelled()) {
            return;
        }

        HashMap<UUID, PlayerLabour> playerLabours = LaboursOfHercules.activePlayers.get(player);
        if(playerLabours.isEmpty()) {
            return;
        }

        for(PlayerLabour playerLabour : playerLabours.values()) {
            for(Map.Entry<PlayerLabourTask, Integer> labourTask : playerLabour.getPlayerTasks().entrySet()) {
                Task task = labourTask.getKey().getTask();
                if(labourTask.getKey().isCompleted()) {
                    continue;
                }

                if(!(task instanceof FishRarityTask fishRarityTask)) {
                    continue;
                }

                if(!fishRarityTask.getRarities().contains(event.getRarity().getInternalName())) {
                    continue;
                }

                if(utilities.updateProgress(playerLabour, player, labourTask.getKey(), 1)) {
                    break;
                }

                if(!LaboursOfHercules.config.updateAllLabours) {
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void updateProgressOnWoodcuttingReward(wbe.yggdrasilsBark.events.PlayerReceiveRewardEvent event) {
        Player player = event.getPlayer();

        if(event.isCancelled()) {
            return;
        }

        HashMap<UUID, PlayerLabour> playerLabours = LaboursOfHercules.activePlayers.get(player);
        if(playerLabours.isEmpty()) {
            return;
        }

        Collection<PlayerLabour> labours = new ArrayList<>(playerLabours.values());
        for(PlayerLabour playerLabour : labours) {
            for(Map.Entry<PlayerLabourTask, Integer> labourTask : playerLabour.getPlayerTasks().entrySet()) {
                Task task = labourTask.getKey().getTask();
                if(labourTask.getKey().isCompleted()) {
                    continue;
                }

                if(!(task instanceof WoodcuttingRarityTask woodcuttingRarityTask)) {
                    continue;
                }

                if(!woodcuttingRarityTask.getRarities().contains(event.getRarity().getInternalName())) {
                    continue;
                }

                if(utilities.updateProgress(playerLabour, player, labourTask.getKey(), 1)) {
                    break;
                }

                if(!LaboursOfHercules.config.updateAllLabours) {
                    return;
                }
            }
        }
    }
}
