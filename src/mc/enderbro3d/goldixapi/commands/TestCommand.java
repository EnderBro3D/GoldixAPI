package mc.enderbro3d.goldixapi.commands;

import mc.enderbro3d.goldixapi.scripting.Script;
import mc.enderbro3d.goldixapi.scripting.ScriptManager;
import mc.enderbro3d.goldixapi.services.commands.Command;
import mc.enderbro3d.goldixapi.utils.WorldUtil;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class TestCommand extends Command {
    public TestCommand() {
        super("test", 0, "t");
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws IOException {
        int key = Integer.parseInt(args[0]);
        switch(key) {
            case 0: {
                int val = Integer.parseInt(args[1]);
                WorldUtil.replaceSelected((Player) sender, Material.getMaterial(val));
                break;
            }
            case 1: {
                WorldUtil.saveToScript((Player) sender, new Script(new File(args[1])), false);
                break;
            }
            case 2: {
                WorldUtil.saveToScript((Player) sender, new Script(new File(args[1])), true);
                break;
            }
            case 3: {
                ScriptManager.runScript(new Script(new File(args[1])), (Player) sender);
                break;
            }

        }
    }

}
