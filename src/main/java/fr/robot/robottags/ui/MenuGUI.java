package fr.robot.robottags.ui;

import fr.robot.robottags.Main;
import fr.robot.robottags.manager.ConfigManager;
import fr.robot.robottags.manager.PlayerManager;
import fr.robot.robottags.manager.TagManager;
import fr.robot.robottags.object.Tag;
import fr.robot.robottags.utility.ItemAPI;
import fr.robot.robottags.utility.ui.FillAPI;
import fr.robot.robottags.utility.ui.GUI;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class MenuGUI implements GUI {

    private InventoryHolder holder;

    @Override
    public String getName(Player player) {
        return ConfigManager.getConfig().get().getString("GUI.title");
    }

    @Override
    public int getSize() {
        return ConfigManager.getConfig().get().getInt("GUI.total-slots");
    }

    @Override
    public void contents(Player player, Inventory inv) {
        FillAPI.setupEmptySlots(inv);
        int current = UIutility.getOpenPage(player);
        for(Tag tag : TagManager.getTags()) {
            if(current != tag.getPage()) continue;
            if(!TagManager.hasAccessTo(player, tag)) continue;
            inv.setItem(tag.getSlot(), tag.getItem());
        }
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack current, int slot) {
        if(current == null || current.getItemMeta() == null) return;
        if(!ItemAPI.hasKey(current, "tag", PersistentDataType.STRING)) return;

        NamespacedKey key = new NamespacedKey(Main.getInstance(), "tag");
        String tagID = current.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        Tag tag = TagManager.getTag(tagID);

        if(!TagManager.hasAccessTo(player, tag)) return;

        PlayerManager.setTag(player, tag.getID());
        player.closeInventory();

        //TODO send a message
    }

    @Override
    public InventoryHolder getHolder() {
        return new ;
    }
}
