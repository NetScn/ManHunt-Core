package com.koopa.manhuntcore.utils;

import com.koopa.manhuntcore.ManhuntCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CompassTracker {
    private final ManhuntCore plugin;

    public CompassTracker(ManhuntCore plugin) {
        this.plugin = plugin;
    }

    public void updateCompass(Player hunter) {
        if (!plugin.getConfig().getBoolean("compass-enabled", true)) {
            return;
        }

        UUID targetId = plugin.getGameManager().getSpeedrunner();
        if (targetId == null) return;

        Player target = Bukkit.getPlayer(targetId);
        if (target == null) return;

        // Update compass if hunter has one
        for (ItemStack item : hunter.getInventory()) {
            if (item != null && item.getType() == Material.COMPASS) {
                hunter.setCompassTarget(target.getLocation());
                break;
            }
        }
    }

    public void giveCompass(Player hunter) {
        if (plugin.getConfig().getBoolean("compass-enabled", true)) {
            hunter.getInventory().addItem(new ItemStack(Material.COMPASS));
        }
    }
} 