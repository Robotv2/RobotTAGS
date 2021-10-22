package fr.robot.robottags.commands.subs;

import fr.robot.robottags.Main;
import fr.robot.robottags.manager.MessageManager;
import org.bukkit.command.CommandSender;

public class Reload {

    public void execute(CommandSender sender, String[] args) {
        if(!Main.hasPermission(sender, "robottags.reload")) {
            MessageManager.Message.NO_PERMISSION.send(sender);
            return;
        }

        Main.getInstance().onReload();
    }
}
