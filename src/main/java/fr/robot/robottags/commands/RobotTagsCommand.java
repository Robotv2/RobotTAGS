package fr.robot.robottags.commands;

import fr.robot.robottags.Main;
import fr.robot.robottags.commands.subs.ClearTag;
import fr.robot.robottags.commands.subs.Reload;
import fr.robot.robottags.commands.subs.SetTag;
import fr.robot.robottags.manager.MessageManager;
import fr.robot.robottags.ui.MenuGUI;
import fr.robot.robottags.utility.Command;
import fr.robot.robottags.utility.ui.GuiAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class RobotTagsCommand extends Command {

    private final SetTag setTag;
    private final ClearTag clearTag;
    private final Reload reload;
    public RobotTagsCommand(JavaPlugin plugin, String name) {
        super(plugin, name);
        this.setTag = new SetTag();
        this.clearTag = new ClearTag();
        this.reload = new Reload();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            if(!(sender instanceof Player))
                MessageManager.Message.NOT_FROM_CONSOLE.send(sender);
            else if(!Main.hasPermission(sender, "robottags.opengui"))
                MessageManager.Message.NO_PERMISSION.send(sender);
            else {
                Player opener = (Player) sender;
                GuiAPI.open(opener, MenuGUI.class);
            }
            return;
        }

        switch(args[0].toLowerCase()) {
            case "set":
                setTag.execute(sender, args);
                break;
            case "clear":
                clearTag.execute(sender, args);
                break;
            case "reload":
                reload.execute(sender, args);
                break;
        }

    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public String getPermissionMessage() {
        return MessageManager.Message.NO_PERMISSION.getMessage();
    }

    @Override
    public boolean canConsoleExecute() {
        return true;
    }

    @Override
    public String getNotConsoleMessage() {
        return "";
    }

    @Override
    public List<String> getSubsCommands() {
        return Arrays.asList("set", "clear","reload");
    }

    @Override
    public boolean enableTabCompleter() {
        return true;
    }
}
