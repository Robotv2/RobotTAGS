package fr.robot.robottags.importer.stock;

import fr.robot.robottags.Main;
import fr.robot.robottags.importer.AbstractImporter;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class DeluxeTags implements AbstractImporter {

    private Configuration config;

    public DeluxeTags() {
        Plugin deluxeTag = Main.getInstance().getServer().getPluginManager().getPlugin("DeluxeTags");
        if(deluxeTag != null)
            config = deluxeTag.getConfig();
    }

    @Override
    public String getPluginName() {
        return "DeluxeTags";
    }

    @Override
    public List<String> getTagsId() {
        return config.getStringList("tags.à-faire.lol");
    }

    @Override
    public String getDisplay(String id) {
        return config.getString("tags." + id + ".id");
    }

    @Override
    public List<String> getLore(String id) {
        return null;
    }

    @Override
    public String getMaterial(String id) {
        return null;
    }

    @Override
    public int getSlot(String id) {
        return 0;
    }

    @Override
    public int getPage(String id) {
        return 0;
    }

    @Override
    public boolean needPermission(String id) {
        return false;
    }

    @Override
    public String getPermission(String id) {
        return null;
    }
}
