package com.koopa.manhuntcore.gui;

import com.koopa.manhuntcore.ManhuntCore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HunterSettingsGUI extends BaseGUI {
    
    public HunterSettingsGUI(ManhuntCore plugin) {
        super(plugin, "&8Hunter Settings", 36);
    }

    public void openGUI(Player player) {
        Inventory gui = createInventory();

        // Hunter Equipment Section
        gui.setItem(11, createGuiItem(Material.IRON_SWORD,
            "&6Starting Equipment",
            "&7Configure hunter starting items",
            "&7Click to modify"));

        // Compass Settings
        gui.setItem(13, createToggleItem(Material.COMPASS,
            "&bCompass Settings",
            plugin.getConfig().getBoolean("compass-enabled", true),
            "&7Configure tracking compass",
            "&7Click to toggle"));

        // Respawn Settings
        gui.setItem(15, createToggleItem(Material.RED_BED,
            "&cRespawn Settings",
            plugin.getConfig().getBoolean("hunter-respawn-enabled", true),
            "&7Configure hunter respawn",
            "&7Click to toggle"));

        // Back Button
        gui.setItem(31, createGuiItem(Material.BARRIER,
            "&c&lBack to Main Menu",
            "&7Click to return"));

        // Add decorative items to make the GUI more balanced
        gui.setItem(10, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " "));
        gui.setItem(12, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " "));
        gui.setItem(14, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " "));
        gui.setItem(16, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " "));

        // Add top and bottom decorative rows
        for (int i = 0; i < 9; i++) {
            gui.setItem(i, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " "));
            gui.setItem(27 + i, createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        // Add decorative barrier around back button
        gui.setItem(30, createGuiItem(Material.RED_STAINED_GLASS_PANE, " "));
        gui.setItem(31, createGuiItem(Material.BARRIER, "&c&lBack to Main Menu", "&7Click to return"));
        gui.setItem(32, createGuiItem(Material.RED_STAINED_GLASS_PANE, " "));

        player.openInventory(gui);
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case 11 -> openEquipmentEditor(player);
            case 13 -> toggleCompassTracking(player);
            case 15 -> toggleHunterRespawn(player);
            case 31 -> new SetupGUI(plugin).openSetupGUI(player);
        }
    }

    private void openEquipmentEditor(Player player) {
        new HunterEquipmentGUI(plugin).openGUI(player);
    }

    private void toggleCompassTracking(Player player) {
        boolean enabled = !plugin.getConfig().getBoolean("compass-enabled", true);
        plugin.getConfig().set("compass-enabled", enabled);
        plugin.saveConfig();
        player.sendMessage("§8[§d§lManhunt§8] §7Compass tracking: " + 
            (enabled ? "§a§lENABLED" : "§c§lDISABLED"));
        openGUI(player);
    }

    private void toggleHunterRespawn(Player player) {
        boolean enabled = !plugin.getConfig().getBoolean("hunter-respawn-enabled", true);
        plugin.getConfig().set("hunter-respawn-enabled", enabled);
        plugin.saveConfig();
        player.sendMessage("§8[§d§lManhunt§8] §7Hunter respawn: " + 
            (enabled ? "§a§lENABLED" : "§c§lDISABLED"));
        openGUI(player);
    }
} 