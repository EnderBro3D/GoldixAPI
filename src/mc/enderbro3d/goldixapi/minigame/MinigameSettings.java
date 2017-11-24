package mc.enderbro3d.goldixapi.minigame;

import mc.enderbro3d.goldixapi.data.Value;

import java.util.concurrent.ConcurrentHashMap;

public class MinigameSettings {
    private ConcurrentHashMap<String, Value> settings = new ConcurrentHashMap<>();

    /**
     * Добавляет новую настройку
     * @param s - Ключ, которому будет присвоено значение
     * @param val - Значение
     */
    public void addSetting(String s, Object val) {
        settings.put(s, new Value(val));
    }

    /**
     * Получает значение настройки
     * @param s - Ключ из которого будет взято значение
     * @return Значение
     */
    public Value getSetting(String s) {
        return settings.get(s);
    }

    /**
     * Удаляет настройку
     * @param s - Настройка
     */
    public void removeSetting(String s) {
        settings.remove(s);
    }
}
