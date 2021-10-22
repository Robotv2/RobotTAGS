package fr.robot.robottags.commands.subs;

import fr.robot.robottags.Main;
import fr.robot.robottags.manager.MessageManager;
import fr.robot.robottags.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import static fr.robot.robottags.Main.sendMessage;

public class ClearTag {

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

        if(!target.hasPlayedBefore()) {
            sendMessage(sender, true, "&cThis player never logged in.");
            return;
        }

        PlayerManager.clear(target.getUniqueId());
        //TODO envoyer le message
    }
}
