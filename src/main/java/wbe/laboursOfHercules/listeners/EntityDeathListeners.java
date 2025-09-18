package wbe.laboursOfHercules.listeners;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.items.RandomCrystalItem;
import wbe.laboursOfHercules.items.RandomLabourItem;
import wbe.laboursOfHercules.labours.PlayerLabour;
import wbe.laboursOfHercules.labours.PlayerLabourTask;
import wbe.laboursOfHercules.labours.tasks.KillTask;
import wbe.laboursOfHercules.labours.tasks.Task;
import wbe.laboursOfHercules.util.Utilities;

import java.util.*;

public class EntityDeathListeners implements Listener {

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void updateProgressOnEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getDamageSource().getCausingEntity();
        if(!(entity instanceof Player player)) {
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

                if(!(task instanceof KillTask killTask)) {
                    continue;
                }

                if(!killTask.getEntities().contains(event.getEntity().getType())) {
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
    public void addSpecialDropsOnDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if(!(entity instanceof Monster)) {
            return;
        }

        if(entity.getType().equals(EntityType.ENDERMAN)) {
            return;
        }

        LivingEntity killer = event.getEntity().getKiller();
        if(killer == null) {
            return;
        }

        ItemStack weapon = killer.getEquipment().getItemInMainHand();
        int lootingLevel = 0;
        if(!weapon.getType().equals(Material.AIR)) {
            lootingLevel = weapon.getEnchantments().getOrDefault(Enchantment.LOOTING, 0);
        }

        double labourChance = LaboursOfHercules.config.labourDropChance + LaboursOfHercules.config.lootingExtraChance * lootingLevel;
        double crystalChance = LaboursOfHercules.config.crystalDropChance + LaboursOfHercules.config.lootingExtraChance * lootingLevel;

        Random random = new Random();
        if(random.nextDouble(100) < labourChance) {
            event.getDrops().add(new RandomLabourItem());
        }

        if(random.nextDouble(100) < crystalChance) {
            event.getDrops().add(new RandomCrystalItem());
        }
    }
}
