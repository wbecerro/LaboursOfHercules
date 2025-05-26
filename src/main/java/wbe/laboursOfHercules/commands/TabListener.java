package wbe.laboursOfHercules.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import wbe.laboursOfHercules.LaboursOfHercules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabListener implements TabCompleter {

    private final List<String> subCommands = Arrays.asList("help", "labour", "crystal", "random", "reload");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if(!command.getName().equalsIgnoreCase("LaboursOfHercules")) {
            return completions;
        }

        // Mostrar subcomandos
        if(args.length == 1) {
            StringUtil.copyPartialMatches(args[0], subCommands, completions);
        }

        // Argumento 1
        if(args.length == 2) {
            switch(args[0].toLowerCase()) {
                case "labour":
                    List<String> labours = new ArrayList<>();
                    LaboursOfHercules.config.labours.entrySet().forEach((entry -> {
                        labours.add(entry.getValue().getId());
                    }));
                    completions.addAll(labours);
                    break;
                case "crystal":
                    List<String> crystals = new ArrayList<>();
                    LaboursOfHercules.config.crystals.entrySet().forEach((entry -> {
                        crystals.add(entry.getValue().getId());
                    }));
                    completions.addAll(crystals);
                    break;
                case "random":
                    completions.add("crystal");
                    completions.add("labour");
                    break;
            }
        }

        // Argumento 2
        if(args.length == 3) {
            switch(args[0].toLowerCase()) {
                case "labour":
                case "crystal":
                case "random":
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        completions.add(player.getName());
                    }
                    break;
            }
        }

        // Argumento 3
        if(args.length == 4) {
            switch(args[0].toLowerCase()) {
                case "labour":
                case "crystal":
                case "random":
                    completions.add("<Cantidad>");
                    break;
            }
        }

        return completions;
    }
}
