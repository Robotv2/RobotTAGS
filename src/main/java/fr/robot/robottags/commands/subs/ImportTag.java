package fr.robot.robottags.commands.subs;

import fr.robot.robottags.Main;
import fr.robot.robottags.commands.AbstractSub;
import fr.robot.robottags.importer.AbstractImporter;
import fr.robot.robottags.importer.ImporterManager;
import fr.robot.robottags.manager.MessageManager;
import org.bukkit.command.CommandSender;

import static fr.robot.robottags.Main.sendMessage;

public class ImportTag implements AbstractSub  {
    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!Main.hasPermission(sender, "robottags.import")) {
            MessageManager.Message.NO_PERMISSION.send(sender);
            return;
        }

        if(args.length != 2) {
            sendMessage(sender, true, "&cUSAGE: /robottags import <plugin name>");
            return;
        }

        ImporterManager.ImportType type;

        try {
            String typeStr = args[1].toUpperCase();
            type = ImporterManager.ImportType.valueOf(typeStr);
        } catch (Exception e) {
            sendMessage(sender, true, "&c'" + args[1] + "' isn't a valid plugin name !");
            return;
        }

        AbstractImporter importer = ImporterManager.getImporter(type);

        if(importer == null) {
            sendMessage(sender, true, "&cThe plugin you're searching to import tags from isn't loaded on the server !");
            return;
        }

        ImporterManager.importTag(importer);
        sendMessage(sender, true, "&7Importation has started, &cplease check your console.");
    }
}
