package fr.robot.robottags.manager;

import fr.robot.robottags.utility.config.Config;
import fr.robot.robottags.utility.config.ConfigAPI;
import org.bukkit.Bukkit;

public class ConfigManager {

    private static Config tagConfig;
    private static Config messageConfig;
    private static Config databaseYML;
    private static Config configYML;
    private static Config mysqlConfig;

    public static void init() {
        tagConfig = ConfigAPI.getConfig("tags");
        messageConfig = ConfigAPI.getConfig("messages");
        databaseYML = ConfigAPI.getConfig("database");
        configYML = ConfigAPI.getConfig("configuration");
        mysqlConfig = ConfigAPI.getConfig("mysql");
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

    public static Config getConfig() { return configYML; }

    public static Config getMysqlConfig() { return mysqlConfig; }
}
