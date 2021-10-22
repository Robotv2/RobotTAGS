package fr.robot.robottags;

import fr.robot.robottags.commands.RobotTagsCommand;
import fr.robot.robottags.listeners.PlayerEvents;
import fr.robot.robottags.manager.*;
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

    @Override
    public void onEnable() {
        INSTANCE = this;

        ConfigAPI.init(this);
        ConfigManager.init();
        StorageManager.init();
        TagManager.init();
        PlayerManager.init();

        initListeners();
        initCommands();
        initGui();
        (new Placeholderapi()).register();
    }

    @Override
    public void onDisable() {
        StorageManager.close();
        INSTANCE = null;
    }

    public void onReload() {
        ConfigAPI.getConfig("tags").reload();
        ConfigAPI.getConfig("messages").reload();
        ConfigAPI.getConfig("database").reload();
        ConfigAPI.getConfig("configuration").reload();
        ConfigAPI.getConfig("mysql").reload();

        TagManager.init();
        PlayerManager.init();
        getLogger().info(colorize("&7Please not that &cyou can't &7change the storage mode with only /tags reload. You'll need to restart your server."));
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
            sender.sendMessage(MessageManager.Message.PREFIX.getMessage() + " " + colorize(message));
        else
            sender.sendMessage(colorize(message));
    }
}
