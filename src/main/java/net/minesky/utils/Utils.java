package net.minesky.utils;

import net.md_5.bungee.api.ChatColor;
import net.minesky.hooks.MineSkyVanishHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String c(String s) {
        return hex(s);
    }

    private static String createHex(String hexString) {
        hexString = hexString.replace("&", "");
        return ChatColor.of(hexString).toString();
    }

    public static String hex(String message) {
        Pattern hexPattern = Pattern.compile("&#[A-Fa-f0-9]{6}");
        Matcher matcher = hexPattern.matcher(message);

        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, createHex(matcher.group()));
        }
        matcher.appendTail(result);

        return ChatColor.translateAlternateColorCodes('&', result.toString());
    }

    public static String translateColorsToANSI(String message) {
        return message
                .replace("§0", "\u001B[30m")
                .replace("§1", "\u001B[34m")
                .replace("§2", "\u001B[32m")
                .replace("§3", "\u001B[36m")
                .replace("§4", "\u001B[31m")
                .replace("§5", "\u001B[35m")
                .replace("§6", "\u001B[33m")
                .replace("§7", "\u001B[37m")
                .replace("§8", "\u001B[90m")
                .replace("§9", "\u001B[94m")
                .replace("§a", "\u001B[92m")
                .replace("§b", "\u001B[96m")
                .replace("§c", "\u001B[91m")
                .replace("§d", "\u001B[95m")
                .replace("§e", "\u001B[93m")
                .replace("§f", "\u001B[97m")
                .replace("§r", "\u001B[0m")
                .replace("§n", "\u001B[4m")
                .replace("§l", "\u001B[1m")
                .replace("§o", "\u001B[3m");
    }

    public static List<String> onlineWithoutVanished() {
        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> !MineSkyVanishHook.isPlayerVanished(p))
                .map(Player::getName)
                .toList();
    }
}