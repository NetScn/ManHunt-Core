package com.koopa.manhuntcore.gui;

import com.koopa.manhuntcore.ManhuntCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class BaseGUI {
    protected final ManhuntCore plugin;
    protected final String title;
    protected final int size;

    public BaseGUI(ManhuntCore plugin, String title, int size) {
        this.plugin = plugin;
        this.title = ChatColor.translateAlternateColorCodes('&', title);
        this.size = size;
    }

    protected Inventory createInventory() {
        Inventory gui = Bukkit.createInventory(null, size, title);
        fillBackground(gui);
        return gui;
    }

    protected void fillBackground(Inventory gui) {
        ItemStack filler = createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, filler);
        }
    }

    protected ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            if (lore.length > 0) {
                meta.setLore(Arrays.stream(lore)
                    .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                    .toList());
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    protected ItemStack createToggleItem(Material material, String name, boolean enabled, String... lore) {
        String status = enabled ? "&a&lENABLED" : "&c&lDISABLED";
        String[] newLore = new String[lore.length + 1];
        System.arraycopy(lore, 0, newLore, 0, lore.length);
        newLore[lore.length] = "&7Status: " + status;
        return createGuiItem(material, name, newLore);
    }

    // Add abstract method for handling clicks
    public abstract void handleClick(InventoryClickEvent event);
} 