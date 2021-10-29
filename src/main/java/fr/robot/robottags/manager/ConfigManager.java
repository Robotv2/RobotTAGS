package fr.robot.robottags.manager;

import fr.robot.robottags.utility.config.Config;
import fr.robot.robottags.utility.config.ConfigAPI;

public class ConfigManager {

    private static Config tagConfig;
    private static Config messageConfig;
    private static Config databaseYML;
    private static Config configYML;

    public static void init() {
        tagConfig = ConfigAPI.getConfig("tags");
        tagConfig.setup();
        messageConfig = ConfigAPI.getConfig("messages");
        messageConfig.setup();
        databaseYML = ConfigAPI.getConfig("database");
        databaseYML.setup();
        configYML = ConfigAPI.getConfig("configuration");
        configYML.setup();

        Settings.initSettings();
    }

    public static class Settings {

        public static String GUI_TITLE;
        public static int GUI_SLOTS;

        public static boolean WANT_CHANGE_ITEM;

        public static boolean WANT_CLOSE_ITEM;
        public static int CLOSE_ITEM_SLOT;

        public static int TOTAL_PAGES;

        public static boolean WANT_NEXT_PAGE;
        public static int NEXT_PAGE_SLOT;

        public static boolean WANT_PREVIOUS_PAGE;
        public static int PREVIOUS_PAGE_SLOT;

        public static boolean WANT_DEBUG;

        public static void initSettings() {
            GUI_TITLE = ConfigManager.getConfig().get().getString("GUI.title");
            GUI_SLOTS = ConfigManager.getConfig().get().getInt("GUI.total-slots");
            TOTAL_PAGES = ConfigManager.getConfig().get().getInt("GUI.total-pages");

            WANT_CHANGE_ITEM = ConfigManager.getConfig().get().getBoolean("GUI.items.change-item.enabled");

            WANT_NEXT_PAGE = ConfigManager.getConfig().get().getBoolean("GUI.items.next-page.enabled");
            NEXT_PAGE_SLOT = ConfigManager.getConfig().get().getInt("GUI.items.next-page.slot");

            WANT_PREVIOUS_PAGE = ConfigManager.getConfig().get().getBoolean("GUI.items.previous-page.enabled");
            PREVIOUS_PAGE_SLOT = ConfigManager.getConfig().get().getInt("GUI.items.previous-page.slot");

            WANT_DEBUG = ConfigManager.getConfig().get().getBoolean("debug");
        }
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
