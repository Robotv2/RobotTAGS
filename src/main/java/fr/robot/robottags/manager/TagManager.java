package fr.robot.robottags.manager;

import fr.robot.robottags.object.Tag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TagManager {

    private static Set<String> IDs = new HashSet<>();
    private static HashMap<String, Tag> tags;

    public static void init() {
        IDs = ConfigManager.getTagConfig().get().getConfigurationSection("tags").getKeys(false);
        tags = new HashMap<>();
        getTagIds()
                .stream().filter(TagManager::exist)
                .forEach(tagID -> tags.put(tagID, getTag(tagID)));
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
}
