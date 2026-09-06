package de.leonfree312.kitplugin;

import org.bukkit.plugin.java.JavaPlugin;
import de.leonfree312.kitplugin.commands.KitCommand;
import de.leonfree312.kitplugin.config.ConfigManager;
import de.leonfree312.kitplugin.listeners.KitListener;

public class KitPlugin extends JavaPlugin {

    private static KitPlugin instance;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        
        // Config Manager initialisieren
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        // Commands registrieren
        getCommand("kit").setExecutor(new KitCommand(this));
        
        // Listener registrieren
        getServer().getPluginManager().registerEvents(new KitListener(this), this);
        
        getLogger().info("KitPlugin erfolgreich aktiviert!");
    }

    @Override
    public void onDisable() {
        getLogger().info("KitPlugin deaktiviert!");
    }

    public static KitPlugin getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}