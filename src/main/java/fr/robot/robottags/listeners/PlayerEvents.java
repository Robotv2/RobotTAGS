package fr.robot.robottags.listeners;

import fr.robot.robottags.manager.ConfigManager;
import fr.robot.robottags.manager.PlayerManager;
import fr.robot.robottags.manager.StorageManager;
import fr.robot.robottags.manager.TagManager;
import fr.robot.robottags.utility.TaskAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        TaskAPI.runTaskLaterAsync(() -> {

            StorageManager.getData().initPlayer(player.getUniqueId());
            PlayerManager.load(player);

        }, 10L);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        TaskAPI.runAsync(() -> {

            PlayerManager.save(player);
            PlayerManager.playerTags.remove(player.getUniqueId());

        });

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {

        if(!ConfigManager.Settings.WANT_ESSENTIALSX_CHAT) return;

        Player player = e.getPlayer();
        String format = e.getFormat();

        format = format.replace("{TAG}", TagManager.getTagDisplaySafe(player));
        e.setFormat(format);
    }
}
