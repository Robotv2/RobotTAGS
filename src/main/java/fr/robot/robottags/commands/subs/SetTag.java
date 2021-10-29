package fr.robot.robottags.commands.subs;

import fr.robot.robottags.Main;
import fr.robot.robottags.manager.MessageManager;
import fr.robot.robottags.manager.PlayerManager;
import fr.robot.robottags.manager.TagManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import static fr.robot.robottags.Main.sendMessage;

public class SetTag {

    public void execute(CommandSender sender, String[] args) {
        if(!Main.hasPermission(sender, "robottags.set")) {
            MessageManager.Message.NO_PERMISSION.send(sender);
            return;
        }
        if(args.length != 3) {
            sendMessage(sender, true,"&cUSAGE: /robottags set <player> <tags>");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        String tagID = args[2];

        if(!target.isOnline() && !target.hasPlayedBefore()) {
            sendMessage(sender, true, "&cThis player never logged in.");
            return;
        }
        if(!TagManager.exist(tagID)) {
            sendMessage(sender, true, "&cThis tag does not exist.");
            return;
        }

        PlayerManager.setTag(target.getUniqueId(), tagID);

        String message = MessageManager.Message.ADMIN_SET_TAG.getMessage()
                .replace("%player%", args[1]).replace("%new-tag%", tagID);
        Main.sendMessage(sender, true, message);
    }
}
