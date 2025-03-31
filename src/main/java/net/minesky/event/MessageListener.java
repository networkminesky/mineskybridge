package net.minesky.event;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.minesky.MineSkyBridge;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;

public class MessageListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!"minesky:proxy".equals(channel)) return;

        ByteArrayDataInput input = ByteStreams.newDataInput(message);
        if (!"PlayerVault".equalsIgnoreCase(input.readUTF())) return;

        String playerName = input.readUTF();
        UUID playerUUID = UUID.fromString(input.readUTF());
        int amount = Integer.parseInt(input.readUTF());

        MineSkyBridge.l.info("👤 Player: " + playerName + " 💰 Valor: " + amount);

        MineSkyBridge.PlayerVault.put(playerUUID, MineSkyBridge.PlayerVault.getOrDefault(playerUUID, 0) + amount);
    }
}
