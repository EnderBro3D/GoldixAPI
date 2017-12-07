package mc.enderbro3d.goldixapi.services.commands;

import mc.enderbro3d.goldixapi.events.AbstractEventListener;
import mc.enderbro3d.goldixapi.services.Service;
import mc.enderbro3d.goldixapi.services.UserService;
import mc.enderbro3d.goldixapi.utils.StacktraceWriter;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class CommandService implements Service {

    private static HashMap<String, Command> commands = new HashMap<>();

    /**
     * Получить команду по имени
     * @param name Имя
     * @return Команда
     */
    public static Command getCommandByName(String name) {
        return commands.get(name);
    }

    /**
     * Получить команду по алиасу
     * @param alias Алиас
     * @return Команда
     */
    public static Command getCommandByAlias(String alias) {
        try {
            return commands.values().stream()
                    .filter(command -> command.getAliases().contains(alias.toLowerCase()))
                    .collect(Collectors.toList()).get(0);
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * Регистрирует команду, если алиасы уже используются - удаляет их
     * @param cmd Команда
     */
    public static void registerCommand(Command cmd) {
        if(commands.containsKey(cmd.getName())) return;
        for(String alias:cmd.getAliases()) {
            if(getCommandByAlias(alias) != null)
                cmd.removeAlias(alias);
        }
        commands.put(cmd.getName().toLowerCase(), cmd);
    }

    /**
     * Удаляет команду из списка регистрируемых
     * @param cmd Команда
     */
    public static void unregisterCommand(Command cmd) {
        if(!commands.containsKey(cmd.getName())) return;
        commands.remove(cmd.getName().toLowerCase());
    }

    static {
        new CommandEventListener();
    }

    @Override
    public void enableService() { }

    @Override
    public void disableService() { }
    
    static String toUpper(String s) {
        char first = Character.toUpperCase(s.charAt(0));
        return first + s.substring(1);
    }

    private static class CommandEventListener extends AbstractEventListener {
        public boolean applicate(String command, CommandSender sender, boolean b) {
            try {
                String[] commandArray = command.split(" ");
                String cmd = commandArray[0].toLowerCase();
                if (!b) cmd = cmd.substring(1);
                String[] args = new String[commandArray.length - 1];
                for (int i = 0; i < args.length; i++) {
                    args[i] = commandArray[i + 1];
                }

                Command cmd1 = getCommandByName(cmd);
                if (cmd1 == null) cmd1 = getCommandByAlias(cmd);

                if (cmd1 != null) {
                    if (sender instanceof Player && UserService.getUser((Player) sender).getGroup().getLevel() < cmd1.getLevel()) {
                        sender.sendMessage("§4" + toUpper(cmd) + " §8| §fНедостаточно прав");
                        return true;
                    }
                    cmd1.execute(sender, args);
                    return true;
                }
                return false;
            } catch(Exception e) {
                String s = RandomStringUtils.randomAlphanumeric(8);
                StacktraceWriter.writeToLog(s, e);
                sender.sendMessage("§cОшибка во время выполнения команды\nПередайте администрации ключ: " + s);
                return true;
            }
        }


        @EventHandler(priority = EventPriority.HIGHEST)
        public void on(PlayerCommandPreprocessEvent e) {
            e.setCancelled(applicate(e.getMessage(), e.getPlayer(), false));
        }

        @EventHandler
        public void on(ServerCommandEvent e) {
            e.setCancelled(applicate(e.getCommand(), e.getSender(), true));
        }
    }
}
