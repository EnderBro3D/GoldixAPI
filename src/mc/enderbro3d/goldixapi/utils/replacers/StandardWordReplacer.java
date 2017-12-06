package mc.enderbro3d.goldixapi.utils.replacers;

import com.google.common.collect.Maps;
import mc.enderbro3d.goldixapi.data.types.KeyType;
import mc.enderbro3d.goldixapi.data.types.EverywhereValueType;
import mc.enderbro3d.goldixapi.services.UserService;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.function.Function;

public class StandardWordReplacer extends AbstractWordReplacer {

    private static HashMap<String, Function<Player, String>> replacements = Maps.newHashMap();

    static {
        replacements.put("@name", player -> player.getName());
        replacements.put("@balance", player -> UserService.getUser(player)
                .getData().getData(KeyType.EVERYWHERE, EverywhereValueType.BALANCE)
                .stringValue());
        replacements.put("@group", player -> UserService.getUser(player).getGroup().getDisplay());
        replacements.put("@world", player -> player.getWorld().getName());
    }

    public StandardWordReplacer() {
        super(replacements);
    }
}
