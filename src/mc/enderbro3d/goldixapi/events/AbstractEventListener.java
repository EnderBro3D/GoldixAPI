package mc.enderbro3d.goldixapi.events;

import mc.enderbro3d.goldixapi.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public abstract class AbstractEventListener implements Listener {
    public AbstractEventListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }
}
