package com.koopa.manhuntcore.listeners;

import com.koopa.manhuntcore.ManhuntCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldListener implements Listener {
    private final ManhuntCore plugin;

    public WorldListener(ManhuntCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        // Update compass if the speedrunner changes worlds
        if (event.getPlayer().getUniqueId().equals(plugin.getGameManager().getSpeedrunner())) {
            plugin.getServer().broadcastMessage("§6The speedrunner has entered " + 
                event.getPlayer().getWorld().getName() + "!");
        }
    }
} 