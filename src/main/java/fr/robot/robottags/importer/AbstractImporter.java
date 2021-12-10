package fr.robot.robottags.importer;

import org.bukkit.Material;

import java.util.List;
import java.util.Set;

public interface AbstractImporter {

    String getPluginName();

    Set<String> getTagsId();
    String getDisplay(String id);

    List<String> getLore(String id);
    Material getMaterial(String id);
    int getSlot(String id);
    int getPage(String id);

    boolean needPermission(String id);
    String getPermission(String id);
}
