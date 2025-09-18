package wbe.laboursOfHercules.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import wbe.laboursOfHercules.LaboursOfHercules;

public class Scheduler {

    private static LaboursOfHercules plugin;

    public static void startSchedulers() {
        plugin = LaboursOfHercules.getInstance();
        startDataSaveScheduler();
    }

    private static void startDataSaveScheduler() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for(Player player : LaboursOfHercules.activePlayers.keySet()) {
                    Utilities.savePlayerData(player);
                }
            }
        }, 10L, 60 * 5 * 20L);
    }
}
