package mc.enderbro3d.goldixapi.frame.events;

import mc.enderbro3d.goldixapi.frame.Button;
import org.bukkit.entity.HumanEntity;

public interface ActionEvent {
    public void execute(Button btn, HumanEntity who);
}
