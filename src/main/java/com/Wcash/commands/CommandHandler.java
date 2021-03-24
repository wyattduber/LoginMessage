package com.Wcash.commands;

import com.Wcash.LoginMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private LoginMessage lm;

    public CommandHandler() {
        lm = LoginMessage.getPlugin();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 0) {
            String helpList =
                    "§f[§aLogin§bMessage§f] §aList of Commands\n" +
                    "§f - §a/lm reload§f - §aReloads the Plugin and it's Config Files\n";
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            switch (args[1]) {
                case "reload":
                    lm.reload();
                    break;
                case "help":
                    break;
            }
        }
        return true;
    }

}
