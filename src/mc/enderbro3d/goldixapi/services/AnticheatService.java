package mc.enderbro3d.goldixapi.services;

import com.comphenix.packetwrapper.WrapperPlayServerEntityEquipment;
import com.google.common.collect.Maps;
import mc.enderbro3d.goldixapi.events.AbstractEventListener;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;

public class AnticheatService implements Service {

    private static final ItemStack BLANK = new ItemStack(Material.AIR);
    private static final HashSet<Player> checks = new HashSet<>();
    private static final ItemStack FAKE = new ItemStack(Material.DIAMOND_CHESTPLATE);

    private static final HashMap<Player, ItemStack> savedChestplates = Maps.newHashMap();
    private static final HashMap<Player, HashMap<Integer, ItemStack>> savedItems = Maps.newHashMap();

    private AnticheatThread thread;

    private static String FAKE_NAME;

    private static void sendHide(Player p) {
        PlayerInventory inv = p.getInventory();
        ItemStack chest = inv.getChestplate();
        if(chest != null) savedChestplates.put(p, chest);

        WrapperPlayServerEntityEquipment e = new WrapperPlayServerEntityEquipment();
        e.setSlot(3);
        e.setItem(BLANK);
        e.setEntityID(p.getEntityId());
        e.sendPacket(p);
    }

    private static void sendShow(Player p) {
        if (savedChestplates.containsKey(p)) {
            WrapperPlayServerEntityEquipment e = new WrapperPlayServerEntityEquipment();
            e.setSlot(3);
            e.setItem(savedChestplates.get(p));
            e.setEntityID(p.getEntityId());
            e.sendPacket(p);

            savedChestplates.remove(p);
        }
    }



    static {
        ItemMeta meta = FAKE.getItemMeta();
        meta.setDisplayName(FAKE_NAME = "Anticheat Goldix");
        List<String> lore = Arrays.asList(RandomStringUtils.randomAlphanumeric(8),
                RandomStringUtils.randomAlphanumeric(8),
                RandomStringUtils.randomAlphanumeric(8));
        meta.setLore(lore);
        FAKE.setItemMeta(meta);

        new AbstractEventListener() {
            @EventHandler
            public void on(InventoryClickEvent e) {
                Player p = (Player) e.getWhoClicked();

                System.out.println(checks.contains(p));
                if(!checks.contains(p)) return;
                ItemStack stack = e.getCurrentItem();
                if(stack != null && stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()
                        && stack.getItemMeta().getDisplayName().equals(FAKE_NAME)) {
                    PlayerInventory inv = p.getInventory();

                    savedItems.get(p).forEach(inv::setItem);

                    e.setCancelled(true);

                    p.kickPlayer("Autoarmor detected");

                    savedItems.remove(p);
                    savedChestplates.remove(p);
                }
            }

            @EventHandler
            public void on(PlayerQuitEvent e) {
                Player p = e.getPlayer();
                PlayerInventory inv = p.getInventory();
                if(checks.contains(p)) {
                    inv.setItem(9, BLANK);
                    if(savedItems.containsKey(p)) savedItems.get(p).forEach(inv::setItem);
                    checks.remove(p);
                }
            }

            @EventHandler
            public void on(PlayerDropItemEvent e) {
                ItemStack stack = e.getItemDrop().getItemStack();
                if(stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()
                        && stack.getItemMeta().getDisplayName().equals(FAKE_NAME)) {
                    e.setCancelled(true);
                }
            }
        };

    }


    public void checkAutoarmor(Player player) {

        Runnable r = () -> {

            PlayerInventory inv = player.getInventory();

            checks.add(player);

            HashMap<Integer, ItemStack> stacks = new HashMap<>();
            //Hiding chestplate
            sendHide(player);
            //Saving 9 slot
            ItemStack slot9 = inv.getItem(9);
            if (slot9 != null) {
                if (!(slot9.hasItemMeta() && slot9.getItemMeta().hasDisplayName()
                        && slot9.getItemMeta().getDisplayName().equals(FAKE_NAME))) stacks.put(9, slot9);
            }

            //Removing all chestplates
            for (int i = 0; i < inv.getSize(); i++) {
                ItemStack stack = inv.getItem(i);
                if (stack == null) continue;
                if (!stack.getType().toString().endsWith("CHESTPLATE")) continue;
                stacks.put(i, stack);
                inv.setItem(i, BLANK);
            }
            //Adding fake chestplate
            inv.setItem(9, FAKE);
            savedItems.put(player, stacks);


            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Removing later
            if (slot9 != null) inv.setItem(9, slot9);
            else inv.setItem(9, BLANK);
            checks.remove(player);
            if (savedItems.containsKey(player)) savedItems.get(player).forEach(inv::setItem);
            sendShow(player);
            savedItems.remove(player);
        };

        Executors.newSingleThreadExecutor().execute(r);
    }

    


    @Override
    public void enableService() {
        if(thread != null) disableService();
        thread = new AnticheatThread();
        thread.start();
    }

    @Override
    public void disableService() {
        thread.stopAnticheat();
    }

    private class AnticheatThread extends Thread {

        private boolean run;

        public void stopAnticheat() {
            run = false;
        }

        public void run() {
            run = true;
            while(run) {
                Bukkit.getOnlinePlayers()
                        .forEach(AnticheatService.this::checkAutoarmor);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
