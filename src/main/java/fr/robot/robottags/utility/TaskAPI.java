package fr.robot.robottags.utility;

import fr.robot.robottags.Main;
import org.bukkit.scheduler.BukkitTask;

public class TaskAPI {

    public static BukkitTask runTaskLater(Runnable task, Long timer) {
        return Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), task, timer);
    }

    public static BukkitTask runTaskLaterAsync(Runnable task, Long timer) {
        return Main.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(Main.getInstance(), task, timer);
    }

    public static BukkitTask runTask(Runnable task) {
        return Main.getInstance().getServer().getScheduler().runTask(Main.getInstance(), task);
    }

    public static BukkitTask runAsync(Runnable task) {
        return Main.getInstance().getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), task);
    }

    public static BukkitTask runTaskTimer(Runnable task, Long delay, Long timer) {
        return Main.getInstance().getServer().getScheduler().runTaskTimer(Main.getInstance(), task, delay, timer);
    }

    public static BukkitTask runTaskTimerAsync(Runnable task, Long delay, Long timer) {
        return Main.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Main.getInstance(), task, delay, timer);
    }
}
