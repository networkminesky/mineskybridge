package net.minesky.event;

import de.myzelyam.api.vanish.PlayerHideEvent;
import de.myzelyam.api.vanish.PlayerShowEvent;
import de.myzelyam.api.vanish.VanishAPI;
import net.md_5.bungee.api.chat.TextComponent;
import net.minesky.MineSkyBridge;
import net.minesky.hooks.Vault;
import net.minesky.utils.SendMessages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerInfo implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        var player = e.getPlayer();
        SendMessages.sendVanishUpdate(player, VanishAPI.isInvisible(player));
        UUID playerUUID = e.getPlayer().getUniqueId();
        var offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());

        if (offlinePlayer == null) {
            MineSkyBridge.l.severe("❌ O jogador não foi encontrado.");
            SendMessages.sendPlayerMessage(player, new TextComponent(SendMessages.color("&cO jogador não foi encontrado.")));
            return;
        }
        MineSkyBridge.l.info(MineSkyBridge.PlayerVault.toString());

        MineSkyBridge.PlayerVault
                .computeIfPresent(playerUUID, (p, amount) -> {
                    MineSkyBridge.l.info("Setando dinheiro para " + player.getName() + " Valor: " + amount);
                    Vault.economy.depositPlayer(offlinePlayer, amount);
                    return null;
                });
    }

    @EventHandler
    public void onPlayerHide(PlayerHideEvent e) {
        Player p = e.getPlayer();
        SendMessages.sendVanishUpdate(p, VanishAPI.isInvisible(p));
    }

    @EventHandler
    public void onPlayerShow(PlayerShowEvent e) {
        Player p = e.getPlayer();
        SendMessages.sendVanishUpdate(p, VanishAPI.isInvisible(p));
    }
}
