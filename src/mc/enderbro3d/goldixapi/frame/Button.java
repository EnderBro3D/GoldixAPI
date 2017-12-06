package mc.enderbro3d.goldixapi.frame;


import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Button implements DrawableComponent {

    private String name;
    private String text;
    private Component parent;
    private int slot;

    private ItemStack item;

    private List<String> description = new ArrayList<>();

    public Button(String name, String text, Component parent, int slot, Material icon, byte iconData, String... description) {
        this.name = name;
        this.text = text;
        this.parent = parent;
        this.slot = slot;
        this.description = Arrays.asList(description);
        this.item = new ItemStack(icon, 1, iconData);
        updateMeta();
    }

    /**
     * Устанавливает слот кнопки
     * @param slot Слот
     */
    public void setSlot(int slot) {
        int oldSlot = this.slot;
        this.slot = slot;

        ((Frame) parent).inventory.setItem(oldSlot, new ItemStack(Material.AIR));
        draw(((Frame) parent).inventory);
    }


    /**
     * Возвращает описание (он же лор)
     * @return Описание
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * Обновляет мету и отрисовывает в фрейме
     */
    public void updateMeta() {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(text);
        meta.setLore(description);
        item.setItemMeta(meta);

        Frame frame = (Frame) parent;
        draw(frame.inventory);
    }

    /**
     * Устанавливает описание (он же лор)
     * @param description Описание
     */
    public void setDescription(String... description) {
        this.description = Arrays.asList(description);
        updateMeta();
    }

    /**
     * Устанавливает текст кнопки
     * @param s Текст
     */
    public void setText(String s) {
        this.text = s;
        updateMeta();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Component getParent() {
        return parent;
    }

    @Override
    public void draw(Inventory inv) {
        inv.setItem(slot, item);
    }
}
