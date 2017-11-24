package mc.enderbro3d.goldixapi.services;

import mc.enderbro3d.goldixapi.events.AbstractEventListener;
import mc.enderbro3d.goldixapi.permissions.CustomPermissible;
import mc.enderbro3d.goldixapi.user.GoldixUser;
import mc.enderbro3d.goldixapi.user.OfflineUser;
import mc.enderbro3d.goldixapi.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;
public class UserService implements Service {

    private static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public static void addUser(User user) {
        users.put(user.getName().toLowerCase(), user);
    }

    public static void injectPlayer(Player player) {
        User user = new GoldixUser(player);
        addUser(user);

        new CustomPermissible(user).inject(player);
    }

    public static void removeUser(String s) {
        users.remove(s.toLowerCase());
    }

    public static User getUser(String s) {
        return users.get(s.toLowerCase());
    }

    public static User getOffline(String s) {
        if(Bukkit.getPlayer(s) != null) return getUser(s);
        return new OfflineUser(s);
    }

    public static Map<String, User> getUsers() {
        return users;
    }

    @Override
    public void registerService() {
        Bukkit.getOnlinePlayers()
                .forEach(UserService::injectPlayer);

        new AbstractEventListener() {
            @EventHandler
            public void on(PlayerJoinEvent e) {
                injectPlayer(e.getPlayer());
            }

            @EventHandler
            public void on(PlayerQuitEvent e) {
                User user = getUser(e.getPlayer().getName());
                user.save();
                removeUser(user.getName());
            }
        };
    }
}
