package fr.robot.robottags.importer;

import fr.robot.robottags.Main;
import fr.robot.robottags.importer.stock.DeluxeTags;
import fr.robot.robottags.manager.TagManager;
import fr.robot.robottags.utility.config.ConfigAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.robot.robottags.Main.log;

public class ImporterManager {

    private static DeluxeTags deluxeTags = null;
    private static List<String> importValues = new ArrayList<>();

    public enum ImportType {
        DELUXETAGS;
    }

    public static void init() {
        importValues = Stream.of(ImportType.values()).map(type -> type.toString().toLowerCase()).collect(Collectors.toList());
        if(Main.getInstance().getServer().getPluginManager().isPluginEnabled("DeluxeTags")) {
            ImporterManager.deluxeTags = new DeluxeTags();
        }
    }

    public static List<String> getValues() {
        return importValues;
    }

    public static AbstractImporter getImporter(ImportType type) {
        switch (type) {
            case DELUXETAGS:
                return ImporterManager.deluxeTags;
        }
        return null;
    }

    public static void importTag(AbstractImporter type) {
        log("Starting the importation of tags from " + type.getPluginName());

        if(type.getTagsId().isEmpty()) {
            log("&cNo Tag could be found, stopping the importation.");
            return;
        }

        for(String tagID : type.getTagsId()) {
            log("Tag found: " + tagID + ", importing it to the config.");

            if(TagManager.exist(tagID)) {
                log("&cTag " + tagID + " already exist ! Skipping..");
                continue;
            }

            TagManager.TagBuilder builder = new TagManager.TagBuilder();
            builder.setId(tagID);
            builder.setDisplay(type.getDisplay(tagID));
            builder.setLore(type.getLore(tagID));
            builder.setMaterial(type.getMaterial(tagID).toString());

            builder.setUseHexColor(false);
            builder.setNeedPermission(type.needPermission(tagID));
            builder.setPermission(type.getPermission(tagID));

            builder.setPage(type.getPage(tagID));
            builder.setSlot(type.getSlot(tagID));

            builder.create();
            log("Tag " + tagID + " has been imported with success !");
        }

        ConfigAPI.getConfig("tags").reload();
        TagManager.init();
    }
}
