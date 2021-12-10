package fr.robot.robottags.importer.stock;

import fr.robot.robottags.Main;
import fr.robot.robottags.importer.AbstractImporter;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public Set<String> getTagsId() {
        ConfigurationSection section = config.getConfigurationSection("deluxetags.");
        if(section == null)
            return new HashSet<>();
        return section.getKeys(false);
    }

    @Override
    public String getDisplay(String id) {
        return config.getString("deluxetags." + id + ".tag");
    }

    @Override
    public List<String> getLore(String id) {
        List<String> lore = config.getStringList("deluxetags." + id + ".description");
        if(lore.isEmpty())
            return Arrays.asList("", "&8imported from deluxetags !", "");
        return lore;
    }

    @Override
    public Material getMaterial(String id) {
        return Material.BOOK;
    }

    @Override
    public int getSlot(String id) {
        return config.getInt("deluxetags." + id + ".order") - 1;
    }

    @Override
    public int getPage(String id) {
        return 1;
    }

    @Override
    public boolean needPermission(String id) {
        return true;
    }

    @Override
    public String getPermission(String id) {
        return config.getString("deluxetags." + id + ".permission");
    }
}
