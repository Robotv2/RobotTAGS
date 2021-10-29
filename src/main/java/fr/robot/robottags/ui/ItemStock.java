package fr.robot.robottags.ui;

import com.google.common.base.Strings;
import fr.robot.robottags.manager.ConfigManager;
import fr.robot.robottags.object.Tag;
import fr.robot.robottags.utility.ItemAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ItemStock {

    private static final HashMap<ItemStockType, ItemStack> BUILD_ITEMS = new HashMap<>();

    public enum ItemStockType {
        CHANGE_ITEM,NEXT_PAGE, PREVIOUS_PAGE;
    }

    public static void init() {
        for(ItemStockType type : ItemStockType.values()) {
            initItem(type);
        }
    }

    private static void initItem(ItemStockType type) {
        String id = "";

        switch (type) {
            case CHANGE_ITEM:
                id = "change-item";
                break;
            case NEXT_PAGE:
                id = "next-page";
                break;
            case PREVIOUS_PAGE:
                id = "previous-page";
                break;
        }

        if(Strings.isNullOrEmpty(id)) return;

        ItemAPI.ItemBuilder builder;

        String NAME = ConfigManager.getConfig().get().getString("GUI.items." + id + ".display");
        List<String> LORE = ConfigManager.getConfig().get().getStringList("GUI.items." + id + ".lore");
        String MATERIAL_OR_HEAD = ConfigManager.getConfig().get().getString("GUI.items." + id + ".material");

        assert MATERIAL_OR_HEAD != null;

        if(MATERIAL_OR_HEAD.startsWith("head-")) {
            ItemStack head = ItemAPI.createSkull(MATERIAL_OR_HEAD.replace("head-", ""));
            builder = ItemAPI.toBuilder(head);
        } else {
            builder = new ItemAPI.ItemBuilder().setType(Material.valueOf(MATERIAL_OR_HEAD.toUpperCase()));
        }

        builder.setName(NAME).setLore(LORE).setKey(id, 1).addFlags(ItemFlag.HIDE_ATTRIBUTES).build();
        BUILD_ITEMS.put(type, builder.build());
    }


    public static ItemStack getChangeItem(Tag tag) {
        ItemStack item = BUILD_ITEMS.get(ItemStockType.CHANGE_ITEM);
        ItemAPI.ItemBuilder builder = ItemAPI.toBuilder(item);

        assert item.getItemMeta() != null;

        String title = item.getItemMeta().getDisplayName();
        List<String> lore = item.getItemMeta().getLore();

        title = title.replace("%tag%", tag.getDisplay()).replace("%tag-id%", tag.getID());
        builder.setName(title);

        if(lore != null && !lore.isEmpty()) {
            lore = lore.stream().map(line -> line.replace("%tag%", tag.getDisplay()).replace("%tag-id%", tag.getID())).collect(Collectors.toList());
            builder.setLore(lore);
        }

        return builder.build();
    }

    public static ItemStack getNextPageItem() {
        return BUILD_ITEMS.get(ItemStockType.NEXT_PAGE);
    }

    public static ItemStack getPreviousPageItem() {
        return BUILD_ITEMS.get(ItemStockType.PREVIOUS_PAGE);
    }
}
