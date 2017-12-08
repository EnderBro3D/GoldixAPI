package mc.enderbro3d.goldixapi.services;

import mc.enderbro3d.goldixapi.data.Data;
import mc.enderbro3d.goldixapi.events.AbstractEventListener;
import mc.enderbro3d.goldixapi.CustomPermissible;
import mc.enderbro3d.goldixapi.services.languages.LanguageService;
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


    /**
     * Добавить пользователя
     * @param user Пользователь
     */
    public static void addUser(User user) {
        users.put(user.getName().toLowerCase(), user);
    }

    /**
     * Загрузить асинхронно данные пользователя
     * @param u Пользователь
     * @param load Функция, которая выполняется когда данные успешно загружены
     */
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

    /**
     * Загрузить синхронно данные пользователя
     * @param u Пользователь
     * @param load Функция, которая выполняется когда данные успешно загружены
     */
    public static void loadSynchronousData(User u, Consumer<Data> load) {
        u.load();
        load.accept(u.getData());
    }

    /**
     * Синхронно сохранить данные пользователя
     * @param u Пользователь
     */
    public static void saveSynchronousData(User u) {
        u.save();
    }

    /**
     * Асинхронно сохранить данные пользователя
     * @param u Пользователь
     */
    public static void saveAsynchronousData(User u) {
        try {
            Runnable r = u::save;
            Executors.newSingleThreadExecutor()
                    .execute(r);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Сделать все важные функции для игрока, чтобы он полноценно стал пользователем
     * @param p Игрок
     */
    public static void injectPlayer(Player p) {
        User user = new GoldixUser(p);
        addUser(user);
        p.sendMessage("§aData §8| §fЗагружаем твои данные...");

        loadAsynchronousData(user, (data) -> {
            new CustomPermissible(user).inject(p);
            LanguageService.sendMessage(p, "§aData §8| ", "dataloaded");
        });
    }

    /**
     * Удалить пользователя по его имени
     * @param s Имя пользователя
     */
    public static void removeUser(String s) {
        users.remove(s.toLowerCase());
    }

    /**
     * Возвращает пользователя
     * @param s Имя
     * @return Пользователь
     */
    public static User getUser(String s) {
        return users.get(s.toLowerCase());
    }

    /**
     * Возвращает пользователя из игрока
     * @param p Игрок
     * @return Пользователь
     */
    public static User getUser(Player p) {
        return getUser(p.getName());
    }

    /**
     * Возвращает оффлайн пользователя
     * @param s Имя
     * @return Оффлайн пользователь
     */
    public static User getOffline(String s) {
        if(Bukkit.getPlayer(s) != null) return getUser(s);
        return new OfflineUser(s);
    }

    /**
     * Получает список всех пользователей
     * @return Список пользователей
     */
    public static Map<String, User> getUsers() {
        return users;
    }

    @Override
    public void enableService() {
        Bukkit.getOnlinePlayers()
                .forEach(UserService::injectPlayer);

        userListener = new UserServiceListener();
    }

    @Override
    public void disableService() {
        HandlerList.unregisterAll(userListener);
        userListener = null;
    }

    public class UserServiceListener extends AbstractEventListener {
        @EventHandler
        public void on(PlayerLoginEvent e) {
            injectPlayer(e.getPlayer());
        }


        @EventHandler
        public void on(PlayerQuitEvent e) {
            User user = getUser(e.getPlayer().getName());
            saveAsynchronousData(user);
            removeUser(user.getName());
        }
    }
}
