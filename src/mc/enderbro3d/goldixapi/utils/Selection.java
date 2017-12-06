package mc.enderbro3d.goldixapi.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Selection {
    private Location loc1, loc2;
    private Player p;

    public Selection(Location loc1, Location loc2, Player p) {
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.p = p;
    }

    public Location getLoc1() {
        return loc1;
    }

    public Location getLoc2() {
        return loc2;
    }

    public Player getPlayer() {
        return p;
    }
}
