package aunmag.shooter.weapon;

import aunmag.nightingale.utilities.TimerNext;
import aunmag.shooter.client.Game;

public class Striker {

    private final TimerNext nextShotTime;

    public Striker(int shotsPerMinute) {
        nextShotTime = new TimerNext(60f / (float) shotsPerMinute);
    }

    boolean isCocked() {
        nextShotTime.update(Game.getWorld().getTime().getCurrent());
        return nextShotTime.isNow();
    }

}
