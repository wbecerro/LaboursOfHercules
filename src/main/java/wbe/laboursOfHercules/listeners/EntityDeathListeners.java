package wbe.laboursOfHercules.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.items.RandomCrystalItem;
import wbe.laboursOfHercules.items.RandomLabourItem;
import wbe.laboursOfHercules.labours.Labour;
import wbe.laboursOfHercules.labours.tasks.KillTask;
import wbe.laboursOfHercules.labours.tasks.Task;
import wbe.laboursOfHercules.util.Utilities;

import java.util.List;
import java.util.Random;

public class EntityDeathListeners implements Listener {

    private LaboursOfHercules plugin = LaboursOfHercules.getInstance();

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void updateProgressOnEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getDamageSource().getCausingEntity();
        if(!(entity instanceof Player)) {
            return;
        }
        Player player = (Player) entity;

        List<ItemStack> labours = utilities.getLaboursFromInventory(player);
        if(labours.isEmpty()) {
            return;
        }

        NamespacedKey baseKey = new NamespacedKey(plugin, "labour");
        for(ItemStack item : labours) {
            String tier = item.getItemMeta().getPersistentDataContainer().get(baseKey, PersistentDataType.STRING);
            Labour labour = LaboursOfHercules.config.labours.get(tier);
            NamespacedKey tasksKey = new NamespacedKey(plugin, "tasks");
            String tasks = item.getItemMeta().getPersistentDataContainer().get(tasksKey, PersistentDataType.STRING);
            String[] tasksParts = tasks.split("\\.");
            for(String taskId : tasksParts) {
                Task task = labour.getTasks().get(taskId);
                if(!(task instanceof KillTask)) {
                    continue;
                }

                for(EntityType entityType : ((KillTask) task).getEntities()) {
                    if(!entityType.equals(event.getEntity().getType())) {
                        continue;
                    }

                    utilities.updateProgress(labour, player, item, task);
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
