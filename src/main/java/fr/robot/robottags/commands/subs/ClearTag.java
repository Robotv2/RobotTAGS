package fr.robot.robottags.commands.subs;

import fr.robot.robottags.Main;
import fr.robot.robottags.commands.AbstractSub;
import fr.robot.robottags.manager.MessageManager;
import fr.robot.robottags.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import static fr.robot.robottags.Main.sendMessage;

public class ClearTag implements AbstractSub {

    public void execute(CommandSender sender, String[] args) {

        if(!Main.hasPermission(sender, "robottags.clear")) {
            MessageManager.Message.NO_PERMISSION.send(sender);
            return;
        }

        if(args.length != 2) {
            sendMessage(sender, true, "&cUSAGE: /robottags clear <player>");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        if(!target.isOnline() && !target.hasPlayedBefore()) {
            sendMessage(sender, true, "&cThis player never logged in.");
            return;
        }

        PlayerManager.clear(target.getUniqueId());
        String message = MessageManager.Message.ADMIN_CLEAR_TAG.getMessage()
                .replace("%player%", args[1]);
        Main.sendMessage(sender, true, message);
    }
}
