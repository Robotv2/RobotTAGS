package fr.robot.robottags.utility.cooldown;

import java.util.HashMap;

public class CooldownAPI {

    private static final HashMap<String, Cooldown> cooldowns = new HashMap<>();

    public static Cooldown getCooldown(String name) {
        Cooldown cooldown = cooldowns.get(name);
        if(cooldown == null) {
            cooldown = new Cooldown(name);
            cooldowns.put(name, cooldown);
        }
        return cooldown;
    }

    public static Cooldown getCooldown(org.bukkit.entity.Player player, String name) {
        return getCooldown(player.getUniqueId() + "-" + name);
    }
}
