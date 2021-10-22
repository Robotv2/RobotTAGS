package fr.robot.robottags.manager;

import org.bukkit.command.CommandSender;

import static fr.robot.robottags.utility.color.ColorAPI.colorize;

public class MessageManager {

    public enum Message {
        PREFIX(ConfigManager.getMessageConfig().get().getString("prefix")),
        NO_PERMISSION(ConfigManager.getMessageConfig().get().getString("no-permission")),
        NOT_FROM_CONSOLE(ConfigManager.getMessageConfig().get().getString("not-from-console"));

        public String message;
        Message(String message) {
            this.message = message;
        }

        public void send(CommandSender sender) {
            message = colorize(message);
            sender.sendMessage(message);
        }

        public String getMessage() {
            return colorize(message);
        }
    }
}
