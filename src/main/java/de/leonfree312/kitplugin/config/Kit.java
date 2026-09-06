package de.leonfree312.kitplugin.config;

import org.bukkit.inventory.ItemStack;
import java.util.List;

public class Kit {
    private String id;
    private String displayName;
    private List<ItemStack> items;
    private long cooldown; // in Sekunden

    public Kit(String id, String displayName, List<ItemStack> items, long cooldown) {
        this.id = id;
        this.displayName = displayName;
        this.items = items;
        this.cooldown = cooldown;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }
}