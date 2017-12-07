package mc.enderbro3d.goldixapi.services.languages;

import mc.enderbro3d.goldixapi.data.MySQLWorker;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class LanguageData {
    private static HashMap<Language, Map<String, String>> messages = new HashMap<>();

    /**
     * Получить уже переведённое сообщение
     * @param lang Язык
     * @param key Ключ сообщения
     * @return Сообщение
     */
    public String getMessage(Language lang, String key) {
        return messages.get(lang).get(key);
    }

    /**
     * Установить перевод сообщения
     * @param lang Язык
     * @param key Ключ
     * @param val Значение
     */
    public void setMessage(Language lang, String key, String val) {
        Map<String, String> languageMessages = messages.get(lang);
        languageMessages.put(key, val);
        messages.put(lang, languageMessages);
    }

    /**
     * Удалить все переводы
     */
    public void unload() {
        messages.clear();
    }

    /**
     * Сохраняет все изменения в базу данных
     */
    public void save() {
        //RU
        Map<String, String> ru = messages.get(Language.RU);
        //EN
        Map<String, String> en = messages.get(Language.EN);
        //DE
        Map<String, String> de = messages.get(Language.DE);


        ru.forEach((key, val) -> MySQLWorker.execute(true, "INSERT INTO `language`(`name`, `ru`) VALUES(?, ?)" +
                "ON DUPLICATE KEY UPDATE `ru`=?", key, val, val));
        en.forEach((key, val) -> MySQLWorker.execute(true, "INSERT INTO `language`(`name`, `en`) VALUES(?, ?)" +
                "ON DUPLICATE KEY UPDATE `en`=?", key, val, val));
        de.forEach((key, val) -> MySQLWorker.execute(true, "INSERT INTO `language`(`name`, `de`) VALUES(?, ?)" +
                "ON DUPLICATE KEY UPDATE `de`=?", key, val, val));
    }

    /**
     * Загружает все строки перевода из базы данных
     */
    public void load() {
        unload();

        ResultSet rs = MySQLWorker.executeQuery(true, "SELECT * FROM `language`");

        Map<String, String> ru = new HashMap<>(),
                en = new HashMap<>(),
                de = new HashMap<>();
        while (MySQLWorker.next(rs)) {
            ru.put(MySQLWorker.get(rs, "name").stringValue(), MySQLWorker.get(rs, "ru").stringValue());
            en.put(MySQLWorker.get(rs, "name").stringValue(), MySQLWorker.get(rs, "en").stringValue());
            de.put(MySQLWorker.get(rs, "name").stringValue(), MySQLWorker.get(rs, "de").stringValue());
        }
        messages.put(Language.RU, ru);
        messages.put(Language.EN, en);
        messages.put(Language.DE, de);
    }
}
