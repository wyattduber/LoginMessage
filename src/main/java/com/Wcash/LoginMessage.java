package com.Wcash;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;

public class LoginMessage extends JavaPlugin {

    public boolean checkForUpdates;
    public boolean useFirstTimeMessage;
    public int messageDelayTicks;
    public FileConfiguration config;
    public File customConfigFile;
    public String[] versions = new String[2];
    public String[] messageNames;
    public String firstTimeMessage;
    public HashMap<String, String> messages = new HashMap<>();

    private LoginListener ll;

    public static LoginMessage getPlugin() { return getPlugin(LoginMessage.class); }

    @Override
    public void onEnable() {

        /* Generate / Reload Config */
        try {
            reloadCustomConfig();
            config = getCustomConfig();
            saveCustomConfig();
        } catch (Exception e) {
            error("Error setting up the config! Contact the developer if you cannot fix this issue");
        }

        /* Parse Config for Messages / Booleans */
        parseConfig();

        /* Register Join Listener for Updates */
        setCheckForUpdates();

        /* Register Commands */
        try {
            Objects.requireNonNull(this.getCommand("lm")).setExecutor(new CommandHandler());
        } catch (NullPointerException e) {
            error(e.getMessage());
        }

    }

    @Override
    public void onDisable() {
        log("Plugin Disabled Successfully!");
    }

    public void reload() {
        /* Un-Register Listeners */
        PlayerJoinEvent.getHandlerList().unregister(ll);

        /* Reload the Config */
        try {
            reloadCustomConfig();
            config = getCustomConfig();
            saveCustomConfig();
        } catch (Exception e) {
            error("Error setting up the config! Contact the developer if you cannot fix this issue");
        }

        /* Parse the Reloaded Config */
        parseConfig();

        /* Check for Updates */
        setCheckForUpdates();
    }

    public void parseConfig() {

        /* Get Boolean to Check for Updates */
        try {
            checkForUpdates = getConfigBool("check-for-updates");
        } catch (NullPointerException e) {
            error("Cannot Find \"check-for-update\" Boolean in Config! Make sure it's there and reload the plugin.");
            return;
        }

        try {
            messageDelayTicks = getConfigInt("message-delay");
        } catch (NullPointerException e) {
            error("Cannot Find \"message-delay\" Boolean in Config! Make sure it's there and reload the plugin.");
            return;
        }

        try {
            StringBuilder message = new StringBuilder();
            useFirstTimeMessage = getConfigBool("enable-first-time-message");
            String[] tempAdd = new String[config.getStringList("first-time-message").size()];
            tempAdd = config.getStringList("first-time-message").toArray(tempAdd);
            for (int i = 0; i < tempAdd.length; i++) {
                tempAdd[i] = tempAdd[i].replaceAll("&", "§");
                if (i == 0) {
                    message.append(tempAdd[i]);
                } else {
                    message.append("\n").append(tempAdd[i]);
                }
            }
            firstTimeMessage = message.toString();
        } catch (NullPointerException e) {
            error(e.getMessage());
            error("Cannot Find \"enable-first-time-message\" Boolean in Config! Make sure it's there and reload the plugin.");
            return;
        }

        /* Parse Message Names and Messages by Permission Node */
        try {
            messageNames = new String[config.getStringList("messages").size()]; // Initialize the Array as an Template
            messageNames = config.getStringList("messages").toArray(messageNames); // Fill the array using itself as a template

            for (String messageName : messageNames) {
                StringBuilder message = new StringBuilder();
                String[] tempAdd = new String[config.getStringList(messageName).size()];
                tempAdd = config.getStringList(messageName).toArray(tempAdd);
                for (int i = 0; i < tempAdd.length; i++) {
                    tempAdd[i] = tempAdd[i].replaceAll("&", "§");
                    if (i == 0) {
                        message.append(tempAdd[i]);
                    } else {
                        message.append("\n").append(tempAdd[i]);
                    }

                }
                messages.put("lm.message." + messageName, message.toString());
            }

        } catch (NullPointerException e) {
            error(e.getMessage());
            error("Error with the Message Section in the Config! Make sure it's set properly and reload the plugin.");
            return;
        }
        log("Config Successfully Loaded");
    }

    public void setCheckForUpdates() {
        try {
            new UpdateChecker(this, 90530).getVersion(version -> {
                if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                    versions[0] = version;
                    versions[1] = this.getDescription().getVersion();
                }
            });
            ll = new LoginListener(versions);
            getServer().getPluginManager().registerEvents(ll, this);
        } catch (Exception e) {
            error(e.getMessage());
            return;
        }
        log("Update Checker Successfully Loaded");
    }

    public String getConfigString(String entryName) {
        return config.getString(entryName);
    }

    public boolean getConfigBool(String entryName) {
        return config.getBoolean(entryName);
    }

    public int getConfigInt(String entryName) {
        return config.getInt(entryName);
    }

    public void reloadCustomConfig() {
        saveDefaultConfig();
        config = YamlConfiguration.loadConfiguration(customConfigFile);
        config.options().copyDefaults(true);

        // Look for defaults in the jar
        Reader defConfigStream = null;
        try {
            defConfigStream = new InputStreamReader(Objects.requireNonNull(this.getResource("config.yml")), StandardCharsets.UTF_8);
        } catch (Exception e) {
            error(e.getMessage());
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            config.setDefaults(defConfig);
        }
    }

    public FileConfiguration getCustomConfig() {
        if (config == null) {
            reloadCustomConfig();
        }
        return config;
    }

    public void saveCustomConfig() {
        if (config == null || customConfigFile == null) {
            return;
        }
        try {
            getCustomConfig().save(customConfigFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }

    @Override
    public void saveDefaultConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(getDataFolder(), "config.yml");
        }
        if (!customConfigFile.exists()) {
            this.saveResource("config.yml", false);
        }
    }

    public void log(String message) {
        this.getLogger().log(Level.INFO, message);
    }

    public void warn(String message) {
        this.getLogger().log(Level.WARNING, message);
    }

    public void error(String message) {
        this.getLogger().log(Level.SEVERE, message);
    }

}
