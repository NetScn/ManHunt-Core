package com.koopa.manhuntcore.gui;

import com.koopa.manhuntcore.ManhuntCore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WorldSettingsGUI extends BaseGUI {

    public WorldSettingsGUI(ManhuntCore plugin) {
        super(plugin, "&8World Settings", 54);
    }

    public void openGUI(Player player) {
        Inventory gui = createInventory();

        // Fill entire GUI with black stained glass panes first
        fillBackground(gui);

        // Top row items (12, 13, 14)
        ItemStack worldGen = createToggleItem(Material.GRASS_BLOCK,
            "&aWorld Generation",
            plugin.getConfig().getBoolean("world.generate-new", false),
            "&7Generate new world on start",
            "&7Click to toggle");
        gui.setItem(12, worldGen);

        ItemStack border = createGuiItem(Material.BARRIER,
            "&cWorld Border",
            "&7Border Size: &f" + plugin.getConfig().getInt("world.border-size", 2000),
            "&7Click to change");
        gui.setItem(13, border);

        ItemStack nether = createToggleItem(Material.NETHERRACK,
            "&4Nether Access",
            plugin.getConfig().getBoolean("world.nether-enabled", true),
            "&7Allow nether travel",
            "&7Click to toggle");
        gui.setItem(14, nether);

        // Middle row items (22)
        ItemStack end = createToggleItem(Material.END_STONE,
            "&5End Access",
            plugin.getConfig().getBoolean("world.end-enabled", true),
            "&7Allow end dimension",
            "&7Click to toggle");
        gui.setItem(22, end);

        // Back button
        ItemStack back = createGuiItem(Material.BARRIER,
            "&c&lBack",
            "&7Return to main menu");
        gui.setItem(49, back);

        // Set item flags to prevent picking up
        for (ItemStack item : gui.getContents()) {
            if (item != null) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setUnbreakable(true);
                    item.setItemMeta(meta);
                }
            }
        }

        player.openInventory(gui);
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (event.getCurrentItem() == null) {
            return;
        }

        switch (event.getSlot()) {
            case 12 -> toggleWorldGeneration(player);
            case 13 -> cycleBorderSize(player);
            case 14 -> toggleNetherAccess(player);
            case 22 -> toggleEndAccess(player);
            case 49 -> new SetupGUI(plugin).openSetupGUI(player);
        }
    }

    private void toggleWorldGeneration(Player player) {
        boolean enabled = !plugin.getConfig().getBoolean("world.generate-new", false);
        plugin.getConfig().set("world.generate-new", enabled);
        plugin.saveConfig();
        player.sendMessage("§8[§d§lManhunt§8] §7World Generation: " + 
            (enabled ? "§a§lENABLED" : "§c§lDISABLED"));
        openGUI(player);
    }

    private void cycleBorderSize(Player player) {
        int current = plugin.getConfig().getInt("world.border-size", 2000);
        int next = switch (current) {
            case 1000 -> 2000;
            case 2000 -> 5000;
            case 5000 -> 10000;
            case 10000 -> 1000;
            default -> 2000;
        };
        plugin.getConfig().set("world.border-size", next);
        plugin.saveConfig();
        player.sendMessage("§8[§d§lManhunt§8] §7Border size: §f" + next + " blocks");
        openGUI(player);
    }

    private void toggleNetherAccess(Player player) {
        boolean enabled = !plugin.getConfig().getBoolean("world.nether-enabled", true);
        plugin.getConfig().set("world.nether-enabled", enabled);
        plugin.saveConfig();
        player.sendMessage("§8[§d§lManhunt§8] §7Nether Access: " + 
            (enabled ? "§a§lENABLED" : "§c§lDISABLED"));
        openGUI(player);
    }

    private void toggleEndAccess(Player player) {
        boolean enabled = !plugin.getConfig().getBoolean("world.end-enabled", true);
        plugin.getConfig().set("world.end-enabled", enabled);
        plugin.saveConfig();
        player.sendMessage("§8[§d§lManhunt§8] §7End Access: " + 
            (enabled ? "§a§lENABLED" : "§c§lDISABLED"));
        openGUI(player);
    }
} 