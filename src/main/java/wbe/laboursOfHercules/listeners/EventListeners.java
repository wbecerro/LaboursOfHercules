package wbe.laboursOfHercules.listeners;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import wbe.laboursOfHercules.LaboursOfHercules;

public class EventListeners {

    private LaboursOfHercules plugin = LaboursOfHercules.getInstance();

    public void initializeListeners() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        pluginManager.registerEvents(new BlockBreakListeners(), plugin);
        pluginManager.registerEvents(new CraftItemListeners(), plugin);
        pluginManager.registerEvents(new EnchantItemListeners(), plugin);
        pluginManager.registerEvents(new EntityDeathListeners(), plugin);
        pluginManager.registerEvents(new EntityTameListeners(), plugin);
        pluginManager.registerEvents(new PlayerFishListeners(), plugin);
        pluginManager.registerEvents(new PlayerShearEntityListeners(), plugin);
        pluginManager.registerEvents(new InventoryClickListeners(), plugin);
        pluginManager.registerEvents(new PlayerInteractListeners(), plugin);

        if(Bukkit.getPluginManager().getPlugin("CrazyCrates") != null) {
            pluginManager.registerEvents(new CrateOpenListeners(), plugin);
        }

        if(Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            pluginManager.registerEvents(new MythicMobDeathListeners(), plugin);
        }

        if(Bukkit.getPluginManager().getPlugin("YggdrasilsBark") != null
                && Bukkit.getPluginManager().getPlugin("AquaticLostWealth") != null) {
            pluginManager.registerEvents(new PlayerReceiveRewardListeners(), plugin);
        }
    }
}
