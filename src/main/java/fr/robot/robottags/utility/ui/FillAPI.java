package fr.robot.robottags.utility.ui;

import fr.robot.robottags.utility.ItemAPI;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class FillAPI {

    private static ItemStack empty;

    public static ItemStack getDefaultEmpty() {
        return new ItemAPI.itemBuilder().setType(Material.BLACK_STAINED_GLASS_PANE)
                .setName("&8").addFlags(ItemFlag.HIDE_ATTRIBUTES).build();
    }

    public static void setEmpty(ItemStack item) {
        empty = item;
    }

    public static ItemStack getEmpty() {
        if(empty == null) {
            empty = getDefaultEmpty();
        }
        return empty;
    }

    public static void setupEmptySlots(Inventory inv) {
        for(int i=0; i<=inv.getSize() - 1; i++) {
            inv.setItem(i, getEmpty());
        }
    }
}
