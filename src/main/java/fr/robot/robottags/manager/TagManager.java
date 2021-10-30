package fr.robot.robottags.manager;

import fr.robot.robottags.Main;
import fr.robot.robottags.object.Tag;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class TagManager {

    private static Set<String> IDs = new HashSet<>();
    private static HashMap<String, Tag> tags = new HashMap<>();

    public static void init() {
        Main.getInstance().getLogger().info("Loading of tags...");
        ConfigurationSection section = ConfigManager.getTagConfig().get().getConfigurationSection("tags");

        if(section == null) return;

        IDs = section.getKeys(false);
        TagManager.getTagIds().forEach(tagID -> tags.put(tagID, getTag(tagID)));
    }

    public static boolean hasAccessTo(Player player, Tag tag) {
        if(!exist(tag.getID())) return false;
        if(!tag.needPermission()) return true;
        return player.hasPermission(tag.getPermission());
    }

    public static boolean exist(String ID) {
        return getTagIds().contains(ID);
    }

    public static Set<String> getTagIds() {
        return IDs;
    }

    public static Tag getTag(String ID) {
        Tag tag = tags.get(ID);
        if(tag == null && exist(ID)) {
            tag = new Tag(ID);
            tags.put(ID, tag);
        }
        return tag;
    }

    public static List<Tag> getTags() {
        return TagManager.getTagIds().stream().map(TagManager::getTag).collect(Collectors.toList());
    }

    public static String getTagDisplaySafe(Player player) {
        if(PlayerManager.getTag(player) != null)
            return PlayerManager.getTag(player).getDisplay();
        else if(PlayerManager.ENABLED_DEFAULT_TAG && TagManager.exist(PlayerManager.DEFAULT_TAG))
            return TagManager.getTag(PlayerManager.DEFAULT_TAG).getDisplay();
        else return "";
    }

    public static String getTagIdSafe(Player player) {
        if(PlayerManager.getTagId(player) != null)
            return PlayerManager.getTagId(player);
        else if(PlayerManager.ENABLED_DEFAULT_TAG && TagManager.exist(PlayerManager.DEFAULT_TAG))
            return PlayerManager.DEFAULT_TAG;
        else return "";
    }
}
