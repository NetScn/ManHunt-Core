package com.koopa.manhuntcore.gui;

import com.koopa.manhuntcore.ManhuntCore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class TimerSettingsGUI extends BaseGUI {

    public TimerSettingsGUI(ManhuntCore plugin) {
        super(plugin, "&8Timer Settings", 36);
    }

    public void openGUI(Player player) {
        Inventory gui = createInventory();

        // Start Countdown
        gui.setItem(11, createGuiItem(Material.CLOCK,
            "&eStart Countdown",
            "&7Current: &f" + plugin.getConfig().getInt("timer.start-countdown", 10) + "s",
            "&7Click to modify"));

        // Game Duration
        gui.setItem(13, createGuiItem(Material.CLOCK,
            "&6Game Duration",
            "&7Current: &f" + formatTime(plugin.getConfig().getInt("timer.game-duration", 0)),
            "&7Click to modify",
            "&70 = No time limit"));

        // Head Start
        gui.setItem(15, createGuiItem(Material.FEATHER,
            "&bHead Start",
            "&7Current: &f" + plugin.getConfig().getInt("timer.head-start", 30) + "s",
            "&7Click to modify"));

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
            case 11 -> cycleStartCountdown(player);
            case 13 -> cycleGameDuration(player);
            case 15 -> cycleHeadStart(player);
            case 31 -> new SetupGUI(plugin).openSetupGUI(player);
        }
    }

    private void cycleStartCountdown(Player player) {
        int current = plugin.getConfig().getInt("timer.start-countdown", 10);
        int next = (current + 5) % 31; // 5-30 seconds
        if (next < 5) next = 5;
        plugin.getConfig().set("timer.start-countdown", next);
        plugin.saveConfig();
        player.sendMessage("§8[§d§lManhunt§8] §7Start countdown set to: §f" + next + "s");
        openGUI(player);
    }

    private void cycleGameDuration(Player player) {
        int current = plugin.getConfig().getInt("timer.game-duration", 0);
        int next = switch (current) {
            case 0 -> 1800; // 30 minutes
            case 1800 -> 3600; // 1 hour
            case 3600 -> 7200; // 2 hours
            case 7200 -> 0; // No limit
            default -> 1800;
        };
        plugin.getConfig().set("timer.game-duration", next);
        plugin.saveConfig();
        player.sendMessage("§8[§d§lManhunt§8] §7Game duration set to: §f" + formatTime(next));
        openGUI(player);
    }

    private void cycleHeadStart(Player player) {
        int current = plugin.getConfig().getInt("timer.head-start", 30);
        int next = (current + 15) % 121; // 15-120 seconds
        if (next < 15) next = 15;
        plugin.getConfig().set("timer.head-start", next);
        plugin.saveConfig();
        player.sendMessage("§8[§d§lManhunt§8] §7Head start set to: §f" + next + "s");
        openGUI(player);
    }

    private String formatTime(int seconds) {
        if (seconds == 0) return "No limit";
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        return hours > 0 ? hours + "h " + minutes + "m" : minutes + "m";
    }
} 