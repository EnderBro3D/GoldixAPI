package mc.enderbro3d.goldixapi.data;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import mc.enderbro3d.goldixapi.data.types.GameType;
import mc.enderbro3d.goldixapi.data.types.GlobalValueType;
import mc.enderbro3d.goldixapi.data.types.MinigameValueType;
import mc.enderbro3d.goldixapi.data.types.ValueType;
import mc.enderbro3d.goldixapi.database.MySQLWorker;
import mc.enderbro3d.goldixapi.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Data {
    private Table<GameType, ValueType, Value> table = HashBasedTable.create();

    private String name;

    public void setData(GameType game, ValueType type, Value val) {
        table.put(game, type, val);
    }

    public Value getData(GameType game, ValueType type) {
        return table.get(game, type);
    }

    public Data(User user) {
        this(user.getName());
    }

    public Data(String name) {
        this.name = name;
        load();
    }

    public void save() {
        int bw_kills = getData(GameType.BEDWARS, MinigameValueType.KILLS).integerValue();
        int bw_wins = getData(GameType.BEDWARS, MinigameValueType.WINS).integerValue();
        int bw_deaths = getData(GameType.BEDWARS, MinigameValueType.DEATHS).integerValue();
        int bw_played = getData(GameType.BEDWARS, MinigameValueType.PLAYED).integerValue();

        MySQLWorker.execute("INSERT INTO `goldix`.`bedwars_stats`(`name`, `kills`, `deaths`, `wins`, `played`) VALUES(?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE `played`=?, `kills`=?, `deaths`=?, `wins`=?", name, bw_kills, bw_deaths, bw_wins, bw_played,
                bw_played, bw_kills, bw_deaths, bw_wins);

        int sw_kills = getData(GameType.SKYWARS, MinigameValueType.KILLS).integerValue();
        int sw_wins = getData(GameType.SKYWARS, MinigameValueType.WINS).integerValue();
        int sw_deaths = getData(GameType.SKYWARS, MinigameValueType.DEATHS).integerValue();
        int sw_played = getData(GameType.SKYWARS, MinigameValueType.PLAYED).integerValue();

        MySQLWorker.execute("INSERT INTO `goldix`.`skywars_stats`(`name`, `kills`, `deaths`, `wins`, `played`) VALUES(?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE `played`=?, `kills`=?, `deaths`=?, `wins`=?", name, sw_kills, sw_deaths, sw_wins, sw_played,
                sw_played, sw_kills, sw_deaths, sw_wins);

        String group = getData(GameType.GLOBAL, GlobalValueType.GROUP).stringValue();
        int balance = getData(GameType.GLOBAL, GlobalValueType.BALANCE).integerValue();
        int level = getData(GameType.GLOBAL, GlobalValueType.LEVEL).integerValue();
        int exp = getData(GameType.GLOBAL, GlobalValueType.EXP).integerValue();

        MySQLWorker.execute("INSERT INTO `goldix`.`userdata`(`name`, `group`, `level`, `exp`, `balance`) VALUES(?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE `group`=?, `level`=?, `exp`=?, `balance`=?", name, group, level, exp, balance,
                group, level, exp, balance);
    }

    public void load() {
        table.clear();

        try {
            //Bedwars Loading
            try (ResultSet rs = MySQLWorker.executeQuery("SELECT * FROM `goldix`.`bedwars_stats` WHERE `name`=?", name)) {
                if (MySQLWorker.next(rs)) {
                    table.put(GameType.BEDWARS, MinigameValueType.KILLS, MySQLWorker.get(rs, "kills"));
                    table.put(GameType.BEDWARS, MinigameValueType.DEATHS, MySQLWorker.get(rs, "deaths"));
                    table.put(GameType.BEDWARS, MinigameValueType.WINS, MySQLWorker.get(rs, "wins"));
                    table.put(GameType.BEDWARS, MinigameValueType.PLAYED, MySQLWorker.get(rs, "played"));
                } else {
                    table.put(GameType.BEDWARS, MinigameValueType.KILLS, new Value(0));
                    table.put(GameType.BEDWARS, MinigameValueType.DEATHS, new Value(0));
                    table.put(GameType.BEDWARS, MinigameValueType.WINS, new Value(0));
                    table.put(GameType.BEDWARS, MinigameValueType.PLAYED, new Value(0));
                }
            }

            //Skywars Loading
            try (ResultSet rs = MySQLWorker.executeQuery("SELECT * FROM `goldix`.`skywars_stats` WHERE `name`=?", name)) {
                if (MySQLWorker.next(rs)) {
                    table.put(GameType.SKYWARS, MinigameValueType.KILLS, MySQLWorker.get(rs, "kills"));
                    table.put(GameType.SKYWARS, MinigameValueType.DEATHS, MySQLWorker.get(rs, "deaths"));
                    table.put(GameType.SKYWARS, MinigameValueType.WINS, MySQLWorker.get(rs, "wins"));
                    table.put(GameType.SKYWARS, MinigameValueType.PLAYED, MySQLWorker.get(rs, "played"));
                } else {
                    table.put(GameType.SKYWARS, MinigameValueType.KILLS, new Value(0));
                    table.put(GameType.SKYWARS, MinigameValueType.DEATHS, new Value(0));
                    table.put(GameType.SKYWARS, MinigameValueType.WINS, new Value(0));
                    table.put(GameType.SKYWARS, MinigameValueType.PLAYED, new Value(0));
                }
            }

            //UserData loading
            try (ResultSet rs = MySQLWorker.executeQuery("SELECT * FROM `goldix`.`userdata` WHERE `name`=?", name)) {
                if (MySQLWorker.next(rs)) {
                    table.put(GameType.GLOBAL, GlobalValueType.GROUP, MySQLWorker.get(rs, "group"));
                    table.put(GameType.GLOBAL, GlobalValueType.LEVEL, MySQLWorker.get(rs, "level"));
                    table.put(GameType.GLOBAL, GlobalValueType.EXP, MySQLWorker.get(rs, "exp"));
                    table.put(GameType.GLOBAL, GlobalValueType.BALANCE, MySQLWorker.get(rs, "balance"));
                } else {
                    table.put(GameType.GLOBAL, GlobalValueType.GROUP, new Value("default"));
                    table.put(GameType.GLOBAL, GlobalValueType.LEVEL, new Value(0));
                    table.put(GameType.GLOBAL, GlobalValueType.EXP, new Value(0));
                    table.put(GameType.GLOBAL, GlobalValueType.BALANCE, new Value(0));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
