package com.koopa.manhuntcore.gui;

import com.koopa.manhuntcore.ManhuntCore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class StatisticsGUI extends BaseGUI {

    public StatisticsGUI(ManhuntCore plugin) {
        super(plugin, "&8Statistics", 36);
    }

    public void openGUI(Player player) {
        Inventory gui = createInventory();

        // Games Played
        gui.setItem(10, createGuiItem(Material.BOOK,
            "&eGames Played",
            "&7Total: &f" + plugin.getConfig().getInt("stats.games-played", 0)));

        // Hunter Wins
        gui.setItem(12, createGuiItem(Material.IRON_SWORD,
            "&cHunter Wins",
            "&7Total: &f" + plugin.getConfig().getInt("stats.hunter-wins", 0)));

        // Speedrunner Wins
        gui.setItem(14, createGuiItem(Material.DIAMOND_BOOTS,
            "&bSpeedrunner Wins",
            "&7Total: &f" + plugin.getConfig().getInt("stats.speedrunner-wins", 0)));

        // Fastest Win
        gui.setItem(16, createGuiItem(Material.CLOCK,
            "&aFastest Win",
            "&7Time: &f" + formatTime(plugin.getConfig().getInt("stats.fastest-win", 0))));

        // Back Button
        gui.setItem(31, createGuiItem(Material.BARRIER,
            "&c&lBack to Main Menu",
            "&7Click to return"));

        player.openInventory(gui);
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() == 31) {
            new SetupGUI(plugin).openSetupGUI(player);
        }
    }

    private String formatTime(int seconds) {
        if (seconds == 0) return "None";
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
} 