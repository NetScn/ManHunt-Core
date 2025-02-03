package com.koopa.manhuntcore.commands;

import com.koopa.manhuntcore.ManhuntCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ManhuntTabCompleter implements TabCompleter {
    private final ManhuntCore plugin;
    private final List<String> mainCommands = Arrays.asList(
        "help", "credits", "setup", "start", "setspawn", "addhunter", "addspeedrunner"
    );
    private final List<String> helpCategories = Arrays.asList(
        "game", "setup", "admin"
    );

    public ManhuntTabCompleter(ManhuntCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // First argument - show all main commands
            String partialCommand = args[0].toLowerCase();
            for (String cmd : mainCommands) {
                if (cmd.startsWith(partialCommand) && hasPermission(sender, "manhunt." + cmd)) {
                    completions.add(cmd);
                }
            }
        } else if (args.length == 2) {
            // Second argument - show context-specific options
            String mainCommand = args[0].toLowerCase();
            String partialArg = args[1].toLowerCase();

            switch (mainCommand) {
                case "help":
                    // Show help categories
                    completions.addAll(helpCategories.stream()
                        .filter(cat -> cat.startsWith(partialArg))
                        .collect(Collectors.toList()));
                    break;

                case "addhunter":
                case "addspeedrunner":
                    // Show online players
                    completions.addAll(Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(partialArg))
                        .collect(Collectors.toList()));
                    break;
            }
        }

        return completions;
    }

    private boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission(permission) || sender.hasPermission("manhunt.admin");
    }
} 