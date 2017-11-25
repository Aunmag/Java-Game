package aunmag.shooter.weapon;

import aunmag.nightingale.utilities.TimerNext;
import aunmag.shooter.client.Game;

public class Striker {

    private static final int convert = 1000 * 60;

    private final TimerNext nextShotTime;

    public Striker(int shotsPerMinute) {
        nextShotTime = new TimerNext(convert / shotsPerMinute);
    }

    boolean isCocked() {
        nextShotTime.update(Game.getWorld().getTime().getCurrentMilliseconds());
        return nextShotTime.isNow();
    }

}
