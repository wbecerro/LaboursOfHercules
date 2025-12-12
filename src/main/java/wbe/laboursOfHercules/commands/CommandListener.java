package wbe.laboursOfHercules.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.items.CrystalItem;
import wbe.laboursOfHercules.items.RandomCrystalItem;
import wbe.laboursOfHercules.items.RandomLabourItem;
import wbe.laboursOfHercules.labours.Crystal;
import wbe.laboursOfHercules.labours.Labour;
import wbe.laboursOfHercules.labours.PlayerLabour;
import wbe.laboursOfHercules.listeners.MenuListener;
import wbe.laboursOfHercules.util.Utilities;

import java.util.HashMap;
import java.util.UUID;

public class CommandListener implements CommandExecutor {

    private LaboursOfHercules plugin = LaboursOfHercules.getInstance();

    private Utilities utilities = new Utilities();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("laboursofhercules")) {
            Player player = null;
            if(sender instanceof Player) {
                player = (Player) sender;
            }

            if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
                if(!sender.hasPermission("laboursofhercules.command.help")) {
                    sender.sendMessage(LaboursOfHercules.messages.noPermission);
                    return false;
                }

                for(String line : LaboursOfHercules.messages.help) {
                    sender.sendMessage(line.replace("&", "§"));
                }
            } else if(args[0].equalsIgnoreCase("labour")) {
                if(!sender.hasPermission("laboursofhercules.command.labour")) {
                    sender.sendMessage(LaboursOfHercules.messages.noPermission);
                    return false;
                }

                if(args.length < 3) {
                    sender.sendMessage(LaboursOfHercules.messages.notEnoughArgs);
                    sender.sendMessage(LaboursOfHercules.messages.labourArguments);
                    return false;
                }

                String type = args[1];
                player = Bukkit.getServer().getPlayer(args[2]);
                int amount = 1;
                if(args.length > 3) {
                    amount = Integer.valueOf(args[3]);
                }
                Labour labour = LaboursOfHercules.config.labours.get(type);
                if(labour == null) {
                    sender.sendMessage(LaboursOfHercules.messages.typeNotFound.replace("%type%", type));
                    return false;
                }

                if(LaboursOfHercules.activePlayers.get(player).size() >= LaboursOfHercules.config.maxLaboursPerPlayer) {
                    sender.sendMessage(LaboursOfHercules.messages.maxLaboursReached);
                    return false;
                }

                for(int i=0;i<amount;i++) {
                    PlayerLabour playerLabour = utilities.createPlayerLabour(labour);
                    HashMap<UUID, PlayerLabour> playerLabours = LaboursOfHercules.activePlayers.get(player);
                    playerLabours.put(playerLabour.getUuid(), playerLabour);
                    LaboursOfHercules.activePlayers.put(player, playerLabours);
                }
            } else if(args[0].equalsIgnoreCase("crystal")) {
                if(!sender.hasPermission("laboursofhercules.command.crystal")) {
                    sender.sendMessage(LaboursOfHercules.messages.noPermission);
                    return false;
                }

                if(args.length < 3) {
                    sender.sendMessage(LaboursOfHercules.messages.notEnoughArgs);
                    sender.sendMessage(LaboursOfHercules.messages.crystalArguments);
                    return false;
                }

                String type = args[1];
                player = Bukkit.getServer().getPlayer(args[2]);
                int amount = 1;
                if(args.length > 3) {
                    amount = Integer.valueOf(args[3]);
                }
                Labour labour = LaboursOfHercules.config.labours.get(type);
                if(labour == null) {
                    sender.sendMessage(LaboursOfHercules.messages.typeNotFound.replace("%type%", type));
                    return false;
                }
                Crystal crystal = LaboursOfHercules.config.crystals.get(labour.getId());
                CrystalItem crystalItem = new CrystalItem(crystal);
                crystalItem.setAmount(amount);
                if(player.getInventory().firstEmpty() == -1) {
                    player.getWorld().dropItem(player.getLocation(), crystalItem);
                } else {
                    player.getInventory().addItem(crystalItem);
                }
            } else if(args[0].equalsIgnoreCase("random")) {
                if(!sender.hasPermission("laboursofhercules.command.random")) {
                    sender.sendMessage(LaboursOfHercules.messages.noPermission);
                    return false;
                }

                if(args.length < 3) {
                    sender.sendMessage(LaboursOfHercules.messages.notEnoughArgs);
                    sender.sendMessage(LaboursOfHercules.messages.randomArguments);
                    return false;
                }

                String object = args[1];
                player = Bukkit.getServer().getPlayer(args[2]);
                int amount = 1;
                if(args.length > 3) {
                    amount = Integer.valueOf(args[3]);
                }
                if(object.equalsIgnoreCase("crystal")) {
                    RandomCrystalItem crystal = new RandomCrystalItem();
                    if(player.getInventory().firstEmpty() == -1) {
                        player.getWorld().dropItem(player.getLocation(), crystal);
                    } else {
                        player.getInventory().addItem(crystal);
                    }
                } else {
                    for(int i=0;i<amount;i++) {
                        RandomLabourItem labour = new RandomLabourItem();
                        if(player.getInventory().firstEmpty() == -1) {
                            player.getWorld().dropItem(player.getLocation(), labour);
                        } else {
                            player.getInventory().addItem(labour);
                        }
                    }
                }
            } else if(args[0].equalsIgnoreCase("list")) {
                if(!sender.hasPermission("laboursofhercules.command.list")) {
                    sender.sendMessage(LaboursOfHercules.messages.noPermission);
                    return false;
                }

                try {
                    MenuListener.openMenu(player, 1);
                } catch(Exception e) {
                    sender.sendMessage(e.getMessage());
                }
            } else if(args[0].equalsIgnoreCase("reload")) {
                if(!sender.hasPermission("laboursofhercules.command.reload")) {
                    sender.sendMessage(LaboursOfHercules.messages.noPermission);
                    return false;
                }

                plugin.reloadConfiguration();
                sender.sendMessage(LaboursOfHercules.messages.reload);
            }
        }
        return true;
    }
}
