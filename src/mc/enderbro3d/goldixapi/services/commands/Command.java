package mc.enderbro3d.goldixapi.services.commands;

import com.google.common.collect.Sets;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Set;

public abstract class Command {

    private Set<String> aliases = Sets.newConcurrentHashSet();
    private String name;
    private int level;

    public String toString() {
        return getName();
    }

    /**
     * Добавить алиас
     * @param alias Алиас
     */
    public void addAlias(String alias) {
        aliases.add(alias);
    }

    /**
     * Удалить алиас
     * @param alias Алиас
     */
    public void removeAlias(String alias) {
        aliases.remove(alias);
    }

    /**
     * Получить все алиасы
     * @return Алиасы
     */
    public Set<String> getAliases() {
        return aliases;
    }

    /**
     * Возвращает имя
     * @return Имя
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает уровень доступа, с которого доступна данная команда
     * @return Уровень доступа
     */
    public int getLevel() {
        return level;
    }

    public Command(String name, int level, String... aliases) {
        this.aliases = Sets.newConcurrentHashSet(Arrays.asList(aliases));
        this.name = name;
        this.level = level;
    }

    /**
     * Абстрактный метод, который запускается, когда что-либо пишет команду
     * @param sender Отправитель команды
     * @param args Аргументы
     * @throws Exception Все ошибки
     */
    public abstract void execute(CommandSender sender, String[] args) throws Exception;
}
