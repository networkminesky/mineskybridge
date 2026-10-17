package net.mineskybridge.event;

import net.mineskybridge.utils.SendMessages;
import net.mineskyvanish.api.vanish.VanishAPI;
import net.mineskyvanish.api.vanish.events.PlayerHideEvent;
import net.mineskyvanish.api.vanish.events.PlayerShowEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerInfo implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        SendMessages.sendVanishUpdate(player, VanishAPI.isInvisible(player));
    }

    @EventHandler
    public void onPlayerHide(PlayerHideEvent e) {
        SendMessages.sendVanishUpdate(e.getPlayer(), true);
    }

    @EventHandler
    public void onPlayerShow(PlayerShowEvent e) {
        SendMessages.sendVanishUpdate(e.getPlayer(), false);
    }
}