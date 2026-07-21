package net.minesky.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minesky.hooks.MineSkyVanishHook;
import net.minesky.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static net.minesky.utils.Utils.translateColorsToANSI;

public class StaffChat implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cnd, @NotNull String lbl, @NotNull String @NotNull [] args) {
        if (!s.hasPermission("mineskybridge.staffchat")) {
            s.sendMessage(Utils.c("&cVocê não pode executar esse comando."));
            return true;
        }

        if (args.length < 1) {
            s.sendMessage(Utils.c("&cVocê deve indicar uma mensagem ao comando."));
            return true;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        String name = (s instanceof Player player) ? player.getName() : "CONSOLE";
        String sv = (s instanceof Player) ? "RPG" : "Nenhum (Console)";
        sendStaffMessage(s, name, message, sv);
        return true;
    }

    static void sendStaffMessage(CommandSender s, String origin, String msg, String originServer) {
        Component p = Component.text("§4[s] §c§n"+origin+"§8: §f"+Utils.c(msg))
                .hoverEvent(HoverEvent.showText(Component.text("§7Servidor: §6"+originServer
                        +"\n§7Vanish: "+ (MineSkyVanishHook.isPlayerVanished(origin) ? "§asim" : "§cnão")
                )));

        Bukkit.getConsoleSender().sendMessage(translateColorsToANSI(LegacyComponentSerializer.legacySection().serialize(p)));
        Bukkit.getOnlinePlayers().stream()
                .filter(bs -> bs.hasPermission("mineskybridge.staffchat"))
                .forEach(bs -> bs.sendMessage(p));

    }
}
