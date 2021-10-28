package fr.robot.robottags.manager;

import fr.robot.robottags.utility.config.Config;
import fr.robot.robottags.utility.config.ConfigAPI;

public class ConfigManager {

    private static Config tagConfig;
    private static Config messageConfig;
    private static Config databaseYML;
    private static Config configYML;

    public static String GUI_TITLE;
    public static int GUI_SLOTS;

    public static boolean WANT_CHANGE_ITEM;

    public static void init() {
        tagConfig = ConfigAPI.getConfig("tags");
        tagConfig.setup();
        messageConfig = ConfigAPI.getConfig("messages");
        messageConfig.setup();
        databaseYML = ConfigAPI.getConfig("database");
        databaseYML.setup();
        configYML = ConfigAPI.getConfig("configuration");
        configYML.setup();

        initSettings();
    }

    public static void initSettings() {
        GUI_TITLE = ConfigManager.getConfig().get().getString("GUI.title");
        GUI_SLOTS = ConfigManager.getConfig().get().getInt("GUI.total-slots");

        WANT_CHANGE_ITEM = ConfigManager.getConfig().get().getBoolean("GUI.items.change-item.enabled");
    }

    public static Config getTagConfig() {
        return tagConfig;
    }

    public static Config getMessageConfig() {
        return messageConfig;
    }

    public static Config getDatabase() {
        return databaseYML;
    }

    public static Config getConfig() {
        return configYML;
    }
}
