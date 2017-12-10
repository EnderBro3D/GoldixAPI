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
        WorldUtil.clearAll();
    }
}