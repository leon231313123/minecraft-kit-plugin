package de.leonfree312.kitplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import de.leonfree312.kitplugin.KitPlugin;
import de.leonfree312.kitplugin.data.PlayerKitData;

public class KitListener implements Listener {
    private KitPlugin plugin;

    public KitListener(KitPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Spielerdaten laden
        PlayerKitData.getData(event.getPlayer());
    }
}