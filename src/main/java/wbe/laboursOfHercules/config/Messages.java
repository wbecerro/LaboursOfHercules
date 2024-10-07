package wbe.laboursOfHercules.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Messages {

    private FileConfiguration config;

    public String noPermission;
    public String reload;
    public String notEnoughArgs;
    public String labourArguments;
    public String crystalArguments;
    public String typeNotFound;
    public List<String> help;

    public Messages(FileConfiguration config) {
        this.config = config;

        noPermission = config.getString("Messages.noPermission").replace("&", "§");
        reload = config.getString("Messages.reload").replace("&", "§");
        notEnoughArgs = config.getString("Messages.notEnoughArgs").replace("&", "§");
        labourArguments = config.getString("Messages.labourArguments").replace("&", "§");
        crystalArguments = config.getString("Messages.crystalArguments").replace("&", "§");
        typeNotFound = config.getString("Messages.typeNotFound").replace("&", "§");
        help = config.getStringList("Messages.help");
    }
}
