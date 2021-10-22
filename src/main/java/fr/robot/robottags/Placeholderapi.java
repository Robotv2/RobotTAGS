package fr.robot.robottags;

import fr.robot.robottags.manager.PlayerManager;
import fr.robot.robottags.manager.TagManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

import static fr.robot.robottags.utility.color.ColorAPI.uncolorize;

public class Placeholderapi extends PlaceholderExpansion {

    @Override
    public @Nonnull String getAuthor() {
        return "Robotv2";
    }

    @Override
    public @Nonnull String getIdentifier() {
        return "robottags";
    }

    @Override
    public @Nonnull String getVersion() {
        return Main.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    public String onPlaceholderRequest(Player player, @Nonnull String placeholder) {
        if (player == null)
            return "";
        switch(placeholder.toLowerCase()) {
            case "player":
                return getTagDisplaySafe(player);
            case "player_id":
                return getTagIdSafe(player);
            case "player_uncolored":
                return uncolorize(getTagDisplaySafe(player));
        }
        if(placeholder.startsWith("tag_")) {
            String tadID = placeholder.replace("tag_", "");
            if(TagManager.exist(tadID))
                return TagManager.getTag(tadID).getDisplay();
        }
        return placeholder;
    }

    public String getTagDisplaySafe(Player player) {
        if(PlayerManager.getTag(player) != null)
            return PlayerManager.getTag(player).getDisplay();
        else if(PlayerManager.ENABLED_DEFAULT_TAG && TagManager.exist(PlayerManager.DEFAULT_TAG))
            return TagManager.getTag(PlayerManager.DEFAULT_TAG).getDisplay();
        else return "";
    }

    public String getTagIdSafe(Player player) {
        if(PlayerManager.getTagId(player) != null)
            return PlayerManager.getTagId(player);
        else if(PlayerManager.ENABLED_DEFAULT_TAG && TagManager.exist(PlayerManager.DEFAULT_TAG))
            return PlayerManager.DEFAULT_TAG;
        else return "";
    }
}
