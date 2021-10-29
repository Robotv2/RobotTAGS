package fr.robot.robottags.utility.ui;

import fr.robot.robottags.Main;
import fr.robot.robottags.utility.TaskAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

import static fr.robot.robottags.utility.color.ColorAPI.colorize;

public class GuiAPI implements Listener {

    private static final HashMap<Class<? extends GUI>, GUI> menus = new HashMap<>();
    private static final HashMap<UUID, GUI> openedGui = new HashMap<>();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        UUID playerUUID = player.getUniqueId();
        ItemStack item = e.getCurrentItem();

        if(item == null) return;
        if(!openedGui.containsKey(playerUUID)) return;

        e.setCancelled(true);
        GUI menu = openedGui.get(playerUUID);
        menu.onClick(player, e.getInventory(), item, e.getRawSlot(), e.getClick());
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        UUID playerUUID = e.getPlayer().getUniqueId();

        if(openedGui.containsKey(playerUUID)) {

            GUI menu = openedGui.get(playerUUID);
            if(menu.cancelClose()) {
                TaskAPI.runTaskLater(() -> {

                    player.openInventory(e.getInventory());

                }, 3L);
                return;
            }


            menu.onClose(player, e);
            TaskAPI.runTaskLater(() -> {

                if(player.getOpenInventory().getType() != InventoryType.CHEST)
                    openedGui.remove(playerUUID);

            }, 3L);
        }
    }

    public static void addMenu(GUI gui){
        menus.put(gui.getClass(), gui);
    }

    public static void open(Player player, Class<? extends GUI> gClass){
        if(!menus.containsKey(gClass)) {
            Main.getInstance().getLogger().warning(colorize("&cVous devez enregistrer l'inventaire avant de pouvoir l'ouvrir"));
            return;
        }

        GUI menu = menus.get(gClass);
        Inventory inv = Bukkit.createInventory(null, menu.getSize(), colorize(menu.getName(player)));
        menu.contents(player, inv);

        openedGui.put(player.getUniqueId(), menu);

        TaskAPI.runTaskLater(() -> {
            player.openInventory(inv);
        }, 2L);
    }
}
