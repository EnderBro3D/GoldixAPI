package mc.enderbro3d.goldixapi.minigame;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ArenaManager {

    private HashMap<String, Arena> arenas = new HashMap<>();

    public Arena getPlayerArena(MinigameUser user) {
        if(user.checkOnline()) return null;

        List<Arena> arenaList = arenas.values().stream()
                .filter(arena -> arena.hasUser(user)).collect(Collectors.toList());

        if(arenaList.size() == 0) return null;
        else if (arenaList.size() > 1) throw new IllegalArgumentException("User cannot be in more than 1 arena");
        else return arenaList.get(0);
    }

    public void createArena(String arena, World world) {
        System.out.println("Creating arena with name " + arena);
        System.out.println("Cleanup the world...");
        world.setAutoSave(false);
        world.setTime(0);
        world.getEntities().forEach(Entity::remove);
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.getWorld().getName().equals(world.getName()))
                .forEach(player -> player.kickPlayer("Cleanuping the world, please rejoin later..."));
        System.out.println("Unloading chunks...");
        for(Chunk c:world.getLoadedChunks()) {
            world.unloadChunk(c);
        }
        System.out.println("Saving world...");
        world.save();

        System.out.println("Unloading world...");
        Bukkit.unloadWorld(world, false);

        System.out.println("Caching world...");

        System.out.println("Loading world...");
        Bukkit.createWorld(new WorldCreator(world.getName()));

    }
}
