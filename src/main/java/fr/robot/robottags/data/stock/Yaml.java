package fr.robot.robottags.data.stock;

import fr.robot.robottags.data.AbstractData;
import fr.robot.robottags.manager.ConfigManager;

import java.util.UUID;

public class Yaml implements AbstractData {
    @Override
    public void load() {
    }

    @Override
    public void close() {
    }

    @Override
    public void initPlayer(UUID playerUUID) {
    }

    @Override
    public boolean hasTag(UUID playerUUID) {
        return ConfigManager.getDatabase().get().get(playerUUID.toString()) != null;
    }

    @Override
    public void setTagId(UUID playerUUID, String tag) {
        ConfigManager.getDatabase().get().set(playerUUID.toString(), tag);
        ConfigManager.getDatabase().save();
    }

    @Override
    public String getTagId(UUID playerUUID) {
        return ConfigManager.getDatabase().get().getString(playerUUID.toString());
    }
}
