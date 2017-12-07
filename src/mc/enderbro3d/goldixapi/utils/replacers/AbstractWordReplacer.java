package mc.enderbro3d.goldixapi.utils.replacers;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class AbstractWordReplacer implements Replacer {

    private HashMap<String, Function<Player, String>> replacements = Maps.newHashMap();

    /**
     * Конструктор AbstractWordReplacer
     * @param replacements Список чего заменять
     */
    public AbstractWordReplacer(Map<String, Function<Player, String>> replacements) {
        this.replacements.putAll(replacements);
    }

    @Override
    public String replace(String s, Player p) {
        String[] words = s.split(" ");
        StringBuilder compiled = new StringBuilder();
        for(int i = 0;i < words.length;i++) {
            String editedString = words[i];
            String append = "";
            if(editedString.startsWith("§")) {
                append = editedString.substring(0, 2);
                editedString = editedString.substring(2, editedString.length());
            }
            if(!editedString.startsWith("@")) {
                compiled.append(editedString + " ");
                continue;
            }
            editedString = replaceValue(editedString, p);
            compiled.append(append + editedString + " ");
        }
        return compiled.substring(0, compiled.length()-1);
    }

    @Override
    public String replaceValue(String s, Player p) {
        if(!canReplace(s.toLowerCase())) return s;
        try {
            return replacements.get(s.toLowerCase()).apply(p);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean canReplace(String s) {
        return replacements.containsKey(s);
    }
}
