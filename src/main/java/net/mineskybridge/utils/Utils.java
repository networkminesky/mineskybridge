package net.mineskybridge.utils;

import net.md_5.bungee.api.ChatColor;
import net.mineskybridge.hooks.MineSkyVanishHook;
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

    public static List<String> onlineWithoutVanished() {
        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> !MineSkyVanishHook.isPlayerVanished(p))
                .map(Player::getName)
                .toList();
    }
}