package com.koopa.manhuntcore.listeners;

import com.koopa.manhuntcore.ManhuntCore;
import com.koopa.manhuntcore.gui.*;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;

public class GUIListener implements Listener {
    private final ManhuntCore plugin;

    public GUIListener(ManhuntCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        
        // Cancel any click in the top inventory (GUI)
        if (event.getClickedInventory() != null && 
            event.getClickedInventory().getType() != InventoryType.PLAYER) {
            
            // Cancel all item movement for plugin GUIs
            if (title.contains("Manhunt") || 
                title.contains("Hunter") || 
                title.contains("World") || 
                title.contains("Game") || 
                title.contains("Timer") || 
                title.contains("Statistics")) {
                
                event.setCancelled(true);
            }
        }
        
        // Handle GUI clicks
        if (title.equals(ChatColor.translateAlternateColorCodes('&', "&8&lManhunt Setup"))) {
            new SetupGUI(plugin).handleClick(event);
        }
        else if (title.equals(ChatColor.translateAlternateColorCodes('&', "&8Hunter Settings"))) {
            new HunterSettingsGUI(plugin).handleClick(event);
        }
        else if (title.equals(ChatColor.translateAlternateColorCodes('&', "&8Hunter Equipment"))) {
            new HunterEquipmentGUI(plugin).handleClick(event);
        }
        else if (title.equals(ChatColor.translateAlternateColorCodes('&', "&8Game Settings"))) {
            new GameSettingsGUI(plugin).handleClick(event);
        }
        else if (title.equals(ChatColor.translateAlternateColorCodes('&', "&8World Settings"))) {
            new WorldSettingsGUI(plugin).handleClick(event);
        }
        else if (title.equals(ChatColor.translateAlternateColorCodes('&', "&8Win Conditions"))) {
            new WinConditionsGUI(plugin).handleClick(event);
        }
        else if (title.equals(ChatColor.translateAlternateColorCodes('&', "&8Timer Settings"))) {
            new TimerSettingsGUI(plugin).handleClick(event);
        }
        else if (title.equals(ChatColor.translateAlternateColorCodes('&', "&8Game Control"))) {
            new GameControlGUI(plugin).handleClick(event);
        }
        else if (title.equals(ChatColor.translateAlternateColorCodes('&', "&8Statistics"))) {
            new StatisticsGUI(plugin).handleClick(event);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        String title = event.getView().getTitle();
        
        // Cancel all drag events in plugin GUIs
        if (title.contains("Manhunt") || 
            title.contains("Hunter") || 
            title.contains("World") || 
            title.contains("Game") || 
            title.contains("Timer") || 
            title.contains("Statistics")) {
            
            event.setCancelled(true);
        }
    }
} 