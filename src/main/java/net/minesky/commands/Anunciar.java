package net.minesky.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minesky.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static net.minesky.utils.Utils.translateColorsToANSI;

public class Anunciar implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String lbl, @NotNull String @NotNull [] args) {
        if (!s.hasPermission("mineskybridge.anunciar")) {
            s.sendMessage(Utils.c("&cVocê não pode executar esse comando."));
            return true;
        }

        if (args.length < 1) {
            s.sendMessage(Utils.c("&cVocê deve indicar uma mensagem ao comando."));
            return true;
        }

        final String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length)).replace('&', '§');
        final Component parsedLegacyText = LegacyComponentSerializer.legacySection().deserialize(message);

        Component t = Component.text(" \n§6§lMine§f§lSky §8: §6")
                .append(parsedLegacyText.color(NamedTextColor.GOLD))
                .append(Component.text("\n "))
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("" +
                        "§a✔ Mensagem oficial do servidor!")));

        for (Player b : Bukkit.getOnlinePlayers()) {
            b.sendMessage(t);
            b.playSound(b, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 1.4f);
        }
        Bukkit.getConsoleSender().sendMessage(translateColorsToANSI(LegacyComponentSerializer.legacySection().serialize(t)));
        return true;
    }
}
