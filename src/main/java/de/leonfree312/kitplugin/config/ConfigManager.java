package de.leonfree312.kitplugin.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class ConfigManager {
    private JavaPlugin plugin;
    private File configFile;
    private FileConfiguration config;
    private Map<String, Kit> kits;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.kits = new HashMap<>();
        this.configFile = new File(plugin.getDataFolder(), "kits.yml");
    }

    public void loadConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!configFile.exists()) {
            createDefaultConfig();
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        loadKits();
    }

    private void createDefaultConfig() {
        try {
            configFile.createNewFile();
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            
            // Standard Kits erstellen
            config.set("kits.starter.name", "&6Starter Kit");
            config.set("kits.starter.cooldown", 86400); // 24 Stunden in Sekunden
            config.set("kits.starter.items.0.type", "DIAMOND_SWORD");
            config.set("kits.starter.items.0.amount", 1);
            config.set("kits.starter.items.1.type", "DIAMOND_PICKAXE");
            config.set("kits.starter.items.1.amount", 1);
            config.set("kits.starter.items.2.type", "COOKED_BEEF");
            config.set("kits.starter.items.2.amount", 32);
            
            config.set("kits.warrior.name", "&c⚔ Krieger Kit");
            config.set("kits.warrior.cooldown", 86400);
            config.set("kits.warrior.items.0.type", "DIAMOND_SWORD");
            config.set("kits.warrior.items.0.amount", 1);
            config.set("kits.warrior.items.1.type", "IRON_HELMET");
            config.set("kits.warrior.items.1.amount", 1);
            config.set("kits.warrior.items.2.type", "IRON_CHESTPLATE");
            config.set("kits.warrior.items.2.amount", 1);
            config.set("kits.warrior.items.3.type", "IRON_LEGGINGS");
            config.set("kits.warrior.items.3.amount", 1);
            config.set("kits.warrior.items.4.type", "IRON_BOOTS");
            config.set("kits.warrior.items.4.amount", 1);
            
            config.save(configFile);
        } catch (Exception e) {
            plugin.getLogger().severe("Fehler beim Erstellen der Config: " + e.getMessage());
        }
    }

    private void loadKits() {
        kits.clear();
        
        if (config.contains("kits")) {
            ConfigurationSection kitsSection = config.getConfigurationSection("kits");
            
            for (String kitName : kitsSection.getKeys(false)) {
                ConfigurationSection kitSection = kitsSection.getConfigurationSection(kitName);
                
                String displayName = kitSection.getString("name", "&6" + kitName);
                long cooldown = kitSection.getLong("cooldown", 86400);
                
                List<ItemStack> items = new ArrayList<>();
                ConfigurationSection itemsSection = kitSection.getConfigurationSection("items");
                
                if (itemsSection != null) {
                    for (String itemKey : itemsSection.getKeys(false)) {
                        ConfigurationSection itemSection = itemsSection.getConfigurationSection(itemKey);
                        String type = itemSection.getString("type", "DIAMOND");
                        int amount = itemSection.getInt("amount", 1);
                        
                        try {
                            Material material = Material.valueOf(type);
                            items.add(new ItemStack(material, amount));
                        } catch (IllegalArgumentException e) {
                            plugin.getLogger().warning("Unbekanntes Item: " + type);
                        }
                    }
                }
                
                kits.put(kitName, new Kit(kitName, displayName, items, cooldown));
            }
        }
        
        plugin.getLogger().info("Geladen: " + kits.size() + " Kits");
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (Exception e) {
            plugin.getLogger().severe("Fehler beim Speichern der Config: " + e.getMessage());
        }
    }

    public Map<String, Kit> getKits() {
        return kits;
    }

    public Kit getKit(String name) {
        return kits.get(name.toLowerCase());
    }

    public void addKit(String name, Kit kit) {
        kits.put(name.toLowerCase(), kit);
        updateKitInConfig(name.toLowerCase(), kit);
    }

    public void removeKit(String name) {
        kits.remove(name.toLowerCase());
        config.set("kits." + name.toLowerCase(), null);
        saveConfig();
    }

    private void updateKitInConfig(String kitName, Kit kit) {
        config.set("kits." + kitName + ".name", kit.getDisplayName());
        config.set("kits." + kitName + ".cooldown", kit.getCooldown());
        
        for (int i = 0; i < kit.getItems().size(); i++) {
            ItemStack item = kit.getItems().get(i);
            config.set("kits." + kitName + ".items." + i + ".type", item.getType().toString());
            config.set("kits." + kitName + ".items." + i + ".amount", item.getAmount());
        }
        
        saveConfig();
    }
}