package mc.enderbro3d.goldixapi.services;

import mc.enderbro3d.goldixapi.data.Data;
import mc.enderbro3d.goldixapi.events.AbstractEventListener;
import mc.enderbro3d.goldixapi.CustomPermissible;
import mc.enderbro3d.goldixapi.user.GoldixUser;
import mc.enderbro3d.goldixapi.user.OfflineUser;
import mc.enderbro3d.goldixapi.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class UserService implements Service {

    private Listener userListener;
    private static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public static void addUser(User user) {
        users.put(user.getName().toLowerCase(), user);
    }

    public static void loadAsynchronousData(User u, Consumer<Data> load) {
        try {
            Callable<Data> data = () -> {
                u.load();
                return u.getData();
            };

            load.accept(Executors.newSingleThreadExecutor()
                    .submit(data).get());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadSynchronousData(User u, Consumer<Data> load) {
        u.load();
        load.accept(u.getData());
    }

    public static void saveSynchronousData(User u) {
        u.save();
    }

    public static void saveAsynchronousData(User u) {
        try {
            Runnable r = u::save;
            Executors.newSingleThreadExecutor()
                    .execute(r);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void injectPlayer(Player p) {
        User user = new GoldixUser(p);
        addUser(user);

        loadAsynchronousData(user, (data) -> new CustomPermissible(user).inject(p));
    }

    public static void removeUser(String s) {
        users.remove(s.toLowerCase());
    }

    public static User getUser(String s) {
        return users.get(s.toLowerCase());
    }

    public static User getUser(Player p) {
        return getUser(p.getName());
    }

    public static User getOffline(String s) {
        if(Bukkit.getPlayer(s) != null) return getUser(s);
        return new OfflineUser(s);
    }

    public static Map<String, User> getUsers() {
        return users;
    }

    @Override
    public void enableService() {
        Bukkit.getOnlinePlayers()
                .forEach(UserService::injectPlayer);

        userListener = new AbstractEventListener() {
            @EventHandler
            public void on(PlayerLoginEvent e) {
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

    @Override
    public void disableService() {
        HandlerList.unregisterAll(userListener);
        userListener = null;
    }
}
