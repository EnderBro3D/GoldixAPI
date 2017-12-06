package mc.enderbro3d.goldixapi.utils.replacers;

import org.bukkit.entity.Player;

public interface Replacer {
    public String replace(String s, Player p);
    public String replaceValue(String s, Player p);
    public boolean canReplace(String s);
}
