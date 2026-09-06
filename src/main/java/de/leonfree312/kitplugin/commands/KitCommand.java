package de.leonfree312.kitplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import de.leonfree312.kitplugin.KitPlugin;
import de.leonfree312.kitplugin.config.ConfigManager;
import de.leonfree312.kitplugin.config.Kit;
import de.leonfree312.kitplugin.data.PlayerKitData;

public class KitCommand implements CommandExecutor {
    private KitPlugin plugin;
    private ConfigManager configManager;

    public KitCommand(KitPlugin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Nur Spieler können diesen Befehl ausführen!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "get":
                handleGetKit(player, args);
                break;
            case "list":
                handleListKits(player);
                break;
            case "create":
                handleCreateKit(player, args);
                break;
            case "delete":
                handleDeleteKit(player, args);
                break;
            case "reload":
                handleReload(player);
                break;
            default:
                showHelp(player);
        }

        return true;
    }

    private void handleGetKit(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Verwendung: /kit get <kit-name>");
            return;
        }

        String kitName = args[1].toLowerCase();
        Kit kit = configManager.getKit(kitName);

        if (kit == null) {
            player.sendMessage(ChatColor.RED + "Kit nicht gefunden: " + kitName);
            return;
        }

        if (!player.hasPermission("kit.use")) {
            player.sendMessage(ChatColor.RED + "Du hast keine Berechtigung!");
            return;
        }

        // Cooldown prüfen
        PlayerKitData data = PlayerKitData.getData(player);
        if (!data.canGetKit(kitName, kit.getCooldown())) {
            long remaining = data.getKitCooldown(kitName);
            long hours = remaining / 3600;
            long minutes = (remaining % 3600) / 60;
            player.sendMessage(ChatColor.RED + "Du kannst dieses Kit noch nicht nehmen! " + hours + "h " + minutes + "m");
            return;
        }

        // Items geben
        for (ItemStack item : kit.getItems()) {
            if (player.getInventory().firstEmpty() == -1) {
                player.getWorld().dropItem(player.getLocation(), item);
            } else {
                player.getInventory().addItem(item);
            }
        }

        // Cooldown setzen
        data.setKitCooldown(kitName, System.currentTimeMillis());

        player.sendMessage(ChatColor.GREEN + "Du hast das Kit " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', kit.getDisplayName()) + ChatColor.GREEN + " erhalten!");
    }

    private void handleListKits(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Verfügbare Kits ===");
        for (Kit kit : configManager.getKits().values()) {
            PlayerKitData data = PlayerKitData.getData(player);
            if (data.canGetKit(kit.getId(), kit.getCooldown())) {
                player.sendMessage(ChatColor.GREEN + "✓ " + ChatColor.translateAlternateColorCodes('&', kit.getDisplayName()));
            } else {
                long remaining = data.getKitCooldown(kit.getId());
                long hours = remaining / 3600;
                player.sendMessage(ChatColor.RED + "✗ " + ChatColor.translateAlternateColorCodes('&', kit.getDisplayName()) + ChatColor.GRAY + " (" + hours + "h)");
            }
        }
    }

    private void handleCreateKit(Player player, String[] args) {
        if (!player.hasPermission("kit.admin")) {
            player.sendMessage(ChatColor.RED + "Nur OPs können Kits erstellen!");
            return;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Verwendung: /kit create <kit-name> [anzeigename]");
            return;
        }

        String kitName = args[1].toLowerCase();
        String displayName = args.length > 2 ? args[2] : "&6" + kitName;

        if (configManager.getKit(kitName) != null) {
            player.sendMessage(ChatColor.RED + "Dieses Kit existiert bereits!");
            return;
        }

        Kit newKit = new Kit(kitName, displayName, player.getInventory().getContents().clone(), 86400);
        configManager.addKit(kitName, newKit);

        player.sendMessage(ChatColor.GREEN + "Kit " + kitName + " wurde erstellt!");
    }

    private void handleDeleteKit(Player player, String[] args) {
        if (!player.hasPermission("kit.admin")) {
            player.sendMessage(ChatColor.RED + "Nur OPs können Kits löschen!");
            return;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Verwendung: /kit delete <kit-name>");
            return;
        }

        String kitName = args[1].toLowerCase();
        if (configManager.getKit(kitName) == null) {
            player.sendMessage(ChatColor.RED + "Kit nicht gefunden!");
            return;
        }

        configManager.removeKit(kitName);
        player.sendMessage(ChatColor.GREEN + "Kit " + kitName + " wurde gelöscht!");
    }

    private void handleReload(Player player) {
        if (!player.hasPermission("kit.reload")) {
            player.sendMessage(ChatColor.RED + "Du hast keine Berechtigung!");
            return;
        }

        configManager.loadConfig();
        player.sendMessage(ChatColor.GREEN + "Konfiguration neu geladen!");
    }

    private void showHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Kit Plugin Hilfe ===");
        player.sendMessage(ChatColor.YELLOW + "/kit get <kit-name>" + ChatColor.GRAY + " - Kit abholen");
        player.sendMessage(ChatColor.YELLOW + "/kit list" + ChatColor.GRAY + " - Alle Kits anzeigen");
        if (player.hasPermission("kit.admin")) {
            player.sendMessage(ChatColor.YELLOW + "/kit create <kit-name> [anzeigename]" + ChatColor.GRAY + " - Kit erstellen (aus Inventar)");
            player.sendMessage(ChatColor.YELLOW + "/kit delete <kit-name>" + ChatColor.GRAY + " - Kit löschen");
            player.sendMessage(ChatColor.YELLOW + "/kit reload" + ChatColor.GRAY + " - Config neu laden");
        }
    }
}