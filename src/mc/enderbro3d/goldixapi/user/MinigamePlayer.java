package mc.enderbro3d.goldixapi.user;

import mc.enderbro3d.goldixapi.minigame.Arena;
import org.bukkit.entity.Player;

public class MinigamePlayer extends GoldixUser {

    private Arena arena;

    public Arena getArena() {
        return arena;
    }

    public MinigamePlayer(Player player, Arena arena) {
        super(player);
        this.arena = arena;
    }
}
