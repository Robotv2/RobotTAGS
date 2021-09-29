package fr.robot.robottags.utility.cooldown;

import java.time.Instant;

public class Cooldown {

    private String name;

    private Long instant;
    private Integer seconds;

    public Cooldown(String name) {
        this.name = name;
    }

    public void setCooldown(int seconds) {
        this.instant = Instant.now().toEpochMilli();
        this.seconds = seconds;
    }

    public int getRemainingCooldown() {
        if(instant == null) return 0;
        return (int) ((Instant.now().toEpochMilli() - instant) / 1000);
    }

    public boolean isCooldownOver() {
        if(instant == null) return true;
        if(seconds == null) return true;
        return ((Instant.now().toEpochMilli() - instant) / 1000) > seconds;
    }

    public void clearCooldown() {
        this.instant = null;
        this.seconds = null;
    }
}
