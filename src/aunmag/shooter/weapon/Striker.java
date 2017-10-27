package aunmag.shooter.weapon;

import aunmag.nightingale.utilities.TimerNext;

public class Striker {

    private static final int convert = 1000 * 60;

    private final TimerNext nextShotTime;

    public Striker(int shotsPerMinute) {
        nextShotTime = new TimerNext(convert / shotsPerMinute);
    }

    boolean isCocked() {
        nextShotTime.update(System.currentTimeMillis());
        return nextShotTime.isNow();
    }

}
