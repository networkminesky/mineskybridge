package net.minesky.commands;

import net.minesky.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Ping implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String lbl, @NotNull String @NotNull [] args) {
        if (s instanceof  Player p) {
            if (args.length < 1) {
                s.sendMessage(Utils.c("&aO seu ping é de " + getPing(p) + "ms "));
                return true;
            } else {
                if (!p.hasPermission("mineskybridge.ping.others")) {
                    s.sendMessage(Utils.c("&aO seu ping é de " + getPing(p) + "ms "));
                    return true;
                }

                Player who = Bukkit.getPlayer(args[0]);
                if (who.isOnline()) {
                    p.sendMessage(Utils.c("&aO ping deste jogador é de " + getPing(who) + "ms "));
                } else {
                    p.sendMessage(Utils.c("&cJogador não encontrado."));
                }
            }
            return true;
        }
        return true;
    }

    private static int getPing(Player p) {
        return (int)p.getPing();
    }
}
