package mc.enderbro3d.goldixapi.frame;

import mc.enderbro3d.goldixapi.events.AbstractEventListener;
import mc.enderbro3d.goldixapi.frame.events.ActionEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Frame implements Component {

    private String name;

    private String title;

    Inventory inventory;

    private List<ActionEvent> actions = new ArrayList<>();

    private ConcurrentHashMap<Integer, Button> buttons = new ConcurrentHashMap<>();

    private AbstractEventListener listener;

    public void addAction(ActionEvent e) {
        actions.add(e);
    }

    private void check() {
        if(inventory.getViewers().size() == 1 && listener != null) {
            HandlerList.unregisterAll(listener);
            listener = null;
        }
    }

    public void drawAll() {
        buttons.values().forEach(btn -> btn.draw(inventory));
    }

    public void init() { }

    public void addButton(Button button) {
        this.buttons.put(button.getSlot(), button);
    }

    public void openFrame(Player player) {
        if(listener == null) {
            listener = new FrameEventListener();
        }
        player.openInventory(inventory);
    }

    public Frame(String name, String title, int rows) {
        this.name = name;
        this.title = title;
        this.inventory = Bukkit.createInventory(null, rows * 9, title);
        init();
    }

    public Button getButtonAt(int i) {
        return buttons.get(i);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getText() {
        return title;
    }

    @Override
    public Component getParent() {
        return null;
    }


    private class FrameEventListener extends AbstractEventListener {
        @EventHandler
        public void onClose(InventoryCloseEvent e) {
            Inventory inv = e.getInventory();
            if (inv.getTitle().equals(Frame.this.inventory.getTitle())) {
                check();
            }
        }

        @EventHandler
        public void onClick(InventoryClickEvent e) {
            Inventory inv = e.getInventory();
            if (inv.getTitle().equals(Frame.this.inventory.getTitle())) {

                Button button = getButtonAt(e.getSlot());
                if(button != null) actions.forEach(action -> action.execute(button, e.getWhoClicked()));
                e.setCancelled(true);
            }
        }

        @EventHandler
        public void onDrag(InventoryDragEvent e) {
            Inventory inv = e.getInventory();
            if (inv.getTitle().equals(Frame.this.inventory.getTitle())) {
                e.setCancelled(true);
            }
        }
    }
}
