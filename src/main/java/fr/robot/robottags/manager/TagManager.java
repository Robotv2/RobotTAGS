package fr.robot.robottags.manager;

import fr.robot.robottags.Main;
import fr.robot.robottags.object.Tag;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class TagManager {

    private static Set<String> IDs = new HashSet<>();
    private static HashMap<String, Tag> tags;

    public static void init() {
        Main.getInstance().getLogger().info("Loading of tags...");
        ConfigurationSection section = ConfigManager.getTagConfig().get().getConfigurationSection("tags");
        if(section != null)
            IDs = section.getKeys(false);
        tags = new HashMap<>();
        getTagIds().forEach(tagID -> tags.put(tagID, getTag(tagID)));
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
        if(tag == null && exist(ID))
            tag = new Tag(ID);
        return tag;
    }

    public static List<Tag> getTags() {
        List<Tag> result = new ArrayList<>();
        for(String tagID : getTagIds()) {
            result.add(getTag(tagID));
        }
        return result;
    }
}
