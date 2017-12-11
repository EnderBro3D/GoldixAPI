package mc.enderbro3d.goldixapi.utils.world;

import mc.enderbro3d.goldixapi.Main;
import mc.enderbro3d.goldixapi.events.AbstractEventListener;
import mc.enderbro3d.goldixapi.scripting.Script;
import mc.enderbro3d.goldixapi.utils.Selection;
import mc.enderbro3d.goldixapi.utils.StringFormater;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class WorldUtil {

    private static ConcurrentHashMap<Player, Set<Block>> selectedBlocks = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Player, Selection> selectedPos = new ConcurrentHashMap<>();
    private static String LINE0 = "CREATE#%d;%d;%d#%d:%d", LINE1 = "WAIT#%d";

    /**
     * Копирует выделенную местность в скрипт файл
     * @param p Игрок, который копирует
     * @param script Скрипт, в который будет записываться информация
     * @param full Переменная, которая обозначает, полностью загружать постройку в скрипт файл или нет. False более быстрее
     * @throws IOException Вызывается, когда не удалось создать файл
     */
    public static void saveToScript(Player p, Script script, boolean full) throws IOException {
        File file = script.getFile();
        if(!file.exists()) file.createNewFile();
        updateBlocks(p, (blocks, coords) -> {
            long time = System.currentTimeMillis();

            try {
                StringBuilder b = new StringBuilder();
                World w = p.getWorld();
                int maxX = coords[0], maxY = coords[1], maxZ = coords[2], minX = coords[3], minY = coords[4], minZ = coords[5];

                int layers = 0;
                for (int yy = minY; yy <= maxY; yy++) {
                    String line0 = String.format(LINE1, 5);

                    for (int xx = minX; xx <= maxX; xx++) {
                        for (int zz = minZ; zz <= maxZ; zz++) {
                            Block block = w.getBlockAt(xx,yy,zz);
                            if(!full && (block.getType() == Material.AIR || block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)) continue;

                            String line = String.format(LINE0, maxX - xx, maxY - yy, maxZ - zz, block.getTypeId(), block.getData());
                            b.append(line + "\n");
                        }
                    }


                    b.append(line0 + "\n");
                    layers++;
                }
                Files.write(file.toPath(), (b.toString()).getBytes(), StandardOpenOption.WRITE);
                StringFormater.sendMessage(p,"§aWorldEditor §8| §fЗаписано в скрипт %d слоёв", layers);

                p.sendMessage(String.valueOf(System.currentTimeMillis() - time));
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Обновляет выделение игрока
     * @param p Игрок
     * @param onUpdate Вызывается, когда обновление закончилось
     */
    public static void updateBlocks(Player p, BiConsumer<Set<Block>, int[]> onUpdate) {
        Runnable r = () -> {
            if (selectedPos.containsKey(p)) {
                Selection select = selectedPos.get(p);
                if(select.getLoc1() == null || select.getLoc2() == null) {
                    p.sendMessage("§aWorldEditor §8| §fТы должен выделить 2 точки");
                    return;
                }
                Location pos0 = select.getLoc1();
                Location pos1 = select.getLoc2();
                int maxX, minX, minY, minZ, maxZ, maxY;

                if (pos1.getX() > pos0.getX()) {
                    maxX = pos1.getBlockX();
                    minX = pos0.getBlockX();
                } else {
                    maxX = pos0.getBlockX();
                    minX = pos1.getBlockX();
                }

                if (pos1.getY() > pos0.getY()) {
                    maxY = pos1.getBlockY();
                    minY = pos0.getBlockY();
                } else {
                    maxY = pos0.getBlockY();
                    minY = pos1.getBlockY();
                }

                if (pos1.getZ() > pos0.getZ()) {
                    maxZ = pos1.getBlockZ();
                    minZ = pos0.getBlockZ();
                } else {
                    maxZ = pos0.getBlockZ();
                    minZ = pos1.getBlockZ();
                }

                Set<Block> blocks = new HashSet<>();
                for (int yy = minY; yy <= maxY; yy++)
                    for (int xx = minX; xx <= maxX; xx++)
                        for (int zz = minZ; zz <= maxZ; zz++) {
                            blocks.add(p.getWorld().getBlockAt(xx, yy, zz));
                        }
                selectedBlocks.put(p, blocks);

                Bukkit.getScheduler().runTask(Main.getInstance(),
                        () -> onUpdate.accept(blocks, new int[]{maxX, maxY, maxZ, minX, minY, minZ}));
            }
        };
        Executors.newSingleThreadExecutor().execute(r);
    }


    /**
     * Заменяет выделенные блоки игрока на m
     * @param p Игрок
     * @param m Материал блока
     */
    public static void replaceSelected(Player p, Material m) {
        updateBlocks(p, (blocks, l) -> {
            p.sendMessage("§aWorldEditor §8| §fЗамена " + blocks.size() + " блоков");
            blocks.forEach(b -> b.setType(m));
        });
    }

    /**
     * Возвращает все выделенные блоки игрока
     * @param p Игрок
     * @return Выделенные блоки
     */
    public static Set<Block> getSelectedBlocks(Player p) {
        return selectedBlocks.get(p);
    }

    /**
     * Очищает выделение игрока
     * @param p Игрок
     */
    public static void removeSelection(Player p) {
        selectedPos.remove(p);
        selectedBlocks.remove(p);
    }

    /**
     * Очищает все выделения
     */
    public static void clearAll() {
        selectedBlocks.clear();
        selectedPos.clear();
    }

    /**
     * Возвращает выделение игрока
     * @param p Игрок
     * @return Выделение
     */
    public static Selection getSelection(Player p) {
        return selectedPos.get(p);
    }

    static {
        new AbstractEventListener() {
            @EventHandler
            public void on(PlayerQuitEvent e) {
                removeSelection(e.getPlayer());
            }

            @EventHandler
            public void on(PlayerInteractEvent e) {
                Player p = e.getPlayer();
                if(e.getItem() == null || e.getItem().getType() != Material.COMMAND) return;
                Location[] loc = new Location[2];
                if(selectedPos.containsKey(p)) {
                    Selection selection = selectedPos.get(p);
                    loc[0] = selection.getLoc1();
                    loc[1] = selection.getLoc2();
                }
                switch(e.getAction()) {
                    case LEFT_CLICK_BLOCK: {
                        p.sendMessage("§aWorldEditor §8| §fТочка 1 выделена");
                        loc[0] = e.getClickedBlock().getLocation();
                        selectedPos.put(p, new Selection(loc[0], loc[1], p));
                        e.setCancelled(true);
                        break;
                    }
                    case RIGHT_CLICK_BLOCK: {
                        p.sendMessage("§aWorldEditor §8| §fТочка 2 выделена");
                        loc[1] = e.getClickedBlock().getLocation();
                        selectedPos.put(p, new Selection(loc[0], loc[1], p));
                        e.setCancelled(true);
                        break;
                    }
                }
            }
        };
    }
}
