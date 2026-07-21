package net.minesky.hooks;

import net.mineskyvanish.api.VanishAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MineSkyVanishHook {
    public static boolean isPlayerVanished(Player player) {
        return VanishAPI.isInvisible(player);
    }

    public static boolean isPlayerVanished(UUID uuid) {
        return isPlayerVanished(Bukkit.getPlayer(uuid));
    }

    public static boolean isPlayerVanished(String rawUsername) {
        Player p = Bukkit.getPlayer(rawUsername);
        if (p == null) return false;
        return isPlayerVanished(p);
    }
}
