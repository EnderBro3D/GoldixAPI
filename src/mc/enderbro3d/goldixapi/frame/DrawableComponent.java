package mc.enderbro3d.goldixapi.frame;

import org.bukkit.inventory.Inventory;

public interface DrawableComponent extends SlotableComponent {
    public void draw(Inventory inv);
}
