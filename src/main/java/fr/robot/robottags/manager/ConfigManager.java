package fr.robot.robottags.manager;

import fr.robot.robottags.utility.config.Config;
import fr.robot.robottags.utility.config.ConfigAPI;

public class ConfigManager {

    private static Config tagConfig;
    private static Config messageConfig;
    private static Config databaseYML;
    private static Config configYML;
    private static Config mysqlConfig;

    public static void init() {
        tagConfig = ConfigAPI.getConfig("tags");
        tagConfig.setup();
        messageConfig = ConfigAPI.getConfig("messages");
        messageConfig.setup();
        databaseYML = ConfigAPI.getConfig("database");
        databaseYML.setup();
        configYML = ConfigAPI.getConfig("configuration");
        configYML.setup();
        mysqlConfig = ConfigAPI.getConfig("mysql");
        mysqlConfig.setup();
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

    public static Config getMysqlConfig() {
        return mysqlConfig;
    }
}
