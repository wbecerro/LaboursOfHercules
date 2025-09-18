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
    public String randomArguments;
    public String typeNotFound;
    public String crystalApplied;
    public String maxLaboursReached;
    public String noTalismansFound;
    public String pageNotFound;
    public String menuNextPage;
    public String menuPreviousPage;
    public String labourAdded;
    public List<String> help;

    public Messages(FileConfiguration config) {
        this.config = config;

        noPermission = config.getString("Messages.noPermission").replace("&", "§");
        reload = config.getString("Messages.reload").replace("&", "§");
        notEnoughArgs = config.getString("Messages.notEnoughArgs").replace("&", "§");
        labourArguments = config.getString("Messages.labourArguments").replace("&", "§");
        crystalArguments = config.getString("Messages.crystalArguments").replace("&", "§");
        randomArguments = config.getString("Messages.randomArguments").replace("&", "§");
        typeNotFound = config.getString("Messages.typeNotFound").replace("&", "§");
        crystalApplied = config.getString("Messages.crystalApplied").replace("&", "§");
        maxLaboursReached = config.getString("Messages.maxLaboursReached").replace("&", "§");
        noTalismansFound = config.getString("Messages.noTalismansFound").replace("&", "§");
        pageNotFound = config.getString("Messages.pageNotFound").replace("&", "§");
        menuNextPage = config.getString("Messages.menuNextPage").replace("&", "§");
        menuPreviousPage = config.getString("Messages.menuPreviousPage").replace("&", "§");
        labourAdded = config.getString("Messages.labourAdded").replace("&", "§");
        help = config.getStringList("Messages.help");
    }
}
