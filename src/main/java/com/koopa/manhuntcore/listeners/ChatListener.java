package com.koopa.manhuntcore.listeners;

import com.koopa.manhuntcore.ManhuntCore;
import com.koopa.manhuntcore.gui.HunterEquipmentGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatListener implements Listener {
    private final ManhuntCore plugin;
    private final Map<UUID, ChatInputType> awaitingInput;

    public ChatListener(ManhuntCore plugin) {
        this.plugin = plugin;
        this.awaitingInput = new HashMap<>();
    }

    public void expectInput(Player player, ChatInputType type) {
        awaitingInput.put(player.getUniqueId(), type);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ChatInputType inputType = awaitingInput.remove(player.getUniqueId());

        if (inputType == null) return;

        event.setCancelled(true);

        if (inputType == ChatInputType.HUNTER_EQUIPMENT) {
            handleHunterEquipmentInput(player, event.getMessage());
        }
    }

    private void handleHunterEquipmentInput(Player player, String input) {
        String[] parts = input.toUpperCase().split(":");
        if (parts.length < 1 || parts.length > 2) {
            player.sendMessage("§8[§d§lManhunt§8] §cInvalid format! Use: MATERIAL:AMOUNT");
            return;
        }

        try {
            Material material = Material.valueOf(parts[0]);
            int amount = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;

            if (amount < 1 || amount > 64) {
                player.sendMessage("§8[§d§lManhunt§8] §cAmount must be between 1 and 64!");
                return;
            }

            // Add item to config
            java.util.List<String> gear = plugin.getConfig().getStringList("hunter-gear");
            gear.add(material.name() + ":" + amount);
            plugin.getConfig().set("hunter-gear", gear);
            plugin.saveConfig();

            player.sendMessage("§8[§d§lManhunt§8] §7Added §f" + amount + "x " + material.name());
            
            // Reopen the equipment GUI
            plugin.getServer().getScheduler().runTask(plugin, () -> 
                new HunterEquipmentGUI(plugin).openGUI(player));

        } catch (IllegalArgumentException e) {
            player.sendMessage("§8[§d§lManhunt§8] §cInvalid material name!");
        }
    }

    public enum ChatInputType {
        HUNTER_EQUIPMENT
    }
} 