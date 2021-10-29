package fr.robot.robottags.ui;

import fr.robot.robottags.Main;
import fr.robot.robottags.manager.ConfigManager;
import fr.robot.robottags.manager.MessageManager;
import fr.robot.robottags.manager.PlayerManager;
import fr.robot.robottags.manager.TagManager;
import fr.robot.robottags.object.Tag;
import fr.robot.robottags.utility.ItemAPI;
import fr.robot.robottags.utility.ui.FillAPI;
import fr.robot.robottags.utility.ui.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class MenuGUI implements GUI {

    private InventoryHolder holder;

    @Override
    public String getName(Player player) {
        return ConfigManager.Settings.GUI_TITLE;
    }

    @Override
    public int getSize() {
        return ConfigManager.Settings.GUI_SLOTS;
    }

    @Override
    public void contents(Player player, Inventory inv) {
        if(ConfigManager.Settings.WANT_EMPLTY_SLOTS_ITEM)
            FillAPI.setupEmptySlots(inv);

        int current = PaginationUtility.getOpenPage(player);
        if(current < 0) current = 1;

        if(ConfigManager.Settings.WANT_NEXT_PAGE && current != ConfigManager.Settings.TOTAL_PAGES)
            inv.setItem(ConfigManager.Settings.NEXT_PAGE_SLOT, ItemStock.getNextPageItem());
        if(ConfigManager.Settings.WANT_PREVIOUS_PAGE && current != 1)
            inv.setItem(ConfigManager.Settings.PREVIOUS_PAGE_SLOT, ItemStock.getPreviousPageItem());


        for(Tag tag : TagManager.getTags()) {
            if(current != tag.getPage()) continue;

            if(!TagManager.hasAccessTo(player, tag) && ConfigManager.Settings.WANT_CHANGE_ITEM) {
                inv.setItem(tag.getSlot(), ItemStock.getChangeItem(tag));
            } else {
                inv.setItem(tag.getSlot(), tag.getItem());
            }
        }

        for(String id : CustomItems.getIds()) {
            if(CustomItems.isEnabled(id))
                inv.setItem(CustomItems.getSlot(id), CustomItems.getItemFor(id, player));
        }
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack current, int slot, ClickType type) {
        if(current == null || current.getItemMeta() == null) return;

        if(ItemAPI.hasKey(current, "tag", PersistentDataType.STRING)) {

            String tagID = (String) ItemAPI.getKeyValue(current, "tag", PersistentDataType.STRING);
            Tag tag = TagManager.getTag(tagID);

            if(!TagManager.hasAccessTo(player, tag)) {
                MessageManager.Message.PLAYER_CANT_ACCESS.send(player);
                return;
            }

            PlayerManager.setTag(player, tag.getID());
            player.closeInventory();

            String message = MessageManager.Message.PLAYER_TAG_CHANGED.getMessage().replace("%tag%", tag.getDisplay());
            Main.sendMessage(player, true, message);

        } else if(ItemAPI.hasKey(current, "next-page", PersistentDataType.INTEGER)) {

            int current_page = PaginationUtility.getOpenPage(player);
            PaginationUtility.setOpenPage(player, current_page + 1);
            this.contents(player, inv);

        } else if(ItemAPI.hasKey(current, "previous-page", PersistentDataType.INTEGER)) {

            int current_page = PaginationUtility.getOpenPage(player);
            PaginationUtility.setOpenPage(player, current_page - 1);
            this.contents(player, inv);

        }

        else if(ItemAPI.hasKey(current, "custom-item", PersistentDataType.STRING)) {

            String itemID = (String) ItemAPI.getKeyValue(current, "custom-item", PersistentDataType.STRING);
            if(type == ClickType.LEFT)
                CustomItems.click(CustomItems.ClickTypeTag.LEFT, itemID, player);
            else if(type == ClickType.RIGHT)
                CustomItems.click(CustomItems.ClickTypeTag.RIGHT, itemID, player);

        }
    }

    @Override
    public void onClose(Player player, InventoryCloseEvent e) {
        PaginationUtility.clearOpenPage(player);
    }

    @Override
    public boolean cancelClose() {
        return false;
    }
}
