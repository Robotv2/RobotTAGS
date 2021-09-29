package fr.robot.robottags.utility;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.robot.robottags.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static fr.robot.robottags.utility.color.ColorAPI.colorize;

public class ItemAPI {

    public static HashMap<String, ItemStack> heads = new HashMap<>();

    public static HashMap<String, ItemStack> getCachedHeads() {
        return heads;
    }

    public static ItemStack getHead(UUID playerUUID) {
        if(heads.containsKey(playerUUID.toString()))
            return heads.get(playerUUID.toString());

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerUUID));
        head.setItemMeta(meta);

        heads.put(playerUUID.toString(), head);
        return head;
    }

    public static ItemStack getHead(Player player) {
        return getHead(player.getUniqueId());
    }

    public static ItemStack getHead(String playerName) {
        return getHead(Bukkit.getOfflinePlayer(playerName).getUniqueId());
    }

    public static ItemStack createSkull(String url) {
        if(heads.containsKey(url))
            return heads.get(url);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (url.isEmpty()) return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException|NoSuchFieldException|SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        heads.put(url, head);
        return head;
    }

    public static boolean hasKey(ItemStack item, String keyStr, PersistentDataType type) {
        NamespacedKey key = new NamespacedKey(Main.getInstance(), keyStr);
        return item.getItemMeta().getPersistentDataContainer().has(key, type);
    }

    public static itemBuilder toBuilder(ItemStack item) {
        itemBuilder builder = new itemBuilder();
        builder.setMeta(item.getItemMeta());
        builder.setType(item.getType());
        builder.setAmount(item.getAmount());
        return builder;
    }

    public static class itemBuilder {
        private Material type;
        private int amount;
        private ItemMeta meta = new ItemStack(Material.GRASS).getItemMeta();

        public itemBuilder setType(Material type) {
            this.type = type;
            return this;
        }

        public itemBuilder setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public itemBuilder setName(String name) {
            this.meta.setDisplayName(colorize(name));
            return this;
        }

        public itemBuilder setLore(String... lore) {
            List<String> result = new ArrayList<>(List.of(lore));
            for(int i = 0; i < result.size(); i++) {
                String line = result.get(i);
                result.set(i, colorize(line));
            }
            this.meta.setLore(result);
            return this;
        }

        public itemBuilder setLore(List<String> lore) {
            for(int i = 0; i < lore.size(); i++) {
                String line = lore.get(i);
                lore.set(i, colorize(line));
            }
            this.meta.setLore(lore);
            return this;
        }

        public itemBuilder setKey(String keyStr, String value) {
            NamespacedKey key = new NamespacedKey(Main.getInstance(), keyStr);
            this.meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
            return this;
        }

        public itemBuilder setKey(String keyStr, double value) {
            NamespacedKey key = new NamespacedKey(Main.getInstance(), keyStr);
            this.meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
            return this;
        }

        public itemBuilder setKey(String keyStr, int value) {
            NamespacedKey key = new NamespacedKey(Main.getInstance(), keyStr);
            this.meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
            return this;
        }

        public itemBuilder setKey(String keyStr, float value) {
            NamespacedKey key = new NamespacedKey(Main.getInstance(), keyStr);
            this.meta.getPersistentDataContainer().set(key, PersistentDataType.FLOAT, value);
            return this;
        }

        public itemBuilder addEnchant(Enchantment enchant, int level, boolean ignoreLevelRestriction) {
            this.meta.addEnchant(enchant, level, ignoreLevelRestriction);
            return this;
        }

        public itemBuilder setUnbreakable(boolean unbreakable) {
            this.meta.setUnbreakable(unbreakable);
            return this;
        }

        public itemBuilder addFlags(ItemFlag... flags) {
            this.meta.addItemFlags(flags);
            return this;
        }

        public itemBuilder setMeta(ItemMeta meta) {
            this.meta = meta;
            return this;
        }

        public ItemStack build() {
            if(this.type == null)
                type = Material.AIR;
            if(this.amount <= 0)
                amount = 1;
            ItemStack item = new ItemStack(type, amount);
            item.setItemMeta(meta);
            return item;
        }
    }
}
