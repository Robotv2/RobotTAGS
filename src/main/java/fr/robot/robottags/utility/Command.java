package fr.robot.robottags.utility;

import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static fr.robot.robottags.utility.color.ColorAPI.colorize;

public abstract class Command implements CommandExecutor, TabCompleter {

    protected String name;

    public Command(JavaPlugin plugin, String name) {
        this.name = name;
        PluginCommand command = plugin.getCommand(name);
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        } else
            throw new CommandException(colorize("Vous n'avez pas enregistrer la commande " +  name + " dans le plugin.yml"));
    }

    public abstract void execute(CommandSender sender, String[] args);

    public abstract String getPermission();
    public abstract String getPermissionMessage();

    public abstract boolean canConsoleExecute();
    public abstract String getNotConsoleMessage();

    public abstract List<String> getSubsCommands();
    public abstract boolean enableTabCompleter();

    private boolean hasPermission(CommandSender sender) {
        return (getPermission() == null || sender.hasPermission(getPermission()));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, String[] args) {
        if(!(sender instanceof org.bukkit.entity.Player)) {
            if(!canConsoleExecute()) {
                sender.sendMessage(colorize(getNotConsoleMessage()));
                return false;
            }
        }
        if(getPermission() != null && !sender.hasPermission(getPermission())) {
            sender.sendMessage(colorize(getPermissionMessage()));
            return false;
        }

        execute(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command,  String alias, String[] args) {
        if(enableTabCompleter() && hasPermission(sender) && !getSubsCommands().isEmpty()) {
            if (args[0].length() == 0) {
                return getSubsCommands();
            }
            if (args.length == 1) {
                List<String> result = new ArrayList<>();
                for (int i = 0; i < getSubsCommands().size(); ++i) {
                    if (getSubsCommands().get(i).startsWith(args[0])) {
                        result.add(getSubsCommands().get(i));
                    }
                }
                return result;
            }
            return null;
        }
        return null;
    }
}
