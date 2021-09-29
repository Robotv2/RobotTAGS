package fr.robot.robottags.object;

import fr.robot.robottags.manager.ConfigManager;
import fr.robot.robottags.utility.ItemAPI;
import fr.robot.robottags.utility.color.IridiumColorAPI.IridiumColorAPI;
import fr.robot.robottags.utility.config.Config;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static fr.robot.robottags.utility.color.ColorAPI.colorize;

public class Tag {

    private final Config config;

    private final String ID;
    private String display;
    private boolean useHexColor;

    private boolean needPermission;
    private String permission;

    private int slot;
    private int page;
    private Material material;
    private List<String> lore;
    private final ItemStack item;

    public Tag(String ID) {
        this.config = ConfigManager.getTagConfig();
        this.ID = ID;
        this.useHexColor = config.get().getBoolean("tags." + ID + ".use-hex-color");

        this.needPermission = config.get().getBoolean("tags." + ID + ".need-permission");
        this.permission = config.get().getString("tags." + ID + ".permission");

        this.slot = config.get().getInt("tags." + ID + ".slot");
        this.page = config.get().getInt("tags." + ID + ".page");
        this.material = Material.valueOf(config.get().getString("tags." + ID + ".material").toUpperCase());
        this.lore = config.get().getStringList("tags." + ID + ".lore");
        this.item = new ItemAPI.itemBuilder()
                .setType(material).setLore(lore)
                .addFlags(ItemFlag.HIDE_ATTRIBUTES).build();

        String unformated = config.get().getString("tags." + ID + ".display");
        if(useHexColor)
            this.display = IridiumColorAPI.process(unformated);
        else
            this.display = colorize(unformated);
    }

    public void saveToFile() {
        config.get().set("tags." + ID + ".display", display);
        config.get().set("tags." + ID + ".use-hex-color", useHexColor);
        config.get().set("tags." + ID + ".need-permission", needPermission);
        config.get().set("tags." + ID + ".permission", permission);
        config.get().set("tags." + ID + ".slot", slot);
        config.get().set("tags." + ID + ".page", page);
        config.get().set("tags." + ID + ".material", material);
        config.get().set("tags." + ID + ".lore", lore);
        config.save();
    }

    public String getID() {
        return ID;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public boolean useHexColor() {
        return useHexColor;
    }

    public void setUseHexColor(boolean useHexColor) {
        this.useHexColor = useHexColor;
    }

    public boolean needPermission() {
        return needPermission;
    }

    public void setNeedPermission(boolean needPermission) {
        this.needPermission = needPermission;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public ItemStack getItem() {
        return item;
    }
}
