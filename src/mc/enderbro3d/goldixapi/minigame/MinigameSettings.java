package mc.enderbro3d.goldixapi.minigame;

import mc.enderbro3d.goldixapi.data.values.IVal;
import mc.enderbro3d.goldixapi.data.values.Value;
import mc.enderbro3d.goldixapi.data.values.ValueArray;

import java.util.concurrent.ConcurrentHashMap;

public class MinigameSettings {
    private ConcurrentHashMap<String, IVal> settings = new ConcurrentHashMap<>();

    public MinigameSettings(int max, int start, int teamCount, String[] teamNames) {
        settings.put("maximum", new Value(max));
        settings.put("start", new Value(start));
        settings.put("teamCount", new Value(teamCount));
        settings.put("teamNames", new ValueArray(teamNames));
        settings.put("gameTime", new Value(0));
    }

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
    public Value getValue(String s) {
        return (Value) settings.get(s);
    }
    public ValueArray getArray(String s) {
        return (ValueArray) settings.get(s);
    }

    /**
     * Удаляет настройку
     * @param s - Настройка
     */
    public void removeSetting(String s) {
        settings.remove(s);
    }
}
