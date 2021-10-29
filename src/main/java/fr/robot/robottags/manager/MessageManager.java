package fr.robot.robottags.manager;

import fr.robot.robottags.Main;
import org.bukkit.command.CommandSender;

import static fr.robot.robottags.utility.color.ColorAPI.colorize;

public class MessageManager {

    public enum Message {
        PREFIX(ConfigManager.getMessageConfig().get().getString("prefix")),
        NO_PERMISSION(ConfigManager.getMessageConfig().get().getString("no-permission")),
        NOT_FROM_CONSOLE(ConfigManager.getMessageConfig().get().getString("not-from-console")),
        PLUGIN_RELOADED(ConfigManager.getMessageConfig().get().getString("plugin-reloaded")),

        ADMIN_SET_TAG(ConfigManager.getMessageConfig().get().getString("admin-set-tag")),
        ADMIN_CLEAR_TAG(ConfigManager.getMessageConfig().get().getString("admin-clear-tag")),

        PLAYER_TAG_CHANGED(ConfigManager.getMessageConfig().get().getString("player-tag-changed")),
        PLAYER_CANT_ACCESS(ConfigManager.getMessageConfig().get().getString("player-can't-access"));

        public String message;
        Message(String message) {
            this.message = message;
        }

        public void send(CommandSender sender) {
            Main.sendMessage(sender, true, message);
        }

        public String getMessage() {
            return colorize(message);
        }
    }
}
