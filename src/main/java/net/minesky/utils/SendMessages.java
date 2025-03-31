package net.minesky.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.chat.TextComponent;
import net.minesky.MineSkyBridge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SendMessages {

    private static void sendPluginMessage(String... data) {
        Player player = Bukkit.getOnlinePlayers().stream().findFirst().orElse(null);
        if (player != null) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            for (String s : data) {
                out.writeUTF(s);
            }
            player.sendPluginMessage(MineSkyBridge.getInstance(), "minesky:proxy", out.toByteArray());
        }
    }

    public static void sendPlayerMessage(Player player, TextComponent message) {
        sendPluginMessage("PlayerMessage", player.getName(), TextComponent.toLegacyText(message));
    }

    public static void sendVanishUpdate(Player player, boolean vanished) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeUTF("VanishUpdate");
            dos.writeUTF(player.getUniqueId().toString());
            dos.writeBoolean(vanished);

            Bukkit.getOnlinePlayers().forEach(p ->
                    p.sendPluginMessage(MineSkyBridge.getInstance(), "minesky:proxy", baos.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
