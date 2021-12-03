package fr.robot.robottags.importer;

import java.util.List;

public interface AbstractImporter {

    String getPluginName();

    List<String> getTagsId();
    String getDisplay(String id);

    List<String> getLore(String id);
    String getMaterial(String id);
    int getSlot(String id);
    int getPage(String id);

    boolean needPermission(String id);
    String getPermission(String id);
}
