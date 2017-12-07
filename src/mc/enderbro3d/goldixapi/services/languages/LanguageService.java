package mc.enderbro3d.goldixapi.services.languages;

import mc.enderbro3d.goldixapi.services.Service;
import mc.enderbro3d.goldixapi.services.UserService;
import mc.enderbro3d.goldixapi.user.User;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LanguageService implements Service {

    private static LanguageData data;

    /**
     * Возвращает данные которые на данный момент используются
     * @return Данные
     */
    public static LanguageData getData() {
        return data;
    }

    /**
     * Вовращает переведённое сообщение пользователя
     * @param u Пользователь
     * @param s Ключ
     * @return Значение
     */
    public static String getLocalizedMessage(User u, String s) {
        return getLocalizedMessage(u.getLanguage(), s);
    }

    /**
     * Возращает перевод у данного языка
     * @param lang Язык
     * @param s Ключ
     * @return Значение
     */
    public static String getLocalizedMessage(Language lang, String s) {
        return data.getMessage(lang, s);
    }

    /**
     * Отправляет локализированное сообщение
     * @param sender Отправитель команды
     * @param prefix Префикс
     * @param s Ключ
     */
    public static void sendMessage(CommandSender sender, String prefix, String s) {
        sender.sendMessage(prefix + getLocalizedMessage(sender, s));
    }

    /**
     * Возвращает локализированное сообщение
     * @param sender Отправитель команды
     * @param s Ключ
     * @return Значение
     */
    public static String getLocalizedMessage(CommandSender sender, String s) {
        Language lang = Language.EN;
        if (sender instanceof Player) lang = UserService.getUser((Player) sender).getLanguage();
        String s1 = getLocalizedMessage(lang, s);
        if (s1 == null) s1 = s;
        return ChatColor.translateAlternateColorCodes('&',s1);
    }

    static {
        data = new LanguageData();
        data.load();
    }

    @Override
    public void enableService() {}

    @Override
    public void disableService() {}
}
