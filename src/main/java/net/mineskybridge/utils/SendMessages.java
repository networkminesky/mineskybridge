package net.mineskybridge.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.chat.TextComponent;
import net.mineskybridge.MineSkyBridge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

public class SendMessages {
    public static void sendVanishUpdate(Player player, boolean vanished) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeUTF("VanishUpdate");
            dos.writeUTF(player.getUniqueId().toString());
            dos.writeBoolean(vanished);

            byte[] message = baos.toByteArray();

            Collection<? extends Player> players = Bukkit.getOnlinePlayers();
            if (!players.isEmpty()) {
                players.forEach(p -> p.sendPluginMessage(MineSkyBridge.getInstance(), "minesky:proxy", message));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}