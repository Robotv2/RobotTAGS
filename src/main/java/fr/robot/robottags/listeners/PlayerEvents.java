package fr.robot.robottags.listeners;

import fr.robot.robottags.manager.MysqlManager;
import fr.robot.robottags.manager.PlayerManager;
import fr.robot.robottags.manager.StorageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if(StorageManager.getMode() == StorageManager.DBMODE.MYSQL)
            MysqlManager.createPlayer(player.getUniqueId());
        PlayerManager.load(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PlayerManager.save(player);
    }
}
