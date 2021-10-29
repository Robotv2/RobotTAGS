package fr.robot.robottags;

import fr.robot.robottags.commands.RobotTagsCommand;
import fr.robot.robottags.listeners.PlayerEvents;
import fr.robot.robottags.manager.*;
import fr.robot.robottags.ui.CustomItems;
import fr.robot.robottags.ui.ItemStock;
import fr.robot.robottags.ui.MenuGUI;
import fr.robot.robottags.utility.config.ConfigAPI;
import fr.robot.robottags.utility.ui.GuiAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static fr.robot.robottags.utility.color.ColorAPI.colorize;

public final class Main extends JavaPlugin {

    private static Main INSTANCE;
    private Placeholderapi placeholderapi;

    @Override
    public void onEnable() {
        INSTANCE = this;
        long current = System.currentTimeMillis();

        getLogger().info("");
        getLogger().info("Thanks for using RobotTags !");
        getLogger().info("Author: Robotv2");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("");

        getLogger().info("Loading of the configurations files...");
        ConfigAPI.init(this);
        ConfigManager.init();

        StorageManager.init();
        TagManager.init();
        PlayerManager.init();

        ItemStock.init();
        CustomItems.init();

        initListeners();
        initCommands();
        initGui();

        updateChecker();
        new Metrics(this, 11791);

        placeholderapi = new Placeholderapi();
        placeholderapi.register();

        getLogger().info("The plugin has been loaded in " + String.valueOf(System.currentTimeMillis() - current) + "MS");
        getLogger().info("");
    }

    @Override
    public void onDisable() {
        StorageManager.close();
        placeholderapi.unregister();
        INSTANCE = null;
    }

    public void onReload() {
        ConfigAPI.getConfig("tags").reload();
        ConfigAPI.getConfig("messages").reload();
        ConfigAPI.getConfig("database").reload();
        ConfigAPI.getConfig("configuration").reload();

        TagManager.init();
        PlayerManager.init();
        ItemStock.init();
        CustomItems.init();
        ConfigManager.Settings.initSettings();
        getLogger().info(colorize("&7Please not that &cyou can't &7change the storage mode with only /tags reload. You'll need to restart your server."));
    }

    private void updateChecker() {
        String response = new UpdateChecker(91885).getVersion();

        if (response != null) {
            try {
                double pluginVersion = Double.parseDouble(getDescription().getVersion());
                double pluginVersionLatest = Double.parseDouble(response);

                if (pluginVersion < pluginVersionLatest) {
                    getLogger().info("");
                    getLogger().info("Update: Outdated version detected " + pluginVersion + ", latest version is "
                            + pluginVersionLatest + ", https://www.spigotmc.org/resources/robottags-hex-color-support-mysql-cross-server-and-gui-system.91885/");
                    getLogger().info("");
                }
            } catch (NumberFormatException exception) {
                if (!getDescription().getVersion().equalsIgnoreCase(response)) {
                    getLogger().info("");
                    getLogger().info("Update: Outdated version detected " + getDescription().getVersion()
                            + ", latest version is " + response
                            + ", https://www.spigotmc.org/resources/robottags-hex-color-support-mysql-cross-server-and-gui-system.91885/");
                    getLogger().info("");
                }
            }
        }
    }


    public void initListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerEvents(), this);
        pm.registerEvents(new GuiAPI(), this);
    }

    public void initCommands() {
        new RobotTagsCommand(this, "robottags");
    }

    public void initGui() {
        GuiAPI.addMenu(new MenuGUI());
    }



    public static Main getInstance() {
        return INSTANCE;
    }

    public static boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission("robottags.admin") || sender.hasPermission(permission);
    }

    public static void sendMessage(CommandSender sender, boolean prefix, String message) {
        if(prefix)
            sender.sendMessage(MessageManager.Message.PREFIX.getMessage()  + colorize(message));
        else
            sender.sendMessage(colorize(message));
    }

    public static void sendDebug(String message) {
        if(ConfigManager.Settings.WANT_DEBUG)
            Main.getInstance().getLogger().info(colorize("&7DEBUG &8- &f" + message));
    }
}
