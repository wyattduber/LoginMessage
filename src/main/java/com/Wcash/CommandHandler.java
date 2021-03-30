package com.Wcash;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class CommandHandler implements CommandExecutor {

    private final LoginMessage lm;

    public CommandHandler() {
        lm = LoginMessage.getPlugin();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        String helpList =
                "§f[§aLogin§bMessage§f] §aList of Commands\n" +
                "§f - §a/lm reload§f - §aReloads the Plugin and it's Config Files\n" +
                "§f - §a/lm help§f - §aShows this list of commands";

        if (args.length == 0) {
            sender.sendMessage(helpList); // If no arguments are sent, then send the help list
            return true;
        }

        switch (args[0]) {
            case "reload":
                lm.reload();
                if (sender instanceof ConsoleCommandSender) {
                    lm.log("Plugin and Configuration Reloaded!");
                } else {
                    sender.sendMessage("§f[§aLogin§bMessage§f] Plugin and Configuration Reloaded!");
                }
                break;
            case "help":
                sender.sendMessage(helpList);
                break;
            default:
                if (sender instanceof ConsoleCommandSender) {
                    lm.log("§c" + "Command Not Found!");
                } else {
                    sender.sendMessage("§f[§aLogin§bMessage§f]§c Command Not Found!");
                }
                sender.sendMessage(helpList);
                break;
            }

        return true;
    }

}
