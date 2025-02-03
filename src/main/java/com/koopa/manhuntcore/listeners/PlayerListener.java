package com.koopa.manhuntcore.listeners;

import com.koopa.manhuntcore.ManhuntCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final ManhuntCore plugin;

    public PlayerListener(ManhuntCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (plugin.getGameManager().isGameRunning()) {
            plugin.getGameManager().handlePlayerDeath(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.getGameManager().isGameRunning()) {
            if (player.equals(plugin.getGameManager().getSpeedrunner())) {
                plugin.getGameManager().endGame();
                plugin.getServer().broadcastMessage("§8[§d§lManhunt§8] §cGame ended: Speedrunner disconnected!");
            }
        }
    }
} 