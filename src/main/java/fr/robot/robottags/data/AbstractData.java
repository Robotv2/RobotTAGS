package fr.robot.robottags.data;

import java.util.UUID;

public interface AbstractData {

    void load();
    void initPlayer(UUID playerUUID);
    boolean hasTag(UUID playerUUID);
    void setTagId(UUID playerUUID, String tagID);
    String getTagId(UUID playerUUID);
    void close();
}
