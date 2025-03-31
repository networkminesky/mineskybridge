package net.minesky;

import net.minesky.event.MessageListener;
import net.minesky.event.PlayerInfo;
import net.minesky.hooks.Vault;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public final class MineSkyBridge extends JavaPlugin {

    public static Map<UUID, Integer> PlayerVault = new HashMap<>();
    public static Logger l;

    @Override
    public void onEnable() {
        l = this.getLogger();
        l.info("    __  ___    _                  _____    __               ____             _        __               ");
        l.info("   /  |/  /   (_)   ____   ___   / ___/   / /__   __  __   / __ )   _____   (_)  ____/ /   ____ _  ___ ");
        l.info("  / /|_/ /   / /   / __ \\ / _ \\  \\__ \\   / //_/  / / / /  / __  |  / ___/  / /  / __  /   / __ `/ / _ \\");
        l.info(" / /  / /   / /   / / / //  __/ ___/ /  / ,<    / /_/ /  / /_/ /  / /     / /  / /_/ /   / /_/ / /  __/");
        l.info("/_/  /_/   /_/   /_/ /_/ \\___/ /____/  /_/|_|   \\__, /  /_____/  /_/     /_/   \\__,_/    \\__, /  \\___/ ");
        l.info("                                               /____/                                   /____/");
        l.info("Carregando o plugin...");
            if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
             l.info("MineskyVaultBridge - Habilitando...");
             Vault.setupEconomy();
         } else {
               this.getPluginLoader().disablePlugin(this);
               return;
         }

            if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish")) {
               l.info("MineSkySuperVanishBridge - Habilitando...");
         } else {
               this.getPluginLoader().disablePlugin(this);
                return;
          }
            getServer().getMessenger().registerOutgoingPluginChannel(this, "minesky:proxy");
            getServer().getMessenger().registerIncomingPluginChannel(this, "minesky:proxy", new MessageListener());
            getServer().getPluginManager().registerEvents(new PlayerInfo(), this);
            l.info("Canais registrados: " + String.join(", ", Bukkit.getMessenger().getIncomingChannels()));
    }

    public static MineSkyBridge getInstance() {
        return MineSkyBridge.getPlugin(MineSkyBridge.class);
    }
}
