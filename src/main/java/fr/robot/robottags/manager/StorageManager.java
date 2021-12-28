package fr.robot.robottags.manager;

import com.google.common.base.Strings;
import fr.robot.robottags.data.AbstractData;
import fr.robot.robottags.data.stock.MySQL;
import fr.robot.robottags.data.stock.Yaml;

public class StorageManager {

    public enum DbMode {
        MYSQL, YML
    }

    public static StorageManager.DbMode mode = StorageManager.DbMode.YML;
    public static AbstractData data;

    public static void init() {
        String modeStr = ConfigManager.getConfig().get().getString("storage.mode");
        if(!Strings.isNullOrEmpty(modeStr)) {
            DbMode mode = DbMode.valueOf(modeStr.toUpperCase());
            setMode(mode);
        }

        switch(getMode()) {
            case YML -> data = new Yaml();
            case MYSQL -> data = new MySQL();
        }

        getData().load();
    }

    public static void close() {
        getData().close();
    }

    public static void setMode(DbMode mode) {
        StorageManager.mode = mode;
    }

    public static DbMode getMode() {
        return mode;
    }

    public static AbstractData getData() {
        return data;
    }
}
