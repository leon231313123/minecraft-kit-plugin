package de.leonfree312.kitplugin.data;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class PlayerKitData {
    private static Map<String, PlayerKitData> playerData = new HashMap<>();
    private Map<String, Long> kitCooldowns; // Kit Name -> Timestamp
    private String playerName;

    public PlayerKitData(String playerName) {
        this.playerName = playerName;
        this.kitCooldowns = new HashMap<>();
    }

    public static PlayerKitData getData(Player player) {
        String uuid = player.getUniqueId().toString();
        return playerData.computeIfAbsent(uuid, k -> new PlayerKitData(player.getName()));
    }

    public boolean canGetKit(String kitName, long cooldown) {
        Long lastUsed = kitCooldowns.get(kitName);
        if (lastUsed == null) {
            return true;
        }

        long currentTime = System.currentTimeMillis();
        long cooldownMs = cooldown * 1000;
        return (currentTime - lastUsed) >= cooldownMs;
    }

    public void setKitCooldown(String kitName, long timestamp) {
        kitCooldowns.put(kitName, timestamp);
    }

    public long getKitCooldown(String kitName) {
        Long lastUsed = kitCooldowns.get(kitName);
        if (lastUsed == null) {
            return 0;
        }

        long currentTime = System.currentTimeMillis();
        long elapsed = (currentTime - lastUsed) / 1000; // in Sekunden
        return elapsed;
    }
}