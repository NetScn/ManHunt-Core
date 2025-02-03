package com.koopa.manhuntcore.commands;

import com.koopa.manhuntcore.ManhuntCore;
import com.koopa.manhuntcore.gui.SetupGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ManhuntCommand implements CommandExecutor {
    private final ManhuntCore plugin;
    private final Map<String, CommandInfo> commands;

    public ManhuntCommand(ManhuntCore plugin) {
        this.plugin = plugin;
        this.commands = new HashMap<>();
        registerCommands();
    }

    private void registerCommands() {
        commands.put("help", new CommandInfo(
            "help [category]",
            "Display help information",
            "Categories: game, setup, admin",
            "manhunt.help"
        ));

        commands.put("credits", new CommandInfo(
            "credits",
            "Show plugin credits",
            "Display information about the plugin creators",
            "manhunt.credits"
        ));

        commands.put("setup", new CommandInfo(
            "setup",
            "Open the setup GUI",
            "Configure all aspects of the manhunt game",
            "manhunt.setup"
        ));

        // Game commands
        commands.put("start", new CommandInfo(
            "start",
            "Start the manhunt game",
            "Begins the game with current hunters and speedrunner",
            "manhunt.start"
        ));

        // Setup commands
        commands.put("setspawn", new CommandInfo(
            "setspawn",
            "Set the game spawn point",
            "Sets spawn point for hunters at your current location",
            "manhunt.setspawn"
        ));
        commands.put("addhunter", new CommandInfo(
            "addhunter <player>",
            "Add a hunter to the game",
            "Assigns the specified player as a hunter",
            "manhunt.addhunter"
        ));
        commands.put("addspeedrunner", new CommandInfo(
            "addspeedrunner <player>",
            "Set the speedrunner",
            "Assigns the specified player as the speedrunner",
            "manhunt.addspeedrunner"
        ));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§8[§d§lManhunt§8] §7Use /manhunt help for commands");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help" -> showHelp(sender, args.length > 1 ? args[1] : null);
            case "credits" -> showCredits(sender);
            case "setup" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("§8[§d§lManhunt§8] §cOnly players can use the setup GUI!");
                    return true;
                }
                if (!sender.hasPermission("manhunt.setup")) {
                    sender.sendMessage("§8[§d§lManhunt§8] §cYou don't have permission to use this command!");
                    return true;
                }
                new SetupGUI(plugin).openSetupGUI(player);
            }
            case "debug" -> handleDebugCommand(sender, args);
            case "start" -> {
                if (!sender.hasPermission("manhunt.start")) {
                    sender.sendMessage("§8[§d§lManhunt§8] §cYou don't have permission to use this command!");
                    return true;
                }
                plugin.getGameManager().startGame();
            }
            case "setspawn" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("§8[§d§lManhunt§8] §cOnly players can set the spawn point!");
                    return true;
                }
                if (!sender.hasPermission("manhunt.setspawn")) {
                    sender.sendMessage("§8[§d§lManhunt§8] §cYou don't have permission to use this command!");
                    return true;
                }
                plugin.getGameManager().setSpawnPoint(player.getLocation());
                sender.sendMessage("§8[§d§lManhunt§8] §aSpawn point set!");
            }
            default -> sender.sendMessage("§8[§d§lManhunt§8] §cUnknown command. Use /manhunt help for help.");
        }

        return true;
    }

    private void handleDebugCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("manhunt.debug")) {
            sender.sendMessage("§8[§d§lManhunt§8] §cYou don't have permission to use debug commands!");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage("§8[§d§lManhunt§8] §cUsage: /manhunt debug <role|start|reset>");
            return;
        }

        switch (args[1].toLowerCase()) {
            case "role" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("§8[§d§lManhunt§8] §cOnly players can use this command!");
                    return;
                }
                if (args.length < 3) {
                    sender.sendMessage("§8[§d§lManhunt§8] §cUsage: /manhunt debug role <hunter|speedrunner>");
                    return;
                }
                switch (args[2].toLowerCase()) {
                    case "hunter" -> {
                        plugin.getGameManager().addHunter(player);
                        sender.sendMessage("§8[§d§lManhunt§8] §aSet as hunter!");
                    }
                    case "speedrunner" -> {
                        plugin.getGameManager().setSpeedrunner(player);
                        sender.sendMessage("§8[§d§lManhunt§8] §aSet as speedrunner!");
                    }
                    default -> sender.sendMessage("§8[§d§lManhunt§8] §cInvalid role! Use hunter or speedrunner");
                }
            }
            case "start" -> {
                plugin.getGameManager().startGame();
                sender.sendMessage("§8[§d§lManhunt§8] §aForce started the game!");
            }
            case "reset" -> {
                plugin.getGameManager().resetGame();
                sender.sendMessage("§8[§d§lManhunt§8] §aReset the game!");
            }
            default -> sender.sendMessage("§8[§d§lManhunt§8] §cUnknown debug command!");
        }
    }

    private void showHelp(CommandSender sender, String category) {
        sender.sendMessage(ChatColor.GOLD + "=== Manhunt Help ===");

        if (category == null) {
            // Show categories
            sender.sendMessage(ChatColor.YELLOW + "Categories:");
            sender.sendMessage(ChatColor.WHITE + "- game: Game control commands");
            sender.sendMessage(ChatColor.WHITE + "- setup: Game setup commands");
            sender.sendMessage(ChatColor.WHITE + "- admin: Administrative commands");
            sender.sendMessage(ChatColor.YELLOW + "Use /manhunt help <category> for specific help");
            return;
        }

        switch (category.toLowerCase()) {
            case "game":
                showCategoryHelp(sender, "Game Commands", 
                    Arrays.asList("start"));
                break;

            case "setup":
                showCategoryHelp(sender, "Setup Commands", 
                    Arrays.asList("setspawn", "addhunter", "addspeedrunner"));
                break;

            case "admin":
                showCategoryHelp(sender, "Admin Commands", 
                    Arrays.asList("reload"));
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Unknown category. Available categories: game, setup, admin");
                break;
        }
    }

    private void showCategoryHelp(CommandSender sender, String categoryName, Iterable<String> commandList) {
        sender.sendMessage(ChatColor.GOLD + "=== " + categoryName + " ===");
        for (String cmdName : commandList) {
            CommandInfo info = commands.get(cmdName);
            if (info != null && sender.hasPermission(info.permission())) {
                sender.sendMessage(ChatColor.YELLOW + "/manhunt " + info.usage());
                sender.sendMessage(ChatColor.WHITE + "  " + info.description());
            }
        }
    }

    private void showCredits(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== ManhuntCore Credits ===");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.YELLOW + "Plugin Developer:");
        sender.sendMessage(ChatColor.WHITE + "• Koopa");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.YELLOW + "Special Thanks:");
        sender.sendMessage(ChatColor.WHITE + "• Dream - Original Manhunt concept");
        sender.sendMessage(ChatColor.WHITE + "• Minecraft Community");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.YELLOW + "Version: " + 
            ChatColor.WHITE + plugin.getDescription().getVersion());
        sender.sendMessage(ChatColor.YELLOW + "Support: " + 
            ChatColor.WHITE + "https://github.com/Koopa/ManhuntCore");
    }

    private record CommandInfo(String usage, String description, String details, String permission) {}
} 