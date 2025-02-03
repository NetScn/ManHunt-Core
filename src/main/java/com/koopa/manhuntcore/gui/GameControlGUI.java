package com.koopa.manhuntcore.gui;

import com.koopa.manhuntcore.ManhuntCore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GameControlGUI extends BaseGUI {

    public GameControlGUI(ManhuntCore plugin) {
        super(plugin, "&8Game Control", 36);
    }

    public void openGUI(Player player) {
        Inventory gui = createInventory();

        // Start Game
        gui.setItem(11, createGuiItem(Material.EMERALD_BLOCK,
            "&a&lStart Game",
            "&7Begin the manhunt",
            "&7with current settings"));

        // Stop Game
        gui.setItem(13, createGuiItem(Material.REDSTONE_BLOCK,
            "&c&lStop Game",
            "&7End the current game",
            "&7and reset players"));

        // Reset Game
        gui.setItem(15, createGuiItem(Material.BARRIER,
            "&4&lReset Game",
            "&7Clear all game data",
            "&7and player assignments"));

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
            case 11 -> startGame(player);
            case 13 -> stopGame(player);
            case 15 -> resetGame(player);
            case 31 -> new SetupGUI(plugin).openSetupGUI(player);
        }
    }

    private void startGame(Player player) {
        plugin.getGameManager().startGame();
        player.closeInventory();
    }

    private void stopGame(Player player) {
        plugin.getGameManager().endGame();
        player.sendMessage("§8[§d§lManhunt§8] §7Game has been stopped!");
        player.closeInventory();
    }

    private void resetGame(Player player) {
        plugin.getGameManager().resetGame();
        player.sendMessage("§8[§d§lManhunt§8] §7Game has been reset!");
        player.closeInventory();
    }
} 