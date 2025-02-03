package com.koopa.manhuntcore;

import com.koopa.manhuntcore.commands.ManhuntCommand;
import com.koopa.manhuntcore.commands.ManhuntTabCompleter;
import com.koopa.manhuntcore.game.GameManager;
import com.koopa.manhuntcore.listeners.PlayerListener;
import com.koopa.manhuntcore.listeners.WorldListener;
import com.koopa.manhuntcore.listeners.GUIListener;
import com.koopa.manhuntcore.listeners.ChatListener;
import com.koopa.manhuntcore.utils.VersionChecker;
import org.bukkit.plugin.java.JavaPlugin;

public class ManhuntCore extends JavaPlugin {
    private GameManager gameManager;
    private VersionChecker versionChecker;
    private ChatListener chatListener;

    @Override
    public void onEnable() {
        // Save default config if it doesn't exist
        saveDefaultConfig();

        // Initialize version checker
        this.versionChecker = new VersionChecker(this);
        this.versionChecker.checkVersion();

        // Initialize game manager
        this.gameManager = new GameManager(this);

        // Initialize chat listener
        this.chatListener = new ChatListener(this);

        // Register commands and tab completer
        ManhuntCommand manhuntCommand = new ManhuntCommand(this);
        getCommand("manhunt").setExecutor(manhuntCommand);
        getCommand("manhunt").setTabCompleter(new ManhuntTabCompleter(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(chatListener, this);

        getLogger().info("ManhuntCore has been enabled!");
    }

    @Override
    public void onDisable() {
        if (gameManager != null) {
            gameManager.endGame();
        }
        getLogger().info("ManhuntCore has been disabled!");
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ChatListener getChatListener() {
        return chatListener;
    }
} 