package net.mineskybridge.utils;

import net.minesky.api.messaging.Messaging;
import net.minesky.core.messaging.MessageType;
import org.bson.Document;
import org.bukkit.entity.Player;

public class SendMessages {

    public static void sendVanishUpdate(Player player, boolean vanished) {
        if (player == null) return;

        Document doc = new Document("uuid", player.getUniqueId().toString())
                .append("vanished", vanished);

        Messaging.publish("minesky:network", "vanish-update", MessageType.DOCUMENT, doc);
    }
}