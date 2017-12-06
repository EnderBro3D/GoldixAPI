package mc.enderbro3d.goldixapi.sideboard;


import com.comphenix.packetwrapper.AbstractPacket;
import com.comphenix.packetwrapper.WrapperPlayServerScoreboardDisplayObjective;
import com.comphenix.packetwrapper.WrapperPlayServerScoreboardObjective;
import com.comphenix.packetwrapper.WrapperPlayServerScoreboardScore;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.google.common.collect.Sets;
import mc.enderbro3d.goldixapi.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Sideboard {

    private String title;
    private String name;

    private Runnable timerUpdateAction;
    private BiConsumer<SideboardSection<String>, Player> initAction;

    public static final int SIDEBAR_POSITION = 1;

    private static ConcurrentHashMap<Sideboard, Set<Player>> visibles = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, SideboardSection<String>> sections = new ConcurrentHashMap<>();


    /**
     * Конструктор скорборда
     * @param title Заглавление
     * @param name Имя скорборда
     * @param timerSpeed Скорость обновления
     * @param initAction Вызывается, когда идёт обновление скорборда [Не по расписанию]
     */
    public Sideboard(String title, String name, int timerSpeed, BiConsumer<SideboardSection<String>, Player> initAction) {
        this.initAction = initAction;
        this.title = title;
        this.name = name;
        visibles.put(this, Sets.newConcurrentHashSet());


        /*
        Создаёт новый ранейбл, который обновляет скорборд раз в @timerSpeed
         */
        if(timerSpeed != 0) {
            BukkitRunnable r = new BukkitRunnable() {
                @Override
                public void run() {
                    timerUpdateAction.run();
                    updateSideboard();
                }
            };
            r.runTaskTimer(Main.getInstance(), 0L, timerSpeed);
        }
    }



    /**
     * Заного отправляет пакет игроку
     */
    public void updateSideboard() {
        visibles.get(this).forEach(this::setSideboard);
    }

    /**
     * Добавляет новую секцию
     * @param section Секция
     */
    public void addSection(SideboardSection<String> section) {
        sections.put(section.getSectionName(), section);
    }

    /**
     * Устанавливает заглавие для скорборда
     * @param s Заглавие
     */
    public void setTitle(String s) {
        title = s;
    }

    /**
     * Устанавливает функцию, которая выполняется во время обновления по расписанию
     * @param r функция
     */
    public void setTimerUpdateAction(Runnable r) {
        this.timerUpdateAction = r;
    }

    /**
     * Удаляет секцию
     * @param section Секция
     */
    public void removeSection(String section) {
        sections.remove(section);
    }

    /**
     * Проверяет текст, если он длиннее i1, то сокращает, иначе
     * оно добавляет пробелы к концу
     * @param s Строка, которую надо проверить
     * @param i1 Нужная длина
     * @return
     */
    public String check(String s, int i1) {
        if(s.length() > i1) s = s.substring(0, i1);
        StringBuilder append = new StringBuilder();
        for(int i = 0;i < i1 - s.length();i++) {
            append.append(" ");
        }
        return s + append;
    }

    /**
     * Создаёт значение в Objective
     * @param objective Объект в скорборде
     * @param scoreName Имя значения
     * @param value Число
     * @return Пакет
     */
    private static AbstractPacket buildScore(String objective, String scoreName, int value) {
        WrapperPlayServerScoreboardScore score = new WrapperPlayServerScoreboardScore();
        score.setValue(value);
        score.setScoreboardAction(EnumWrappers.ScoreboardAction.CHANGE);
        score.setObjectiveName(objective);
        score.setScoreName(scoreName);
        return score;
    }

    /**
     * Создаёт новый объект в скорборде
     * @param objective Имя объекта
     * @param display Выводимое имя
     * @return Пакет
     */
    private static AbstractPacket buildObjective(String objective, String display) {
        WrapperPlayServerScoreboardObjective obj = new WrapperPlayServerScoreboardObjective();
        obj.setDisplayName(display);
        obj.setMode(WrapperPlayServerScoreboardObjective.Mode.ADD_OBJECTIVE);
        obj.setName(objective);
        return obj;
    }

    /**
     * Создаёт пакет показа объекта
     * @param objective Объект
     * @return Пакет
     */
    private static AbstractPacket buildShow(String objective) {
        WrapperPlayServerScoreboardDisplayObjective dobj = new WrapperPlayServerScoreboardDisplayObjective();
        dobj.setPosition(SIDEBAR_POSITION);
        dobj.setScoreName(objective);
        return dobj;
    }

    /**
     * Устанавливает Sideboard игроку
     * @param player Игрок
     */
    public void setSideboard(Player player) {
        buildObjective(name, title).sendPacket(player);

        int sectionPos = 0;
        int linePos = 0;

        for(SideboardSection<String> section:sections.values()) {
            initAction.accept(section, player);

            buildScore(name, (check("§7[" + section.getDisplay() + "§7]", 32)), -(sectionPos + linePos + 1)).sendPacket(player);

            for(SideboardLine<String> line:section.getLines().values()) {
                buildScore(name, check(" " + line.getLine(), 32), -(sectionPos + linePos + 2)).sendPacket(player);
                linePos++;
            }
            sectionPos ++;
        }
        buildShow(name).sendPacket(player);

        Set<Player> players = visibles.get(this);
        players.add(player);
        visibles.put(this, players);
    }

    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }



    @Override
    public String toString() {
        return name;
    }

    /**
     * Ищет секцию по имени и возвращает её
     * @param section Имя секции
     * @return Секция
     */
    public SideboardSection<String> getSection(String section) {
        return sections.get(section);
    }
}
