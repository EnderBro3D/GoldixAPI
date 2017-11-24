package mc.enderbro3d.goldixapi.user;

import mc.enderbro3d.goldixapi.data.Data;
import mc.enderbro3d.goldixapi.statistics.UserGroup;

public interface User {

    /**
     * Возвращает группу
     * @return Текущая группа игрока
     */
    public UserGroup getGroup();

    /**
     * Возвращает имя
     * @return Его имя
     */
    public String getName();

    /**
     * Возвращает данные игрока
     * @return Его данные
     */
    public Data getData();

    /**
     * Сохраняет игрока в базе данных
     */
    public void save();

    /**
     * Устанавливает группу
     * @param group - Группа
     */
    public void setGroup(UserGroup group);

    /**
     * Проверяет, есть ли у игрока право
     * @param s - Право
     * @return Имеет ли право игрок или нет. (true или false)
     */
    public boolean hasPermission(String s);


    public void addPermission(String s);

    public void removePermission(String s);
}
