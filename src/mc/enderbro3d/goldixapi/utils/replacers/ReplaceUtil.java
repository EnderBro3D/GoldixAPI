package mc.enderbro3d.goldixapi.utils.replacers;

import org.bukkit.entity.Player;

public class ReplaceUtil {
    private static StandardWordReplacer replacer = new StandardWordReplacer();
    public static String replace(String s, Player p, Replacer rep) {
        return rep.replace(s, p);
    }

    public static String replace(String s, Player p) {
        return replacer.replace(s, p);
    }

    public static StandardWordReplacer getReplacer() {
        return replacer;
    }
}
