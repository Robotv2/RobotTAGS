package fr.robot.robottags.utility.ui;

import fr.robot.robottags.utility.TaskAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static fr.robot.robottags.utility.color.ColorAPI.colorize;

public class GuiAPI implements Listener {

    private static final Map<Class<? extends GUI>, GUI> menus = new HashMap<>();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        String title = e.getView().getTitle();
        ItemStack current = e.getCurrentItem();

        if(e.getCurrentItem() == null) return;

        menus.values().stream()
                .filter(menu -> title.equals(menu.getName(player)))
                .forEach(menu -> {
                    menu.onClick(player, e.getInventory(), current, e.getRawSlot());
                    e.setCancelled(true);
                });
    }

    public static void addMenu(GUI gui){
        menus.put(gui.getClass(), gui);
    }

    public static void open(Player player, Class<? extends GUI> gClass){
        if(!menus.containsKey(gClass)) return;

        GUI menu = menus.get(gClass);
        Inventory inv = Bukkit.createInventory(null, menu.getSize(), colorize(menu.getName(player)));
        menu.contents(player, inv);

        TaskAPI.runTaskLater(() -> {
            player.openInventory(inv);
        }, 2L);
    }
}
