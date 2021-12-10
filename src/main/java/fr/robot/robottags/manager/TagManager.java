package fr.robot.robottags.manager;

import fr.robot.robottags.Main;
import fr.robot.robottags.object.Tag;
import fr.robot.robottags.utility.config.Config;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class TagManager {

    private static Set<String> IDs = new HashSet<>();
    private static HashMap<String, Tag> tags = new HashMap<>();

    public static void init() {
        Main.getInstance().getLogger().info("Loading of the tags...");
        ConfigurationSection section = ConfigManager.getTagConfig().get().getConfigurationSection("tags");

        if(section == null) return;

        IDs = section.getKeys(false);
        TagManager.getTagIds().forEach(tagID -> tags.put(tagID, getTag(tagID)));
    }

    public static boolean exist(String ID) {
        return getTagIds().contains(ID);
    }

    public static Tag getTag(String ID) {
        Tag tag = tags.get(ID);
        if(tag == null && exist(ID)) {
            tag = new Tag(ID);
            tags.put(ID, tag);
        }
        return tag;
    }

    public static Set<String> getTagIds() {
        return IDs;
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

    public static boolean hasAccessTo(Player player, Tag tag) {
        if(!exist(tag.getID())) return false;
        if(!tag.needPermission()) return true;
        return player.hasPermission(tag.getPermission());
    }

    public static class TagBuilder {
        private String ID;
        private String DISPLAY;
        private String PERMISSION;

        private boolean USE_HEX_COLOR = false;
        private boolean NEED_PERMISSION;

        private int SLOT;
        private int PAGE;
        private String MATERIAL;
        private List<String> LORE;

        public TagBuilder setId(String id) {
            this.ID = id;
            return this;
        }

        public TagBuilder setDisplay(String display) {
            this.DISPLAY = display;
            return this;
        }

        public TagBuilder setPermission(String permission) {
            this.PERMISSION = permission;
            return this;
        }

        public void setUseHexColor(boolean USE_HEX_COLOR) {
            this.USE_HEX_COLOR = USE_HEX_COLOR;
        }

        public void setNeedPermission(boolean NEED_PERMISSION) {
            this.NEED_PERMISSION = NEED_PERMISSION;
        }

        public void setSlot(int SLOT) {
            this.SLOT = SLOT;
        }

        public void setPage(int PAGE) {
            this.PAGE = PAGE;
        }

        public void setMaterial(String MATERIAL) {
            this.MATERIAL = MATERIAL;
        }

        public void setLore(List<String> lore) {
            this.LORE = lore;
        }

        public void create() {
            if(Main.Validation(ID, DISPLAY)) return;

            Config config = ConfigManager.getTagConfig();
            config.get().set("tags." + ID + ".display", DISPLAY);
            config.get().set("tags." + ID + ".use-hex-color", USE_HEX_COLOR);
            config.get().set("tags." + ID + ".need-permission", NEED_PERMISSION);
            config.get().set("tags." + ID + ".slot", SLOT);

            if(Objects.isNull(PERMISSION))
                config.get().set("tags." + ID + ".permission", "robottags.tag." + ID);
            else
                config.get().set("tags." + ID + ".permission", PERMISSION);

            config.get().set("tags." + ID + ".page", this.PAGE);
            config.get().set("tags." + ID + ".material", this.MATERIAL);
            config.get().set("tags." + ID + ".lore", this.LORE);
            config.save();
        }
    }
}
