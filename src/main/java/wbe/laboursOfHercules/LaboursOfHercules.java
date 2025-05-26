package wbe.laboursOfHercules;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import wbe.laboursOfHercules.commands.CommandListener;
import wbe.laboursOfHercules.commands.TabListener;
import wbe.laboursOfHercules.config.Config;
import wbe.laboursOfHercules.config.Messages;
import wbe.laboursOfHercules.listeners.EventListeners;
import wbe.laboursOfHercules.util.Utilities;

import java.io.File;

public final class LaboursOfHercules extends JavaPlugin {

    private FileConfiguration configuration;

    private CommandListener commandListener;

    private TabListener tabListener;

    private EventListeners eventListeners;

    public static Config config;

    public static Messages messages;

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
    }

    @Override
    public void onDisable() {
        reloadConfig();
        getLogger().info("LaboursOfHercules disabled correctly.");
    }

    public static LaboursOfHercules getInstance() {
        return getPlugin(LaboursOfHercules.class);
    }

    public void reloadConfiguration() {
        if(!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        reloadConfig();
        configuration = getConfig();
        config = new Config(configuration);
        messages = new Messages(configuration);
    }
}
