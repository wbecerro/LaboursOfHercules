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
        pluginManager.registerEvents(new PlayerInteractListeners(), plugin);
        pluginManager.registerEvents(new PlayerMoveListeners(), plugin);
        pluginManager.registerEvents(new PlayerJoinListeners(), plugin);
        pluginManager.registerEvents(new PlayerQuitListeners(), plugin);
        pluginManager.registerEvents(new MenuListener(), plugin);
        pluginManager.registerEvents(new InventoryCloseListeners(), plugin);

        if(Bukkit.getPluginManager().getPlugin("CrazyCrates") != null) {
            pluginManager.registerEvents(new CrateOpenListeners(), plugin);
        }

        if(Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            pluginManager.registerEvents(new MythicMobDeathListeners(), plugin);
        }

        if(Bukkit.getPluginManager().getPlugin("YggdrasilsBark") != null
                && Bukkit.getPluginManager().getPlugin("AcuaticLostWealth") != null) {
            pluginManager.registerEvents(new PlayerReceiveRewardListeners(), plugin);
        }

        if(Bukkit.getPluginManager().getPlugin("TartarusRiches") != null) {
            pluginManager.registerEvents(new PlayerReceiveGemListeners(), plugin);
        }
    }
}
