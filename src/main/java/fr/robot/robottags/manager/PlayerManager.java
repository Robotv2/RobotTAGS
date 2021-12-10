package fr.robot.robottags.manager;

import fr.robot.robottags.object.Tag;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

import static fr.robot.robottags.Main.debug;

public class PlayerManager {

    public static HashMap<UUID, String> playerTags = new HashMap<>();

    public static boolean ENABLED_DEFAULT_TAG = false;
    public static String DEFAULT_TAG = "default";

    public static void init() {
        ENABLED_DEFAULT_TAG = ConfigManager.getConfig().get().getBoolean("options.default-tag.enabled");
        DEFAULT_TAG = ConfigManager.getConfig().get().getString("options.default-tag.tag");
    }

    public static void setTag(UUID playerUUID, String tagID) {
        playerTags.put(playerUUID, tagID);
    }

    public static void load(UUID playerUUID) {
        debug("Chargement du tag pour un joueur. (" + playerUUID.toString() + ")");
        String tagID = StorageManager.getData().getTagId(playerUUID);

        if(tagID != null && TagManager.exist(tagID)) {
            setTag(playerUUID, tagID);
            debug("Le joueur possède le tag " + tagID + " (" + StorageManager.getMode() + ")");
        } else {
            debug("Le joueur n'a été vu avec aucun tag existant." +  " (" + StorageManager.getMode() + ")");
        }
    }

    public static void save(UUID playerUUID) {
        String tagID = getTagId(playerUUID);
        if(tagID == null) {
            debug("Le joueur avec l'UUID " + playerUUID + " ne possède pas de tag.");
            return;
        }
        StorageManager.getData().setTagId(playerUUID, tagID);
        debug("Le joueur avec l'UUID " + playerUUID + " a été sauvegardé avec le tag " + tagID + " + (" + StorageManager.getMode() + ")");
    }

    public static void clear(UUID playerUUID) {
        StorageManager.getData().setTagId(playerUUID, null);
        playerTags.remove(playerUUID);
    }

    public static void save(Player player) {
        save(player.getUniqueId());
    }

    public static @Nullable String getTagId(UUID playerUUID) {
        if(playerTags.containsKey(playerUUID) && TagManager.exist(playerTags.get(playerUUID)))
            return playerTags.get(playerUUID);
        return null;
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

    public static void setTag(Player player, String tagID) {
        setTag(player.getUniqueId(), tagID);
    }

    public static void load(Player player) {
        load(player.getUniqueId());
    }
}
