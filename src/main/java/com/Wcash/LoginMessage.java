package com.Wcash;

import com.Wcash.commands.CommandHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Level;

public class LoginMessage extends JavaPlugin implements Listener {

    public boolean checkForUpdates;
    public FileConfiguration config;
    public File customConfigFile;
    public String[] versions;

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
        if (parseConfig()) {

        }

        /* Register Join Listener for Updates */
        if (checkForUpdates) {
            setCheckForUpdates();
        }

        /* Register Commands */
        try {
            Objects.requireNonNull(this.getCommand("lm")).setExecutor(new CommandHandler());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        /* Check for Updates and send message to player with permission to see updates */
        if (checkForUpdates && (event.getPlayer().hasPermission("mcdb.update") || event.getPlayer().isOp())) {
            event.getPlayer().sendMessage("§f[§aLogin§bMessage§f] Version §c" + versions[0] + " §favailable! You have §c" + versions[1] + ".");
            log("Version " + versions[0] + " available! You have " + versions[1] + ".");
        }
    }


    public void reload() {
        /* Un-Register Listeners

        parseConfig();
    }

    public boolean parseConfig() {

        /* Get Boolean to Check for Updates */
        try {
            checkForUpdates = getConfigBool("check-for-updates");
        } catch (NullPointerException e) {
            error("Cannot Find \"check-for-update\" Boolean in Config!");
        }

        //TODO Implement a Function to interpret the different MOTD's and then assign them a permission node

        return true;
    }

    public void setCheckForUpdates() {
        try {
            new UpdateChecker(this, 00000).getVersion(version -> {
                if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                    versions[0] = version;
                    versions[1] = this.getDescription().getVersion();
                }
                getServer().getPluginManager().registerEvents(this, this);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getConfigString(String entryName) {
        return config.getString(entryName);
    }

    public boolean getConfigBool(String entryName) {
        return config.getBoolean(entryName);
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
            e.printStackTrace();
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
