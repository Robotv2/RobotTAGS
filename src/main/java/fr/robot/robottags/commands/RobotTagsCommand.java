package fr.robot.robottags.commands;

import com.google.common.base.Strings;
import fr.robot.robottags.commands.subs.ClearTag;
import fr.robot.robottags.commands.subs.ImportTag;
import fr.robot.robottags.commands.subs.Reload;
import fr.robot.robottags.commands.subs.SetTag;
import fr.robot.robottags.importer.ImporterManager;
import fr.robot.robottags.manager.MessageManager;
import fr.robot.robottags.manager.TagManager;
import fr.robot.robottags.ui.MenuGUI;
import fr.robot.robottags.utility.ui.GuiAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static fr.robot.robottags.Main.hasPermission;

public class RobotTagsCommand implements TabExecutor, TabCompleter {

    private final AbstractSub setTag;
    private final AbstractSub clearTag;
    private final AbstractSub reload;
    private final AbstractSub importTag;
    public RobotTagsCommand() {
        this.setTag = new SetTag();
        this.clearTag = new ClearTag();
        this.reload = new Reload();
        this.importTag = new ImportTag();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            if(!(sender instanceof Player))
                MessageManager.Message.NOT_FROM_CONSOLE.send(sender);
            else if(!hasPermission(sender, "robottags.opengui"))
                MessageManager.Message.NO_PERMISSION.send(sender);
            else {
                Player opener = (Player) sender;
                GuiAPI.open(opener, MenuGUI.class);
            }
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "set" -> setTag.execute(sender, args);
            case "clear" -> clearTag.execute(sender, args);
            case "reload" -> reload.execute(sender, args);
            case "import" -> importTag.execute(sender, args);
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> subs = new ArrayList<>();

        if(hasPermission(sender, "robottags.clear"))
            subs.add("clear");
        if(hasPermission(sender, "robottags.reload"))
            subs.add("reload");
        if(hasPermission(sender, "robottags.set"))
            subs.add("set");
        if(hasPermission(sender, "robottags.import"))
            subs.add("import");

        if(args.length == 0)
            return subs;
        if(args.length == 1)
            return subs.stream().filter(arg -> arg.startsWith(args[0].toLowerCase())).collect(Collectors.toList());

        switch(args[0].toLowerCase()) {
            case "set" -> {
                if (args.length == 3) {
                    if (Strings.isNullOrEmpty(args[2]))
                        return TagManager.getTagIds().stream().toList();
                    else
                        return TagManager.getTagIds().stream().filter(arg -> arg.startsWith(args[2].toLowerCase())).collect(Collectors.toList());
                }
            }

            case "import" -> {
                if (args.length == 2) {
                    if (Strings.isNullOrEmpty(args[1]))
                        return ImporterManager.getValues();
                    else
                        return ImporterManager.getValues().stream().filter(arg -> arg.startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                }
            }
        }
        return Collections.emptyList();
    }
}
