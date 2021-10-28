package fr.robot.robottags.ui;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class PaginationUtility {

    private static final HashMap<Player, Integer> pages = new HashMap<>();

    public static int getOpenPage(Player player) {
        if(pages.containsKey(player))
            return pages.get(player);
        return 1;
    }

    public static void setOpenPage(Player player, int page) {
        pages.put(player, page);
    }

    public static void clearOpenPage(Player player) {
        pages.remove(player);
    }
}
