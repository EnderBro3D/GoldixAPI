package mc.enderbro3d.goldixapi.frame;

import org.bukkit.inventory.Inventory;

public interface DrawableComponent extends SlotableComponent {

    /**
     * Отрисовка в инвентаре
     * @param inv Инвентарь
     */
    public void draw(Inventory inv);
}
