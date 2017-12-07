package mc.enderbro3d.goldixapi.data;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import mc.enderbro3d.goldixapi.data.types.EverywhereValueType;
import mc.enderbro3d.goldixapi.data.types.KeyType;
import mc.enderbro3d.goldixapi.data.types.MinigameValueType;
import mc.enderbro3d.goldixapi.data.types.ValueType;
import mc.enderbro3d.goldixapi.data.values.Value;
import mc.enderbro3d.goldixapi.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Data {
    private Table<KeyType, ValueType, Value> table = HashBasedTable.create();

    private String name;

    /**
     * Устанавливает значение данных
     * @param key Ключ сервера
     * @param type Значение, которое вы хотите установить
     * @param val Значение
     */
    public void setData(KeyType key, ValueType type, Value val) {
        table.put(key, type, val);
    }

    /**
     *
     * @param key Ключ
     * @param type Значение
     * @return Значение
     */
    public Value getData(KeyType key, ValueType type) {
        return table.get(key, type);
    }

    public Data(User user) {
        this(user.getName());
    }

    public Data(String name) {
        this.name = name;
    }

    /**
     * Сохраняет данные
     */
    public void save() {
        int bw_kills = getData(KeyType.BEDWARS, MinigameValueType.KILLS).integerValue();
        int bw_wins = getData(KeyType.BEDWARS, MinigameValueType.WINS).integerValue();
        int bw_deaths = getData(KeyType.BEDWARS, MinigameValueType.DEATHS).integerValue();
        int bw_played = getData(KeyType.BEDWARS, MinigameValueType.PLAYED).integerValue();

        MySQLWorker.execute(true, "INSERT INTO `goldix`.`bedwars_stats`(`name`, `kills`, `deaths`, `wins`, `played`) VALUES(?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE `played`=?, `kills`=?, `deaths`=?, `wins`=?", name, bw_kills, bw_deaths, bw_wins, bw_played,
                bw_played, bw_kills, bw_deaths, bw_wins);

        int sw_kills = getData(KeyType.SKYWARS, MinigameValueType.KILLS).integerValue();
        int sw_wins = getData(KeyType.SKYWARS, MinigameValueType.WINS).integerValue();
        int sw_deaths = getData(KeyType.SKYWARS, MinigameValueType.DEATHS).integerValue();
        int sw_played = getData(KeyType.SKYWARS, MinigameValueType.PLAYED).integerValue();

        MySQLWorker.execute(true, "INSERT INTO `goldix`.`skywars_stats`(`name`, `kills`, `deaths`, `wins`, `played`) VALUES(?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE `played`=?, `kills`=?, `deaths`=?, `wins`=?", name, sw_kills, sw_deaths, sw_wins, sw_played,
                sw_played, sw_kills, sw_deaths, sw_wins);

        String group = getData(KeyType.EVERYWHERE, EverywhereValueType.GROUP).stringValue();
        int balance = getData(KeyType.EVERYWHERE, EverywhereValueType.BALANCE).integerValue();
        int level = getData(KeyType.EVERYWHERE, EverywhereValueType.LEVEL).integerValue();
        int exp = getData(KeyType.EVERYWHERE, EverywhereValueType.EXP).integerValue();
        int lang = getData(KeyType.EVERYWHERE, EverywhereValueType.LANG).integerValue();

        MySQLWorker.execute(true, "INSERT INTO `goldix`.`userdata`(`name`, `group`, `level`, `exp`, `balance`, `lang`) VALUES(?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE `group`=?, `level`=?, `exp`=?, `balance`=?, `lang`=?", name, group, level, exp, balance, lang,
                group, level, exp, balance, lang);
    }

    /**
     * Загружает данные
     */
    public void load() {
        table.clear();
        try {
            //Bedwars Loading
            try (ResultSet rs = MySQLWorker.executeQuery(true, "SELECT * FROM `goldix`.`bedwars_stats` WHERE `name`=?", name)) {
                if (MySQLWorker.next(rs)) {
                    table.put(KeyType.BEDWARS, MinigameValueType.KILLS, MySQLWorker.get(rs, "kills"));
                    table.put(KeyType.BEDWARS, MinigameValueType.DEATHS, MySQLWorker.get(rs, "deaths"));
                    table.put(KeyType.BEDWARS, MinigameValueType.WINS, MySQLWorker.get(rs, "wins"));
                    table.put(KeyType.BEDWARS, MinigameValueType.PLAYED, MySQLWorker.get(rs, "played"));
                } else {
                    table.put(KeyType.BEDWARS, MinigameValueType.KILLS, new Value(0));
                    table.put(KeyType.BEDWARS, MinigameValueType.DEATHS, new Value(0));
                    table.put(KeyType.BEDWARS, MinigameValueType.WINS, new Value(0));
                    table.put(KeyType.BEDWARS, MinigameValueType.PLAYED, new Value(0));
                }
            }

            //Skywars Loading
            try (ResultSet rs = MySQLWorker.executeQuery(true, "SELECT * FROM `goldix`.`skywars_stats` WHERE `name`=?", name)) {
                if (MySQLWorker.next(rs)) {
                    table.put(KeyType.SKYWARS, MinigameValueType.KILLS, MySQLWorker.get(rs, "kills"));
                    table.put(KeyType.SKYWARS, MinigameValueType.DEATHS, MySQLWorker.get(rs, "deaths"));
                    table.put(KeyType.SKYWARS, MinigameValueType.WINS, MySQLWorker.get(rs, "wins"));
                    table.put(KeyType.SKYWARS, MinigameValueType.PLAYED, MySQLWorker.get(rs, "played"));
                } else {
                    table.put(KeyType.SKYWARS, MinigameValueType.KILLS, new Value(0));
                    table.put(KeyType.SKYWARS, MinigameValueType.DEATHS, new Value(0));
                    table.put(KeyType.SKYWARS, MinigameValueType.WINS, new Value(0));
                    table.put(KeyType.SKYWARS, MinigameValueType.PLAYED, new Value(0));
                }
            }

            //UserData loading
            try (ResultSet rs = MySQLWorker.executeQuery(true, "SELECT * FROM `goldix`.`userdata` WHERE `name`=?", name)) {
                if (MySQLWorker.next(rs)) {
                    table.put(KeyType.EVERYWHERE, EverywhereValueType.GROUP, MySQLWorker.get(rs, "group"));
                    table.put(KeyType.EVERYWHERE, EverywhereValueType.LEVEL, MySQLWorker.get(rs, "level"));
                    table.put(KeyType.EVERYWHERE, EverywhereValueType.EXP, MySQLWorker.get(rs, "exp"));
                    table.put(KeyType.EVERYWHERE, EverywhereValueType.BALANCE, MySQLWorker.get(rs, "balance"));
                    table.put(KeyType.EVERYWHERE, EverywhereValueType.LANG, MySQLWorker.get(rs, "lang"));

                } else {
                    table.put(KeyType.EVERYWHERE, EverywhereValueType.GROUP, new Value("default"));
                    table.put(KeyType.EVERYWHERE, EverywhereValueType.LEVEL, new Value(0));
                    table.put(KeyType.EVERYWHERE, EverywhereValueType.EXP, new Value(0));
                    table.put(KeyType.EVERYWHERE, EverywhereValueType.BALANCE, new Value(0));
                    table.put(KeyType.EVERYWHERE, EverywhereValueType.LANG, new Value(0));
                }
            }
            save();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
