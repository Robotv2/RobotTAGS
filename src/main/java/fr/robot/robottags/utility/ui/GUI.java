package fr.robot.robottags.utility.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface GUI {
    String getName(Player player);
    int getSize();
    void contents(Player player, Inventory inv);
    void onClick(Player player, Inventory inv, ItemStack current, int slot);
    void onClose(Player player, InventoryCloseEvent event);
    boolean cancelClose();
}
