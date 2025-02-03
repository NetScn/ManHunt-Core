package com.koopa.manhuntcore.gui;

import com.koopa.manhuntcore.ManhuntCore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class WinConditionsGUI extends BaseGUI {

    public WinConditionsGUI(ManhuntCore plugin) {
        super(plugin, "&8Win Conditions", 36);
    }

    public void openGUI(Player player) {
        Inventory gui = createInventory();

        // Dragon Kill
        gui.setItem(11, createToggleItem(Material.DRAGON_HEAD,
            "&5Dragon Kill",
            plugin.getConfig().getBoolean("win-conditions.dragon-kill", true),
            "&7Win by killing the dragon",
            "&7Click to toggle"));

        // Time Limit
        gui.setItem(13, createToggleItem(Material.CLOCK,
            "&eTime Limit",
            plugin.getConfig().getBoolean("win-conditions.time-limit", false),
            "&7Win by surviving time limit",
            "&7Click to toggle"));

        // All Hunters Dead
        gui.setItem(15, createToggleItem(Material.SKELETON_SKULL,
            "&cHunter Elimination",
            plugin.getConfig().getBoolean("win-conditions.hunter-elimination", false),
            "&7Win by killing all hunters",
            "&7Click to toggle"));

        // Back Button
        gui.setItem(31, createGuiItem(Material.BARRIER,
            "&c&lBack",
            "&7Return to game settings"));

        player.openInventory(gui);
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case 11 -> toggleDragonKill(player);
            case 13 -> toggleTimeLimit(player);
            case 15 -> toggleHunterElimination(player);
            case 31 -> new GameSettingsGUI(plugin).openGUI(player);
        }
    }

    private void toggleDragonKill(Player player) {
        boolean enabled = !plugin.getConfig().getBoolean("win-conditions.dragon-kill", true);
        plugin.getConfig().set("win-conditions.dragon-kill", enabled);
        plugin.saveConfig();
        player.sendMessage("§8[§d§lManhunt§8] §7Dragon kill win condition: " + 
            (enabled ? "§a§lENABLED" : "§c§lDISABLED"));
        openGUI(player);
    }

    private void toggleTimeLimit(Player player) {
        boolean enabled = !plugin.getConfig().getBoolean("win-conditions.time-limit", false);
        plugin.getConfig().set("win-conditions.time-limit", enabled);
        plugin.saveConfig();
        player.sendMessage("§8[§d§lManhunt§8] §7Time limit win condition: " + 
            (enabled ? "§a§lENABLED" : "§c§lDISABLED"));
        openGUI(player);
    }

    private void toggleHunterElimination(Player player) {
        boolean enabled = !plugin.getConfig().getBoolean("win-conditions.hunter-elimination", false);
        plugin.getConfig().set("win-conditions.hunter-elimination", enabled);
        plugin.saveConfig();
        player.sendMessage("§8[§d§lManhunt§8] §7Hunter elimination win condition: " + 
            (enabled ? "§a§lENABLED" : "§c§lDISABLED"));
        openGUI(player);
    }
} 