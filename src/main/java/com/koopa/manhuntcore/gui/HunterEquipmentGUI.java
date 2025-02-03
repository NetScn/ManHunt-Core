package com.koopa.manhuntcore.gui;

import com.koopa.manhuntcore.ManhuntCore;
import com.koopa.manhuntcore.listeners.ChatListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HunterEquipmentGUI extends BaseGUI {

    public HunterEquipmentGUI(ManhuntCore plugin) {
        super(plugin, "&8Hunter Equipment", 54);
    }

    public void openGUI(Player player) {
        Inventory gui = createInventory();

        // Equipment slots
        List<String> currentGear = plugin.getConfig().getStringList("hunter-gear");
        int slot = 10;
        for (String item : currentGear) {
            String[] parts = item.split(":");
            Material material = Material.valueOf(parts[0]);
            int amount = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
            gui.setItem(slot, createGuiItem(material,
                "&6" + material.name(),
                "&7Amount: &f" + amount,
                "&7Click to remove"));
            slot++;
            if ((slot + 1) % 9 == 0) slot += 2;
        }

        // Add Item Button
        gui.setItem(49, createGuiItem(Material.EMERALD,
            "&a&lAdd Item",
            "&7Click to add new item"));

        // Back Button
        gui.setItem(45, createGuiItem(Material.BARRIER,
            "&c&lBack",
            "&7Return to hunter settings"));

        player.openInventory(gui);
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() == 45) {
            new HunterSettingsGUI(plugin).openGUI(player);
            return;
        }

        if (event.getSlot() == 49) {
            player.closeInventory();
            player.sendMessage("§8[§d§lManhunt§8] §7Type the item to add (format: MATERIAL:AMOUNT)");
            plugin.getChatListener().expectInput(player, ChatListener.ChatInputType.HUNTER_EQUIPMENT);
            return;
        }

        ItemStack clicked = event.getCurrentItem();
        if (clicked != null && clicked.getType() != Material.BLACK_STAINED_GLASS_PANE) {
            removeItem(player, clicked.getType());
        }
    }

    private void removeItem(Player player, Material material) {
        List<String> gear = plugin.getConfig().getStringList("hunter-gear");
        gear.removeIf(item -> item.startsWith(material.name()));
        plugin.getConfig().set("hunter-gear", gear);
        plugin.saveConfig();
        player.sendMessage("§8[§d§lManhunt§8] §7Removed §f" + material.name() + " §7from hunter gear");
        openGUI(player);
    }
} 