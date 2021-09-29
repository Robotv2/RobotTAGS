package fr.robot.robottags;

import fr.robot.robottags.listeners.PlayerEvents;
import fr.robot.robottags.manager.ConfigManager;
import fr.robot.robottags.manager.PlayerManager;
import fr.robot.robottags.manager.StorageManager;
import fr.robot.robottags.manager.TagManager;
import fr.robot.robottags.utility.config.ConfigAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        ConfigAPI.init(this);
        StorageManager.init();
        TagManager.init();
        ConfigManager.init();
        PlayerManager.init();

        initListeners();
    }

    @Override
    public void onDisable() {
        INSTANCE = null;
        StorageManager.close();
    }

    public void initListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerEvents(), this);
    }

    public static Main getInstance() {
        return INSTANCE;
    }
}
