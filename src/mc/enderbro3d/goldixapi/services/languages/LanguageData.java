package mc.enderbro3d.goldixapi.services.languages;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import mc.enderbro3d.goldixapi.data.MySQLWorker;
import mc.enderbro3d.goldixapi.utils.StringFormater;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class LanguageData {
    private static HashMap<Language, Map<String, String>> messages = new HashMap<>();

    public String getMessage(Language lang, String key) {
        return messages.get(lang).get(key);
    }

    public void setMessage(Language lang, String key, String val) {
        Map<String, String> languageMessages = messages.get(lang);
        languageMessages.put(key, val);
        messages.put(lang, languageMessages);
    }

    public void unload() {
        messages.clear();
    }

    public void save(boolean async) {
        System.out.println("Saving language data...");
        StringFormater.sendMessage(false,"Saving props: async=%b", async);
        Runnable r = () -> {
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
            System.out.println("Saving successfully completed.");
        };

        if(async) Executors.newSingleThreadExecutor().execute(r);
        else r.run();
    }
    public void load(boolean async) {
        System.out.println("Loading language data...");
        StringFormater.sendMessage(false,"Loading props: async=%b", async);
        unload();

        Runnable r = () -> {
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
            System.out.println("Loading successfully completed.");
        };

        if(!async) r.run();
        else Executors.newSingleThreadExecutor().execute(r);
    }
}
