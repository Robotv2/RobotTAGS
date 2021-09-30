package fr.robot.robottags;

import fr.robot.robottags.manager.PlayerManager;
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
                return PlayerManager.getTag(player).getDisplay();
            case "player_id":
                return PlayerManager.getTagId(player);
            case "player_uncolored":
                return uncolorize(PlayerManager.getTag(player).getDisplay());
        }
        return placeholder;
    }
}
