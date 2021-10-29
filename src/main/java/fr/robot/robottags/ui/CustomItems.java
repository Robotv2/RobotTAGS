package fr.robot.robottags.ui;

import fr.robot.robottags.Main;
import fr.robot.robottags.manager.ConfigManager;
import fr.robot.robottags.manager.MessageManager;
import fr.robot.robottags.utility.ItemAPI;
import fr.robot.robottags.utility.config.Config;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.robot.robottags.utility.color.ColorAPI.colorize;

public class CustomItems {

    private static CommandSender console;

    public static HashMap<String, ItemStack> CUSTOM_ITEMS = new HashMap<>();
    public static Set<String> CUSTOM_ITEMS_ID = new HashSet<>();

    public enum ClickTypeTag {
        RIGHT, LEFT;
    }

    public static void init() {
        CUSTOM_ITEMS.clear();
        ConfigurationSection section = ConfigManager.getConfig().get().getConfigurationSection("GUI.custom-items.");
        if(section == null) return;

        console = Bukkit.getConsoleSender();
        CUSTOM_ITEMS_ID = section.getKeys(false);
        for(String id : CUSTOM_ITEMS_ID) {
            buildAndSave(id);
        }
    }

    public static Set<String> getIds() {
        return CUSTOM_ITEMS_ID;
    }

    public static int getSlot(String id) {
        return ConfigManager.getConfig().get().getInt("GUI.custom-items." + id + ".slot");
    }

    public static boolean isEnabled(String id) {
        return ConfigManager.getConfig().get().getBoolean("GUI.custom-items." + id + ".enabled");
    }

    public static void buildAndSave(String id) {
        try {
            String name = ConfigManager.getConfig().get().getString("GUI.custom-items." + id + ".name");
            List<String> lore = ConfigManager.getConfig().get().getStringList("GUI.custom-items." + id + ".lore");
            String MATERIAL_OR_HEAD = ConfigManager.getConfig().get().getString("GUI.custom-items." + id + ".material");

            ItemAPI.ItemBuilder builder;
            assert MATERIAL_OR_HEAD != null;

            if(MATERIAL_OR_HEAD.startsWith("head-")) {
                ItemStack head = ItemAPI.createSkull(MATERIAL_OR_HEAD.replace("head-", ""));
                builder = ItemAPI.toBuilder(head);
            } else {
                builder = new ItemAPI.ItemBuilder().setType(Material.valueOf(MATERIAL_OR_HEAD.toUpperCase()));
            }

            builder.setName(name).setLore(lore).setKey("custom-item", id).addFlags(ItemFlag.HIDE_ATTRIBUTES).build();
            CUSTOM_ITEMS.put(id, builder.build());
        } catch (Exception e) {
            Main.getInstance().getLogger().warning(colorize("&cAn error occurred while trying to create the custom-item: " + id));
            Main.getInstance().getLogger().warning(colorize("&cError message: &f" + e.getMessage()));
        }
    }

    public static ItemStack getItemFor(String id, Player player) {
        if(!CUSTOM_ITEMS.containsKey(id)) return new ItemStack(Material.AIR);

        ItemStack item = CUSTOM_ITEMS.get(id);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;

        String name = meta.getDisplayName();
        List<String> lore = meta.getLore();

        name = sanitizeString(name, player);
        if(lore != null && !lore.isEmpty())
            lore = lore.stream().map(line -> sanitizeString(line, player)).collect(Collectors.toList());

        ItemAPI.ItemBuilder builder = ItemAPI.toBuilder(item).setName(name);
        if(lore != null) builder.setLore(lore);

        return builder.build();
    }

    public static void click(ClickTypeTag type, String id, Player player) {
        if(!CUSTOM_ITEMS_ID.contains(id)) return;

        String section_id = type == ClickTypeTag.RIGHT ? "right-click" : "left-click";
        List<String> section = ConfigManager.getConfig().get().getStringList("GUI.custom-items." + id + "." + section_id);
        if(section.isEmpty()) return;

        for(String cmd : section) {
            if(cmd.startsWith("[CONSOLE]")) {
                cmd = cmd
                        .replace("[CONSOLE] ", "")
                        .replace("%player%", player.getName());
                Bukkit.dispatchCommand(console, cmd);
                continue;
            }
            if(cmd.startsWith("[PLAYER]")) {
                cmd = cmd
                        .replace("[PLAYER] ", "")
                        .replace("%player%", player.getName());
                Bukkit.dispatchCommand(player, cmd);
                continue;
            }
            if(cmd.startsWith("[MESSAGE]")) {
                cmd = cmd
                        .replace("[MESSAGE] ", "")
                        .replace("%player%", player.getName())
                        .replace("%prefix%", MessageManager.Message.PREFIX.getMessage());
                cmd = sanitizeString(cmd, player);
                player.sendMessage(colorize(cmd));
                continue;
            }
            if(cmd.startsWith("[CLOSE]")) {
                player.closeInventory();
            }
        }
    }

    private static String sanitizeString(String text, Player player) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }
}
