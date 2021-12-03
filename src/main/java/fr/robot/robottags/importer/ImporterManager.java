package fr.robot.robottags.importer;

import fr.robot.robottags.Main;
import fr.robot.robottags.importer.stock.DeluxeTags;
import fr.robot.robottags.manager.TagManager;

import static fr.robot.robottags.utility.color.ColorAPI.colorize;

public class ImporterManager {

    private static DeluxeTags deluxeTags;

    public enum ImportType {
        DELUXETAGS;
    }

    public static void init() {
        if(Main.getInstance().getServer().getPluginManager().isPluginEnabled("DeluxeTags")) {
            ImporterManager.deluxeTags = new DeluxeTags();
        }
    }

    public static AbstractImporter getImporter(ImportType type) {
        switch (type) {
            case DELUXETAGS:
                return ImporterManager.deluxeTags;
        }
    }

    public static void importTag(AbstractImporter type) {
        Main.getInstance().getLogger().info("Starting the importation of tags from " + type.getPluginName());

        if(type.getTagsId().isEmpty()) {
            Main.getInstance().getLogger().info(colorize("&cNo Tag could be found, stopping the importation."));
            return;
        }

        for(String tagID : type.getTagsId()) {
            Main.getInstance().getLogger().info(colorize("Tag found: " + tagID + ", importing it to the config."));

            if(TagManager.exist(tagID)) {
                Main.getInstance().getLogger().info(colorize("&cTag " + tagID + " already exist ! Skipping.."));
                continue;
            }


        }
    }
}
