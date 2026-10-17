package net.mineskybridge.event;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.mineskybridge.MineSkyBridge;
import net.mineskybridge.hooks.VaultHook;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;

public class MessageListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player carrier, byte[] message) {
        if (!"minesky:proxy".equals(channel)) return;

        ByteArrayDataInput input = ByteStreams.newDataInput(message);
        String subChannel = input.readUTF();

        if (!"PlayerVault".equalsIgnoreCase(subChannel)) return;

        String playerName = input.readUTF();
        UUID playerUUID = UUID.fromString(input.readUTF());
        int amount = input.readInt();

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);

        Economy economy = VaultHook.economy;
        if (economy != null) {
            EconomyResponse response = economy.depositPlayer(offlinePlayer, amount);

            if (response.transactionSuccess()) {
                MineSkyBridge.l.info("§a[REWARD] Depositado $" + amount + " para " + playerName + " com sucesso!");
            } else {
                MineSkyBridge.l.warning("§c[REWARD] Falha ao depositar para " + playerName + ": " + response.errorMessage);
            }
        } else {
            MineSkyBridge.l.warning("§c[REWARD] Vault Economy nao encontrado! Guardando no cache.");
            MineSkyBridge.PlayerVault.merge(playerUUID, amount, Integer::sum);
        }
        
        Player onlinePlayer = Bukkit.getPlayer(playerUUID);
        if (onlinePlayer != null && onlinePlayer.isOnline()) {
            onlinePlayer.sendMessage("§a§l[!] §aVocê recebeu §e$" + amount + " §apor sua colocação no evento!");
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }
    }
}