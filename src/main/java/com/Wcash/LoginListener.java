package com.Wcash;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

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

        if (lm.messageDelayTicks > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    /* Send the custom first message if player is new */
                    if (lm.useFirstTimeMessage && !event.getPlayer().hasPlayedBefore()) {
                        sendFirstMessage(event.getPlayer());
                        return;
                    }

                    /* Sends the Messages to Players who have the Permission node to receive them */
                    sendCustomMessages(event.getPlayer());
                }
            }.runTaskLater(lm, lm.messageDelayTicks);
        } else {
            /* Send the custom first message if player is new */
            if (lm.useFirstTimeMessage && !event.getPlayer().hasPlayedBefore()) {
                sendFirstMessage(event.getPlayer());
                return;
            }

            /* Sends the Messages to Players who have the Permission node to receive them */
            sendCustomMessages(event.getPlayer());
        }

    }

    private void sendFirstMessage(Player player) {
        String message = lm.firstTimeMessage;
        message = message.replaceAll("%PLAYER%", player.getDisplayName());
        message = message.replaceAll("%ONLINE%", Integer.toString(lm.getServer().getOnlinePlayers().size()));
        message = message.replaceAll("%MAXPLAYERS%",  Integer.toString(lm.getServer().getMaxPlayers()));
        player.getPlayer().sendMessage(message);
    }

    private void sendCustomMessages(Player player) {
        for (String messageName : messageNames) {
            if (player.hasPermission("lm.message." + messageName)) {
                String message = messages.get("lm.message." + messageName);
                message = message.replaceAll("%PLAYER%", player.getDisplayName());
                message = message.replaceAll("%ONLINE%", Integer.toString(lm.getServer().getOnlinePlayers().size()));
                message = message.replaceAll("%MAXPLAYERS%",  Integer.toString(lm.getServer().getMaxPlayers()));

                player.sendMessage(message);
            }
        }
    }

}


