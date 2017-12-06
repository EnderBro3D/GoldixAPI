package mc.enderbro3d.goldixapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StringFormater {
    public static void sendMessage(Player p, String message, Object... args) {
        sendMessage((CommandSender) p, message, args);
    }

    public static void sendMessage(CommandSender s, String message, Object... args) {
        s.sendMessage(String.format(message, args));
    }

    public static void sendMessage( boolean colored,String message, Object... args) {
        if(colored) sendMessage(Bukkit.getConsoleSender(), message, args);
        else System.out.println(String.format(message, args));
    }
}
