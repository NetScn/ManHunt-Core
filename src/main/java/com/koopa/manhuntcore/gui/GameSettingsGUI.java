package com.koopa.manhuntcore.gui;

import com.koopa.manhuntcore.ManhuntCore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GameSettingsGUI extends BaseGUI {

    public GameSettingsGUI(ManhuntCore plugin) {
        super(plugin, "&8Game Settings", 36);
    }

    public void openGUI(Player player) {
        Inventory gui = createInventory();

        // PvP Settings
        gui.setItem(10, createToggleItem(Material.DIAMOND_SWORD,
            "&cPvP Settings",
            plugin.getConfig().getBoolean("pvp-enabled", true),
            "&7Enable/Disable PvP",
            "&7Click to toggle"));

        // Grace Period
        gui.setItem(12, createGuiItem(Material.CLOCK,
            "&eGrace Period",
            "&7Current: &f" + plugin.getConfig().getInt("grace-period", 30) + "s",
            "&7Click to modify"));

        // Win Conditions
        gui.setItem(14, createGuiItem(Material.DRAGON_HEAD,
            "&6Win Conditions",
            "&7Configure victory conditions",
            "&7Click to modify"));

        // Difficulty
        gui.setItem(16, createGuiItem(Material.ZOMBIE_HEAD,
            "&4Difficulty",
            "&7Current: &f" + plugin.getConfig().getString("difficulty", "NORMAL"),
            "&7Click to cycle"));

        // Back Button
        gui.setItem(31, createGuiItem(Material.BARRIER,
            "&c&lBack to Main Menu",
            "&7Click to return"));

        player.openInventory(gui);
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case 10 -> togglePvP(player);
            case 12 -> modifyGracePeriod(player);
            case 14 -> openWinConditions(player);
            case 16 -> cycleDifficulty(player);
            case 31 -> new SetupGUI(plugin).openSetupGUI(player);
        }
    }

    private void togglePvP(Player player) {
        boolean enabled = !plugin.getConfig().getBoolean("pvp-enabled", true);
        plugin.getConfig().set("pvp-enabled", enabled);
        plugin.saveConfig();
        player.sendMessage("§8[§d§lManhunt§8] §7PvP: " + 
            (enabled ? "§a§lENABLED" : "§c§lDISABLED"));
        openGUI(player);
    }

    private void modifyGracePeriod(Player player) {
        int current = plugin.getConfig().getInt("grace-period", 30);
        int next = (current + 30) % 181; // Cycles between 0-180 seconds
        if (next == 0) next = 30;
        plugin.getConfig().set("grace-period", next);
        plugin.saveConfig();
        player.sendMessage("§8[§d§lManhunt§8] §7Grace period set to: §f" + next + "s");
        openGUI(player);
    }

    private void openWinConditions(Player player) {
        new WinConditionsGUI(plugin).openGUI(player);
    }

    private void cycleDifficulty(Player player) {
        String current = plugin.getConfig().getString("difficulty", "NORMAL");
        String next = switch (current) {
            case "PEACEFUL" -> "EASY";
            case "EASY" -> "NORMAL";
            case "NORMAL" -> "HARD";
            case "HARD" -> "PEACEFUL";
            default -> "NORMAL";
        };
        plugin.getConfig().set("difficulty", next);
        plugin.saveConfig();
        player.sendMessage("§8[§d§lManhunt§8] §7Difficulty set to: §f" + next);
        openGUI(player);
    }
} 