package net.minesky;

import net.minesky.commands.*;
import net.minesky.event.MessageListener;
import net.minesky.event.PlayerInfo;
import net.minesky.hooks.VaultHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public final class MineSkyBridge extends JavaPlugin {

    public static final Map<UUID, Integer> PlayerVault = new HashMap<>();
    public static Logger l;

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
        getServer().getMessenger().registerOutgoingPluginChannel(this, "minesky:proxy");
        getServer().getMessenger().registerIncomingPluginChannel(this, "minesky:proxy", new MessageListener());
        getServer().getPluginManager().registerEvents(new PlayerInfo(), this);

        registerCommands();

        l.info("Canais registrados: " + String.join(", ", Bukkit.getMessenger().getIncomingChannels()));
        l.info("MineSkyBridge habilitado com sucesso!");
    }

    private boolean checkPlugin(String pluginName) {
        return getServer().getPluginManager().getPlugin(pluginName) != null;
    }

    private void registerCommands() {
        this.getCommand("staffchat").setExecutor(new StaffChat());
        this.getCommand("ping").setExecutor(new Ping());
        this.getCommand("anunciar").setExecutor(new Anunciar());
        this.getCommand("tell").setExecutor(new Tell());
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
        getServer().getMessenger().unregisterIncomingPluginChannel(this);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        l.info("MineSkyBridge desabilitado.");
    }
}