package fr.robot.robottags.manager;

import com.google.common.base.Strings;
import fr.robot.robottags.Main;

import java.sql.SQLException;

import static fr.robot.robottags.utility.color.ColorAPI.colorize;

public class StorageManager {

    public enum DBMODE {
        MYSQL, YML
    }

    public static StorageManager.DBMODE mode = StorageManager.DBMODE.YML;

    public static void init() {
        String modeStr = ConfigManager.getConfig().get().getString("storage.mode");
        if(!Strings.isNullOrEmpty(modeStr))
            setMode(DBMODE.valueOf(modeStr.toUpperCase()));

        if(mode == DBMODE.MYSQL) {
            try {
                Main.getInstance().getLogger().info(colorize("&7Trying to connect to the database..."));
                MysqlManager.connect();
                MysqlManager.createTable();
                Main.getInstance().getLogger().info(colorize("&aSuccessfully connected !"));
            } catch (ClassNotFoundException | SQLException e) {
                Main.getInstance().getLogger().warning(colorize("&cAn error occurred while trying to connect to the database."));
                Main.getInstance().getLogger().warning(colorize("&cPlease check your MySQL credentials in the configuration file."));
                Main.getInstance().getLogger().warning(colorize("&cSwitching to YML storage mode by default."));
                setMode(DBMODE.YML);
            }
        }
    }

    public static void close() {
        if(mode == DBMODE.MYSQL)
            MysqlManager.disconnect();
    }

    public static void setMode(DBMODE mode) {
        StorageManager.mode = mode;
    }

    public static DBMODE getMode() {
        return mode;
    }
}
