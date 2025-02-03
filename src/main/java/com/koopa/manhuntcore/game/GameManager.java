package com.koopa.manhuntcore.game;

import com.koopa.manhuntcore.ManhuntCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldBorder;
import org.apache.commons.io.FileUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GameManager {
    private final ManhuntCore plugin;
    private final Set<UUID> hunters;
    private UUID speedrunner;
    private boolean gameRunning;
    private Location spawnPoint;

    public GameManager(ManhuntCore plugin) {
        this.plugin = plugin;
        this.hunters = new HashSet<>();
        this.speedrunner = null;
    }

    private void generateNewWorld() {
        String worldName = "manhunt_" + System.currentTimeMillis();
        WorldCreator creator = new WorldCreator(worldName);
        World newWorld = creator.createWorld();
        
        if (newWorld != null) {
            // Set spawn point in new world
            Location spawn = newWorld.getSpawnLocation();
            setSpawnPoint(spawn);
            
            // Teleport all players to new world
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.teleport(spawn);
            }
            
            // Set world border
            int borderSize = plugin.getConfig().getInt("world.border-size", 2000);
            WorldBorder border = newWorld.getWorldBorder();
            border.setCenter(spawn);
            border.setSize(borderSize);
            
            plugin.getLogger().info("Created new world: " + worldName);
        }
    }

    public void startGame() {
        if (speedrunner == null) {
            Bukkit.broadcastMessage("§8[§d§lManhunt§8] §cCannot start game: No speedrunner assigned!");
            return;
        }

        if (hunters.isEmpty()) {
            Bukkit.broadcastMessage("§8[§d§lManhunt§8] §cCannot start game: No hunters assigned!");
            return;
        }

        // Check if we should generate a new world
        if (plugin.getConfig().getBoolean("world.generate-new", false)) {
            generateNewWorld();
        }

        // Rest of start game logic
        gameRunning = true;
        
        // Teleport players to spawn point if set
        if (spawnPoint != null) {
            Player runner = Bukkit.getPlayer(speedrunner);
            if (runner != null) {
                runner.teleport(spawnPoint);
            }
            
            for (UUID hunterId : hunters) {
                Player hunter = Bukkit.getPlayer(hunterId);
                if (hunter != null) {
                    hunter.teleport(spawnPoint);
                }
            }
        }

        Bukkit.broadcastMessage("§8[§d§lManhunt§8] §aGame started!");
    }

    public void endGame() {
        if (!gameRunning) {
            return;
        }

        gameRunning = false;
        Bukkit.broadcastMessage("§8[§d§lManhunt§8] §cGame has been ended!");
        
        // Reset player states
        resetPlayers();
    }

    public void resetGame() {
        gameRunning = false;
        hunters.clear();
        speedrunner = null;
        spawnPoint = null;
        
        // Delete generated world if it exists
        if (plugin.getConfig().getBoolean("world.generate-new", false)) {
            for (World world : Bukkit.getWorlds()) {
                if (world.getName().startsWith("manhunt_")) {
                    // Teleport players out
                    for (Player player : world.getPlayers()) {
                        player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                    }
                    
                    // Unload and delete world
                    Bukkit.unloadWorld(world, false);
                    try {
                        FileUtils.deleteDirectory(world.getWorldFolder());
                    } catch (IOException e) {
                        plugin.getLogger().warning("Failed to delete world: " + world.getName());
                    }
                }
            }
        }
        
        Bukkit.broadcastMessage("§8[§d§lManhunt§8] §cGame reset!");
    }

    public void addHunter(Player player) {
        if (player.equals(speedrunner)) {
            speedrunner = null;
        }
        hunters.add(player.getUniqueId());
        player.sendMessage("§8[§d§lManhunt§8] §7You are now a §chunter§7!");
    }

    public void setSpeedrunner(Player player) {
        hunters.remove(player.getUniqueId());
        speedrunner = player.getUniqueId();
        player.sendMessage("§8[§d§lManhunt§8] §7You are now the §bspeedrunner§7!");
    }

    private void applyGameSettings() {
        // Apply difficulty
        String difficulty = plugin.getConfig().getString("difficulty", "NORMAL");
        Bukkit.getWorlds().forEach(world -> 
            world.setDifficulty(org.bukkit.Difficulty.valueOf(difficulty)));
        
        // Apply world border
        int borderSize = plugin.getConfig().getInt("world.border-size", 2000);
        Bukkit.getWorlds().forEach(world -> 
            world.getWorldBorder().setSize(borderSize));
    }

    private void giveHuntersEquipment() {
        List<String> gear = plugin.getConfig().getStringList("hunter-gear");
        for (UUID hunterId : hunters) {
            Player hunter = Bukkit.getPlayer(hunterId);
            if (hunter != null) {
                for (String item : gear) {
                    // Parse item format: MATERIAL:AMOUNT
                    String[] parts = item.split(":");
                    org.bukkit.Material material = org.bukkit.Material.valueOf(parts[0]);
                    int amount = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
                    
                    hunter.getInventory().addItem(new org.bukkit.inventory.ItemStack(material, amount));
                }
            }
        }
    }

    private void startCountdown() {
        int countdown = plugin.getConfig().getInt("timer.start-countdown", 10);
        // TODO: Implement countdown timer
    }

    private void resetPlayers() {
        // Reset all players' inventories, gamemodes, etc.
        List<Player> allPlayers = new ArrayList<>();
        for (UUID hunterId : hunters) {
            Player hunter = Bukkit.getPlayer(hunterId);
            if (hunter != null) {
                allPlayers.add(hunter);
            }
        }
        if (speedrunner != null) {
            Player runner = Bukkit.getPlayer(speedrunner);
            if (runner != null) {
                allPlayers.add(runner);
            }
        }

        for (Player player : allPlayers) {
            player.getInventory().clear();
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setGameMode(org.bukkit.GameMode.SURVIVAL);
        }
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public Set<UUID> getHunters() {
        return new HashSet<>(hunters);
    }

    public UUID getSpeedrunner() {
        return speedrunner;
    }

    public void setSpawnPoint(Location location) {
        this.spawnPoint = location;
    }

    public boolean isHunter(UUID playerId) {
        return hunters.contains(playerId);
    }

    public void handlePlayerDeath(Player player) {
        if (hunters.contains(player.getUniqueId())) {
            // Respawn hunter with gear
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (spawnPoint != null) {
                    player.teleport(spawnPoint);
                }
                giveHuntersEquipment();
            }, 1L);
        } else if (player.getUniqueId().equals(speedrunner)) {
            // End game - hunters win
            endGame();
            plugin.getServer().broadcastMessage("§8[§d§lManhunt§8] §cGame Over! Hunters win!");
            player.setGameMode(GameMode.SPECTATOR);
            
            // Update statistics
            int hunterWins = plugin.getConfig().getInt("stats.hunter-wins", 0);
            plugin.getConfig().set("stats.hunter-wins", hunterWins + 1);
            plugin.saveConfig();
        }
    }
} 