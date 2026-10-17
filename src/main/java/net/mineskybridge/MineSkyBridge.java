package net.mineskybridge;

import net.minesky.api.messaging.Messaging;
import net.mineskybridge.event.BridgeNetworkListener;
import net.mineskybridge.event.PlayerInfo;
import net.mineskybridge.hooks.VaultHook;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public final class MineSkyBridge extends JavaPlugin {

    public static final Map<UUID, Integer> PlayerVault = new HashMap<>();
    public static Logger l;
    private BridgeNetworkListener networkListener;

    @Override
    public void onEnable() {
        l = getLogger();
        logStartupMessage();
        if (!checkPlugin("Vault")) {
            getLogger().warning("Vault não encontrado! Desativando plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        VaultHook.setupEconomy();

        if (!checkPlugin("MineSkyVanish")) {
            getLogger().warning("MineSkyVanish não encontrado! Desativando plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (Messaging.hasObserver()) {
            this.networkListener = new BridgeNetworkListener();
            Messaging.registerListener("MineSkyBridge", this.networkListener);
            l.info("Mainframe Messaging registrado com sucesso no canal 'minesky:network'.");
        } else {
            l.warning("Mainframe Messaging desabilitado ou sem conexao ativa com o MongoDB!");
        }

        getServer().getPluginManager().registerEvents(new PlayerInfo(), this);

        l.info("MineSkyBridge habilitado com sucesso!");
    }

    private boolean checkPlugin(String pluginName) {
        return getServer().getPluginManager().getPlugin(pluginName) != null;
    }

    private void logStartupMessage() {
        String[] art = {
                "    __  ___    _                  _____    __               ____             _        __               ",
                "   /  |/  /   (_)   ____   ___   / ___/   / /__   __  __   / __ )   _____   (_)  ____/ /   ____ _  ___ ",
                "  / /|_/ /   / /   / __ \\ / _ \\  \\__ \\   / //_/  / / / /  / __  |  / ___/  / /  / __  /   / __ `/ / _ \\",
                " / /  / /   / /   / / / //  __/ ___/ /  / ,<    / /_/ /  / /_/ /  / /     / /  / /_/ /   / /_/ / /  __/",
                "/_/  /_/   /_/   /_/ /_/ \\___/ /____/  /_/|_|   \\__, /  /_____/  /_/     /_/   \\__,_/    \\__, /  \\___/ ",
                "                                               /____/                                   /____/"
        };
        for (String line : art) l.info(line);
        l.info("Carregando o plugin...");
    }

    public static MineSkyBridge getInstance() {
        return JavaPlugin.getPlugin(MineSkyBridge.class);
    }

    @Override
    public void onDisable() {
        if (networkListener != null) {
            Messaging.unregisterListener(networkListener);
        }
        l.info("MineSkyBridge desabilitado.");
    }
}