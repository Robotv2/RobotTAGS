package fr.robot.robottags.listeners;

import fr.robot.robottags.manager.MysqlManager;
import fr.robot.robottags.manager.PlayerManager;
import fr.robot.robottags.manager.StorageManager;
import fr.robot.robottags.utility.TaskAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        TaskAPI.runTaskLaterAsync(() -> {

            if(StorageManager.getMode() == StorageManager.DBMODE.MYSQL)
                MysqlManager.createPlayer(player.getUniqueId());
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
}
