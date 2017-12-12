package mc.enderbro3d.goldixapi;

import mc.enderbro3d.goldixapi.commands.LanguageCommand;
import mc.enderbro3d.goldixapi.commands.PermissionsCommand;
import mc.enderbro3d.goldixapi.commands.TestCommand;
import mc.enderbro3d.goldixapi.data.MySQLWorker;
import mc.enderbro3d.goldixapi.services.AnticheatService;
import mc.enderbro3d.goldixapi.services.UserService;
import mc.enderbro3d.goldixapi.services.commands.CommandService;
import mc.enderbro3d.goldixapi.services.languages.LanguageService;
import mc.enderbro3d.goldixapi.utils.world.WorldUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;

public class Main extends JavaPlugin {

    private static Main instance;

    private UserService userService;

    private AnticheatService anticheatService;

    public static Main getInstance() {
        return instance;
    }

    public Main() {
        instance = this;
        userService = new UserService();
        anticheatService = new AnticheatService();
    }



    @Override
    public void onDisable() {

        anticheatService.disableService();
        userService.disableService();

        LanguageService.getData().save();
        MySQLWorker.disconnect();
    }

    @Override
    public void onEnable() {
        MySQLWorker.connect("root", "", "goldix", "localhost", 3306);
        
        userService.enableService();
        anticheatService.enableService();

        CommandService.registerCommand(new LanguageCommand());
        CommandService.registerCommand(new TestCommand());
        CommandService.registerCommand(new PermissionsCommand());
        WorldUtil.clearAll();
    }


}