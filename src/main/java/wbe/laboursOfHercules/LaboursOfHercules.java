package wbe.laboursOfHercules;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import wbe.laboursOfHercules.commands.CommandListener;
import wbe.laboursOfHercules.commands.TabListener;
import wbe.laboursOfHercules.config.Config;
import wbe.laboursOfHercules.config.Messages;
import wbe.laboursOfHercules.labours.PlayerLabour;
import wbe.laboursOfHercules.listeners.EventListeners;
import wbe.laboursOfHercules.util.Scheduler;
import wbe.laboursOfHercules.util.Utilities;

import java.io.File;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public final class LaboursOfHercules extends JavaPlugin {

    private FileConfiguration configuration;

    private CommandListener commandListener;

    private TabListener tabListener;

    private EventListeners eventListeners;

    public static Config config;

    public static Messages messages;

    public static HashMap<Player, HashMap<UUID, PlayerLabour>> activePlayers = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("LaboursOfHercules enabled correctly.");
        reloadConfiguration();

        commandListener = new CommandListener();
        getCommand("laboursofhercules").setExecutor(commandListener);
        tabListener = new TabListener();
        getCommand("laboursofhercules").setTabCompleter(tabListener);
        eventListeners = new EventListeners();
        eventListeners.initializeListeners();
        Scheduler.startSchedulers();
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        reloadConfig();
        for(Player player : activePlayers.keySet()) {
            Utilities.savePlayerData(player);
        }
        getLogger().info("LaboursOfHercules disabled correctly.");
    }

    public static LaboursOfHercules getInstance() {
        return getPlugin(LaboursOfHercules.class);
    }

    public void reloadConfiguration() {
        if(!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        new File(getDataFolder(), "saves").mkdir();
        for(Player player : activePlayers.keySet()) {
            Utilities.savePlayerData(player);
        }

        reloadConfig();
        configuration = getConfig();
        config = new Config(configuration);
        messages = new Messages(configuration);
        for(Player player : Bukkit.getOnlinePlayers()) {
            Utilities.loadPlayerData(player);
        }
    }
}
