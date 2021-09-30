package fr.robot.robottags.manager;

import joptsimple.internal.Strings;

import java.sql.SQLException;

public class StorageManager {

    public enum DBMODE {
        MYSQL, YML
    }

    public static StorageManager.DBMODE mode = StorageManager.DBMODE.YML;

    public static void init() {
        String modeStr = ConfigManager.getConfig().get().getString("storage.mode");
        if(!Strings.isNullOrEmpty(modeStr))
            setMode(DBMODE.valueOf(modeStr));

        if(mode == DBMODE.MYSQL) {
            try {
                MysqlManager.connect();
                MysqlManager.createTable();
            } catch (ClassNotFoundException | SQLException e) {
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
