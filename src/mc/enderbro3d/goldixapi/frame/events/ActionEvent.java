package mc.enderbro3d.goldixapi.frame.events;

import mc.enderbro3d.goldixapi.frame.Button;
import org.bukkit.entity.HumanEntity;

public interface ActionEvent {

    /**
     * Выполняется, когда кто-либо кликает в инвентаре
     * @param btn Кнопка
     * @param who Тот, кто кликнул
     */
    public void execute(Button btn, HumanEntity who);
}
