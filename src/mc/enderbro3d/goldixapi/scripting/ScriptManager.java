package mc.enderbro3d.goldixapi.scripting;

import mc.enderbro3d.goldixapi.Main;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.Executors;

public class ScriptManager {
    /**
     * Запускает скрипт для игрока
     * @param s Скрипт
     * @param player Игрок
     */
    public static void runScript(Script s, Player player) {
        Runnable r = () -> {
            try {
                LineIterator iter = IOUtils.lineIterator(new FileReader(s.getFile()));
                int lineNum = 0;
                while(iter.hasNext()) {
                    String line = iter.next();
                    String[] split = line.toUpperCase().split("#");
                    switch(split[0]) {
                        case "PLAY":
                            if (split.length < 4) {
                                System.out.println("Cannot parse " + line + " at " + lineNum);
                                System.out.println("Arguments: Play#Instrument#Octave#Tone");
                                System.out.println("For example: Play#Piano#0#b");
                                continue;
                            }

                            player.playNote(player.getLocation(), Instrument.valueOf(split[1]), Note.natural(Integer.parseInt(split[2]), Note.Tone.valueOf(split[3])));
                            break;
                        case "CREATE":
                            if (split.length < 3) {
                                System.out.println("Cannot parse " + line + " at " + lineNum);
                                System.out.println("Arguments: CREATE#x;y;z#ID:subID");
                                System.out.println("For example: CREATE#0;0;0#1:0");
                                continue;
                            }

                            Location playerLoc = player.getLocation();

                            String[] coords = split[1].split(";");
                            String[] id = split[2].split(":");
                            Material mat = Material.getMaterial(Integer.parseInt(id[0]));

                            Block b = player.getWorld().getBlockAt(playerLoc.add(-Integer.parseInt(coords[0]), -Integer.parseInt(coords[1]) + (mat.toString().endsWith("DOOR") ? 1 : 0), -Integer.parseInt(coords[2])));


                            Bukkit.getScheduler().runTask(Main.getInstance(), () -> b.setTypeIdAndData(mat.getId(), Byte.parseByte(id[1]), true));
                            break;
                        case "WAIT":
                            if (split.length < 2) {
                                System.out.println("Cannot parse " + line + " at " + lineNum);
                                System.out.println("Arguments: Wait#ms");
                                System.out.println("For example: Wait#10");
                                continue;
                            }
                            Thread.sleep(Long.parseLong(split[1]));
                            break;
                        case "TITLE":
                            if (split.length < 3) {
                                System.out.println("Cannot parse " + line + " at " + lineNum);
                                System.out.println("Arguments: Title#title#subtitle");
                                System.out.println("For example: Title#Welcome#SkyGames");
                                continue;
                            }
                            player.sendTitle(split[1], split[2]);
                            break;
                        case "MESSAGE":
                            if (split.length < 2) {
                                System.out.println("Cannot parse " + line + " at " + lineNum);
                                System.out.println("Arguments: Message#msg");
                                System.out.println("For example: Message#Hello world");
                                continue;
                            }
                            player.sendMessage(split[1]);
                            break;
                        default:
                            System.out.println("Cannot parse " + line + " at " + lineNum);
                            break;
                    }

                    lineNum++;
                }

            } catch (FileNotFoundException | InterruptedException e) {
                System.out.println("Error while executing the script:\n " + e + ": " + e.getMessage());
            }
        };
        Executors.newSingleThreadExecutor().execute(r);
    }
}
