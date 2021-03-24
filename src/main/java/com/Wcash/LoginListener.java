package com.Wcash;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public class LoginListener implements Listener {

    private final LoginMessage lm;
    private final boolean checkForUpdates;
    private final String[] versions;
    private final String[] messageNames;
    private final HashMap<String, String> messages;

    public LoginListener(String[] versions) {
        lm = LoginMessage.getPlugin();
        checkForUpdates = lm.checkForUpdates;
        messages = lm.messages;
        messageNames = lm.messageNames;
        this.versions = versions;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        /* Check for Updates and send message to player with permission to see updates */
        if (checkForUpdates && (event.getPlayer().hasPermission("lm.update") || event.getPlayer().isOp())) {
            event.getPlayer().sendMessage("§f[§aLogin§bMessage§f] Version §c" + versions[0] + " §favailable! You have §c" + versions[1] + "§f.");
            lm.log("Version " + versions[0] + " available! You have " + versions[1] + ".");
        }

        /* Sends the Messages to Players who have the Permission node to receive them */
        for (String messageName : messageNames) {
            if (event.getPlayer().hasPermission("lm.message." + messageName)) {
                String message = messages.get("lm.message." + messageName);
                message = message.replaceAll("%USER%", event.getPlayer().getDisplayName());
                message = message.replaceAll("%ONLINE%", Integer.toString(lm.getServer().getOnlinePlayers().size()));
                message = message.replaceAll("%MAXPLAYERS%",  Integer.toString(lm.getServer().getMaxPlayers()));

                event.getPlayer().sendMessage(message);
            }
        }

    }

}
