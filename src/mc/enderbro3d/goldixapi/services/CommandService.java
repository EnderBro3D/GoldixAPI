package mc.enderbro3d.goldixapi.services;

import mc.enderbro3d.goldixapi.events.AbstractEventListener;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public abstract class CommandService implements Service {

    private String command;
    private String[] aliases;
    private int level;

    public CommandService(String command, int level, String... aliases) {
        this.command = command;
        this.aliases = aliases;
        this.level = level;
    }

    private boolean isCommand(String s, boolean console) {
        boolean isAlias = false;
        for(String s1:aliases) {
            if(!console ? s.equalsIgnoreCase("/" + s1) :
                    s.equalsIgnoreCase(s1)) {
                isAlias = true;
                break;
            }
        }
        return isAlias || (!console ? s.equalsIgnoreCase("/" + command) : s.equalsIgnoreCase(command));
    }

    public String getCommand() {
        return command;
    }

    public String[] getAliases() {
        return aliases;
    }

    public abstract void executeCommand(CommandSender sender, String command, String[] args);

    @Override
    public void registerService() {
        new CommandEventListener();
    }

    private class CommandEventListener extends AbstractEventListener {
        public void applicate(String command, CommandSender sender, Cancellable event, boolean b) {
            String[] commandArray = command.split(" ");
            if(isCommand(commandArray[0], b)) {

                String[] newArray = new String[commandArray.length - 1];
                for(int i = 0;i < newArray.length;i ++) {
                    newArray[i] = commandArray[i + 1];
                }
                executeCommand(sender, commandArray[0], newArray);
                event.setCancelled(true);
            }
        }


        @EventHandler
        public void on(PlayerCommandPreprocessEvent e) {
            if(UserService.getUser(e.getPlayer().getName()).getGroup().getLevel() >= level)
                applicate(e.getMessage(), e.getPlayer(), e, false);
        }

        @EventHandler
        public void on(ServerCommandEvent e) {
            applicate(e.getCommand(), e.getSender(), e, true);
        }
    }
}
