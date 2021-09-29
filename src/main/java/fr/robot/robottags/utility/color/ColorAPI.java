package fr.robot.robottags.utility.color;

import org.bukkit.ChatColor;

public class ColorAPI {
    
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String uncolorize(String message) {
        return ChatColor.stripColor(message);
    }
}
