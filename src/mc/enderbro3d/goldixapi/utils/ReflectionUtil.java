package mc.enderbro3d.goldixapi.utils;

import java.lang.reflect.Field;

public class ReflectionUtil {
    public static Field getField(Class clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T getField(Object object, String field, Class<T> valueClass) {
        try {
            Class clazz = object.getClass();
            Field field1 = getField(clazz, field);
            T value = (T) field1.get(object);
            return value;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> void setField(Class<T> clazz, T object, String field, Object value) {
        try {
            Field field1 = getField(clazz, field);
            field1.set(object, value);
            field1.setAccessible(false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
