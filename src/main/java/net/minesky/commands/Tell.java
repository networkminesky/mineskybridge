package net.minesky.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minesky.hooks.MineSkyVanishHook;
import net.minesky.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static net.minesky.utils.Utils.translateColorsToANSI;

public class Tell implements CommandExecutor, TabCompleter {
    static final TextColor rosaClaro = TextColor.fromHexString("#f77293");
    static final TextColor rosaEscuro = TextColor.fromHexString("#db4275");

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String lbl, @NotNull String @NotNull [] args) {
       if (s instanceof  Player sender) {
           if (args.length < 2) {
               Component message = Component.text("Uso correto: /tell <jogador> <mensagem>", NamedTextColor.RED);
               s.sendMessage(message);
               return true;
           }

           final Player player = Bukkit.getPlayer(args[0]);
           final String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replace('&', '§');

           if (!player.isOnline()) {
               sender.sendMessage(Utils.c("&cEsse jogador não existe ou não está online."));
               return true;
           }

           sendTell(sender, player, message);

       } else {
           s.sendMessage(Utils.c("Não pode executar esse comando."));
       }
       return true;
    }

    static void sendTell(Player source, Player who, String msg) {

        if(who == null || (MineSkyVanishHook.isPlayerVanished(who) && !source.hasPermission("mineskybridge.tell.sendtovanished"))) {
            source.sendMessage(Component.text("Esse jogador não existe ou não está online.", NamedTextColor.RED));
            return;
        }

        if(source.equals(who)) {
            source.sendMessage(Component.text("Você não pode enviar mensagens para você mesmo.", NamedTextColor.RED));
            return;
        }

        final String outrosv = "";

        Component paraQuemTaRecebendo = Component.text("[", rosaClaro)
                .append(Component.text("⏿", rosaClaro, TextDecoration.BOLD))
                .append(Component.text("] ", rosaClaro))
                .append(Component.text(source.getName(), Style.style(rosaClaro, TextDecoration.ITALIC, TextDecoration.BOLD)))
                .append(Component.text(" -> ", NamedTextColor.GRAY))
                .append(Component.text("Você", Style.style(rosaEscuro, TextDecoration.ITALIC)))
                .append(Component.text(":", NamedTextColor.DARK_GRAY))
                .append(Component.text(" "+msg, rosaClaro, TextDecoration.ITALIC))
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("§eClique para responder rapidamente."+outrosv)))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell "+source.getName()+" "));

        Component paraQuemEnviou = Component.text("[", rosaClaro)
                .append(Component.text("⏿", rosaClaro, TextDecoration.BOLD))
                .append(Component.text("] ", rosaClaro))
                .append(Component.text("Você", Style.style(rosaEscuro, TextDecoration.ITALIC)))
                .append(Component.text(" -> ", NamedTextColor.GRAY))
                .append(Component.text(who.getName(), Style.style(rosaClaro, TextDecoration.ITALIC, TextDecoration.BOLD)))
                .append(Component.text(":", NamedTextColor.DARK_GRAY))
                .append(Component.text(" "+msg, rosaClaro, TextDecoration.ITALIC));

        Component spyMessage = Component.text("(Spy)", Style.style(NamedTextColor.DARK_GRAY, TextDecoration.ITALIC))
                .append(Component.text(" ", NamedTextColor.GRAY))
                .append(Component.text("[", rosaClaro))
                .append(Component.text("⏿", rosaClaro, TextDecoration.BOLD))
                .append(Component.text("] ", rosaClaro))
                .append(Component.text(source.getName(), Style.style(rosaEscuro, TextDecoration.ITALIC, TextDecoration.BOLD)))
                .append(Component.text(" -> ", NamedTextColor.GRAY))
                .append(Component.text(who.getName(), Style.style(rosaClaro, TextDecoration.ITALIC, TextDecoration.BOLD)))
                .append(Component.text(":", NamedTextColor.DARK_GRAY))
                .append(Component.text(" " + msg, rosaClaro, TextDecoration.ITALIC))
                .append(Component.text(" ", NamedTextColor.GRAY));


        who.playSound(who, Sound.BLOCK_NOTE_BLOCK_BELL,
                SoundCategory.MASTER, 0.5f, 1.2f);

        source.sendMessage(paraQuemEnviou);
        who.sendMessage(paraQuemTaRecebendo);

        Bukkit.getOnlinePlayers().stream()
                .filter(bs -> bs.hasPermission("mineskybridge.spy"))
                .filter(bs -> !bs.equals(source) && !bs.equals(who))
                .forEach(bs -> bs.sendMessage(spyMessage));
        Bukkit.getConsoleSender().sendMessage(translateColorsToANSI(LegacyComponentSerializer.legacySection().serialize(spyMessage)));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return Utils.onlineWithoutVanished();
    }
}
