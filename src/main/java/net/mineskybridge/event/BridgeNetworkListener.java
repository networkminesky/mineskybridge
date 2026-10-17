package net.mineskybridge.event;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.minesky.core.messaging.MessagingListener;
import net.minesky.core.messaging.NetworkMessage;
import net.mineskybridge.MineSkyBridge;
import net.mineskybridge.hooks.VaultHook;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BridgeNetworkListener extends MessagingListener {

    public BridgeNetworkListener() {
        super("minesky:network");
    }

    @Override
    public void onMessage(NetworkMessage message) {
        if (!"reward-vault".equals(message.subchannel())) {
            return;
        }

        try {
            Document doc = message.asDocument();
            String playerName = doc.getString("playerName");
            UUID playerUUID = UUID.fromString(doc.getString("uuid"));
            int amount = doc.getInteger("amount");

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
        } catch (Exception e) {
            MineSkyBridge.l.severe("Erro ao processar reward-vault no Mainframe Messaging: " + e.getMessage());
        }
    }

    @Override
    public void onMessage(String subchannel, String value) {
    }
}