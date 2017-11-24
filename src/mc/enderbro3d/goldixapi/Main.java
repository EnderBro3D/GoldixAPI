package mc.enderbro3d.goldixapi;

import com.avaje.ebean.validation.Email;
import mc.enderbro3d.goldixapi.database.MySQLWorker;
import mc.enderbro3d.goldixapi.events.AbstractEventListener;
import mc.enderbro3d.goldixapi.frame.Button;
import mc.enderbro3d.goldixapi.frame.Frame;
import mc.enderbro3d.goldixapi.frame.events.ActionEvent;
import mc.enderbro3d.goldixapi.services.CommandService;
import mc.enderbro3d.goldixapi.services.UserService;
import mc.enderbro3d.goldixapi.sideboard.Sideboard;
import mc.enderbro3d.goldixapi.sideboard.SideboardLine;
import mc.enderbro3d.goldixapi.sideboard.SideboardSection;
import mc.enderbro3d.goldixapi.sideboard.SideboardUpdater;
import mc.enderbro3d.goldixapi.statistics.UserGroup;
import mc.enderbro3d.goldixapi.user.User;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    private static Main instance;
    private static Logger logger = Logger.getGlobal();
    private static final String[] animation = new String[] {"§eGoldix", "§6Goldix", "§cGoldix"};

    public static Logger getGlobal() {
        return logger;
    }

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


    @Override
    public void onDisable() {
        MySQLWorker.disconnect();
    }

    @Override
    public void onEnable() {

        MySQLWorker.connect("root", "", "goldix", "localhost", 3306);
        MySQLWorker.execute(
                "CREATE TABLE IF NOT EXISTS `goldix`.`groups` (" +
                        "  `name` VARCHAR(16) NOT NULL," +
                        "  `prefix` VARCHAR(16) NOT NULL," +
                        "  `display` VARCHAR(16) NOT NULL," +
                        "  `level` INT NOT NULL," +
                        "  `permissions` LONGTEXT NOT NULL," +
                        "  PRIMARY KEY (`name`));");
        MySQLWorker.execute(
                "CREATE TABLE IF NOT EXISTS `goldix`.`bedwars_stats` (" +
                        "  `name` VARCHAR(16) NOT NULL," +
                        "  `kills` INT NOT NULL," +
                        "  `deaths` INT NOT NULL," +
                        "  `wins` INT NOT NULL," +
                        "  `played` INT NOT NULL," +
                        "   PRIMARY KEY(`name`));");
        MySQLWorker.execute(
                "CREATE TABLE IF NOT EXISTS `goldix`.`skywars_stats` (" +
                        "  `name` VARCHAR(16) NOT NULL," +
                        "  `kills` INT NOT NULL," +
                        "  `deaths` INT NOT NULL," +
                        "  `wins` INT NOT NULL," +
                        "  `played` INT NOT NULL," +
                        "   PRIMARY KEY (`name`));");
        MySQLWorker.execute(
                "CREATE TABLE IF NOT EXISTS `goldix`.`userdata` (" +
                        "  `name` VARCHAR(16) NOT NULL," +
                        "  `level` INT NOT NULL," +
                        "  `exp` INT NOT NULL," +
                        "  `balance` INT NOT NULL," +
                        "  `group` VARCHAR(16) NOT NULL," +
                        "  PRIMARY KEY (`name`));");

        CommandService service = new CommandService("inv", 0) {
            @Override
            public void executeCommand(CommandSender sender, String command, String[] args) {
                Player player = (Player) sender;
                Frame frame = new Frame("test", "§aTesting", 3);
                frame.addAction((btn, who) -> {
                    btn.setDescription(who.getName());
                    btn.updateMeta();
                });

                frame.addButton(new Button("btn", "§4Test", frame, 13, Material.BED, (byte) 0, "Test"));
                frame.openFrame(player);
            }
        };
        service.registerService();

        UserService service1 = new UserService();
        service1.registerService();

        new AbstractEventListener() {
            @EventHandler
            public void onPlayer(PlayerJoinEvent e) {
                Sideboard sideboard = new Sideboard("Sideboard 0", "test");
                SideboardSection<String> testSection = new SideboardSection<>(sideboard, "test", "Test Section");
                testSection.addLine(new SideboardLine<>(testSection, "Test line [" + e.getPlayer().getName() + "]"));
                sideboard.addSection(testSection);
                sideboard.setSideboard(e.getPlayer());
            }
        };
    }
}