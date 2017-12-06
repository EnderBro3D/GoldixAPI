package mc.enderbro3d.goldixapi.data;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import mc.enderbro3d.goldixapi.data.values.IVal;
import mc.enderbro3d.goldixapi.data.values.Value;
import mc.enderbro3d.goldixapi.data.values.ValueArray;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Settings {
    private Map<String, IVal> settings = new ConcurrentHashMap();
    private boolean addable = true;

    /**
     * Возвращает, можно добавить значение или только изменить
     * @return Разрешение на добавление
     */
    public boolean isAddable() {
        return addable;
    }

    /**
     * Установить разрешение на добавление
     * @param addable Разрешение на добавление
     */
    public void setAddable(boolean addable) {
        this.addable = addable;
    }

    /**
     * Добавить значение
     * @param key Ключ
     * @param value Значение
     */
    public void put(String key, Value value) {
        if(!hasSetting(key) && !addable) return;
        settings.put(key, value);
    }

    /**
     * Добавить массив
     * @param key Ключ
     * @param array Массив
     */
    public void putArray(String key, ValueArray array) {
        if(!hasSetting(key) && !addable) return;
        settings.put(key, array);
    }

    /**
     * Удалить ключ
     * @param key Ключ
     */
    public void remove(String key) {
        settings.remove(key);
    }

    /**
     * Получить значение
     * @param key Ключ
     * @return Значение
     */
    public Value getValue(String key) {
        return (Value) settings.get(key);
    }

    /**
     * Получить массив
     * @param key Ключ
     * @return Массив
     */
    public ValueArray getValueArray(String key) {
        return (ValueArray) settings.get(key);
    }

    /**
     * Удалить все ключи
     */
    public void clear() {
        settings.clear();
    }


    /**
     * Получить все ключи
     * @return Все ключи
     */
    public Set<String> keySet() {
        return settings.keySet();
    }

    /**
     * Получить все значения
     * @return Все значения
     */
    public Set<IVal> valueSet() {
        return Sets.newHashSet(settings.values());
    }

    /**
     * Проверяет, есть ли ключ в настройках
     * @param key Ключ
     * @return Есть ключ или нет
     */
    public boolean hasSetting(String key) {
        return settings.containsKey(key);
    }


    /**
     * Получить все значения и ключи
     * @return Все значения и ключи
     */
    public Map<String, IVal> map() {
        return Maps.newHashMap(settings);
    }

}
