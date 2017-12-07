package mc.enderbro3d.goldixapi;

import mc.enderbro3d.goldixapi.commands.LanguageCommand;
import mc.enderbro3d.goldixapi.commands.PermissionsCommand;
import mc.enderbro3d.goldixapi.commands.TestCommand;
import mc.enderbro3d.goldixapi.data.MySQLWorker;
import mc.enderbro3d.goldixapi.events.AbstractEventListener;
import mc.enderbro3d.goldixapi.services.AnticheatService;
import mc.enderbro3d.goldixapi.services.UserService;
import mc.enderbro3d.goldixapi.services.commands.CommandService;
import mc.enderbro3d.goldixapi.services.languages.LanguageService;
import mc.enderbro3d.goldixapi.sideboard.Sideboard;
import mc.enderbro3d.goldixapi.sideboard.SideboardLine;
import mc.enderbro3d.goldixapi.sideboard.SideboardSection;
import mc.enderbro3d.goldixapi.utils.WorldUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;

public class Main extends JavaPlugin {
    private static Main instance;
    private static AnticheatService anticheat;

    private static Connection connection;

    public static Main getInstance() {
        return instance;
    }

    public static Connection getConnection() {
        return connection;
    }

    public Main() {
        instance = this;
    }


    public static AnticheatService getAnticheat() {
        return anticheat;
    }

    @Override
    public void onDisable() {
        anticheat.disableService();
        LanguageService.getData().save();
        MySQLWorker.disconnect();
    }

    @Override
    public void onEnable() {
        MySQLWorker.connect("root", "", "goldix", "localhost", 3306);
        UserService service1 = new UserService();
        service1.enableService();
        anticheat = new AnticheatService();
        anticheat.enableService();
        CommandService.registerCommand(new LanguageCommand());
        CommandService.registerCommand(new TestCommand());
        CommandService.registerCommand(new PermissionsCommand());


        MySQLWorker.execute(false,"CREATE TABLE IF NOT EXISTS `userdata`(" +
                "`name` VARCHAR(16) NOT NULL," +
                "`level` INT NOT NULL," +
                "`exp` INT NOT NULL," +
                "`balance` INT NOT NULL," +
                "`group` VARCHAR(16) NOT NULL," +
                "`lang` INT NOT NULL," +
                "PRIMARY KEY (`name`));");

        MySQLWorker.execute(false, "CREATE TABLE IF NOT EXISTS `bedwars_stats`(" +
                "`name` VARCHAR(16) NOT NULL," +
                "`kills` INT NOT NULL," +
                "`deaths` INT NOT NULL," +
                "`wins` INT NOT NULL," +
                "`played` VARCHAR(16) NOT NULL," +
                "PRIMARY KEY (`name`));");
        MySQLWorker.execute(false,"CREATE TABLE IF NOT EXISTS `skywars_stats`(" +
                "`name` VARCHAR(16) NOT NULL," +
                "`kills` INT NOT NULL," +
                "`deaths` INT NOT NULL," +
                "`wins` INT NOT NULL," +
                "`played` VARCHAR(16) NOT NULL," +
                "PRIMARY KEY (`name`));");
        MySQLWorker.execute(false, "CREATE TABLE IF NOT EXISTS `groups` (" +
                "`name` VARCHAR(16) NOT NULL," +
                "`prefix` VARCHAR(16) NOT NULL," +
                "`display` VARCHAR(16) NOT NULL," +
                "`level` INT NOT NULL," +
                "`permissions` LONGTEXT NOT NULL," +
                "PRIMARY KEY (`name`));");
        MySQLWorker.execute(false, "CREATE TABLE IF NOT EXISTS `language` (" +
                "`name` VARCHAR(16) NOT NULL," +
                "`ru` VARCHAR(48) DEFAULT 'lang_Err'," +
                "`en` VARCHAR(48) DEFAULT 'lang_Err'," +
                "`de` VARCHAR(48) DEFAULT 'lang_Err'," +
                "PRIMARY KEY (`name`));");
        WorldUtil.clearAll();

        new AbstractEventListener() {
            @EventHandler
            public void test(PlayerJoinEvent e) {
                Sideboard board = new Sideboard("§6Scoreboard §l[Beta]", "testboard", 60 * 20, (sect, p) -> sect.setLine(0, new SideboardLine<>(sect, "§aYour name is " + p.getName())));
                SideboardSection<String> section = new SideboardSection<>(board, "testsection", "§aTesting");
                section.addLine(new SideboardLine<>(section, "§eWelcome"));
                board.addSection(section);
                board.setSideboard(e.getPlayer());
            }
        };
    }
}