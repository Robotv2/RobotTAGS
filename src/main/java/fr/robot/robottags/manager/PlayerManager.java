package fr.robot.robottags.manager;

import fr.robot.robottags.manager.StorageManager.DBMODE;
import fr.robot.robottags.object.Tag;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    public static HashMap<UUID, String> playerTags = new HashMap<>();

    public static boolean ENABLED_DEFAULT_TAG = false;
    public static String DEFAULT_TAG = "default";

    public static void init() {
        ENABLED_DEFAULT_TAG = ConfigManager.getConfig().get().getBoolean("default-tag.enabled");
        DEFAULT_TAG = ConfigManager.getConfig().get().getString("default-tag.tag");
    }

    public static boolean hasTag(Player player) {
        return hasTag(player.getUniqueId());
    }

    public static boolean hasTag(UUID playerUUID) {
        switch(StorageManager.getMode()) {
            case YML:
                return ConfigManager.getDatabase().get().get(playerUUID.toString()) != null;
            case MYSQL:
                return MysqlManager.SQLgettag(playerUUID) != null;
        }
        return false;
    }

    public static void load(Player player) {
        load(player.getUniqueId());
    }

    public static void load(UUID playerUUID) {
        String tagID = null;
        if(hasTag(playerUUID)) {
            switch (StorageManager.getMode()) {
                case YML:
                    tagID = ConfigManager.getDatabase().get().getString(playerUUID.toString());
                    break;
                case MYSQL:
                    tagID = MysqlManager.SQLgettag(playerUUID);
                    break;
            }
        }

        if(tagID != null && TagManager.exist(tagID))
            setTag(playerUUID, tagID);
        else if(ENABLED_DEFAULT_TAG && TagManager.exist(DEFAULT_TAG))
            setTag(playerUUID, DEFAULT_TAG);
        else
            setTag(playerUUID, null);
    }

    public static void save(UUID playerUUID) {
        String tagID = getTagId(playerUUID);
        if(tagID == null) return;
        switch (StorageManager.getMode()) {
            case YML:
                ConfigManager.getDatabase().get().set(playerUUID.toString(), tagID);
                break;
            case MYSQL:
                MysqlManager.SQLsettag(playerUUID, tagID);
                break;
        }
    }

    public static void save(Player player) {
        save(player.getUniqueId());
    }

    public static String getTagId(UUID playerUUD) {
        return playerTags.get(playerUUD);
    }

    public static String getTagId(Player player) {
        return getTagId(player.getUniqueId());
    }

    public static Tag getTag(UUID playerUUD) {
        return TagManager.getTag(getTagId(playerUUD));
    }

    public static Tag getTag(Player player) {
        return getTag(player.getUniqueId());
    }

    public static void setTag(UUID playerUUID, String tagID) {
        playerTags.put(playerUUID, tagID);
    }

    public static void setTag(Player player, String tagID) {
        setTag(player.getUniqueId(), tagID);
    }
}
