package com.koopa.manhuntcore.gui;

import com.koopa.manhuntcore.ManhuntCore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class SetupGUI extends BaseGUI {

    public SetupGUI(ManhuntCore plugin) {
        super(plugin, "&8&lManhunt Setup", 45);
    }

    public void openSetupGUI(Player player) {
        Inventory gui = createInventory();

        // Add functional items
        gui.setItem(11, createGuiItem(Material.COMPASS, 
            "&6Hunter Settings",
            "&7Configure hunter equipment",
            "&7and abilities"));

        gui.setItem(13, createGuiItem(Material.DIAMOND_SWORD,
            "&bGame Settings",
            "&7Adjust game rules and",
            "&7mechanics"));

        gui.setItem(15, createGuiItem(Material.GRASS_BLOCK,
            "&aWorld Settings",
            "&7Configure world generation",
            "&7and spawn points"));

        gui.setItem(29, createGuiItem(Material.CLOCK,
            "&eTimer Settings",
            "&7Set countdown and",
            "&7game duration"));

        gui.setItem(31, createGuiItem(Material.REDSTONE,
            "&cGame Control",
            "&7Start, stop, or",
            "&7reset the game"));

        gui.setItem(33, createGuiItem(Material.BOOK,
            "&dStatistics",
            "&7View game statistics",
            "&7and player records"));

        player.openInventory(gui);
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case 11 -> new HunterSettingsGUI(plugin).openGUI(player);
            case 13 -> new GameSettingsGUI(plugin).openGUI(player);
            case 15 -> new WorldSettingsGUI(plugin).openGUI(player);
            case 29 -> new TimerSettingsGUI(plugin).openGUI(player);
            case 31 -> new GameControlGUI(plugin).openGUI(player);
            case 33 -> new StatisticsGUI(plugin).openGUI(player);
        }
    }
} 