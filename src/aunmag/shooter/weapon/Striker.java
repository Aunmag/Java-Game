package aunmag.shooter.weapon;

import aunmag.nightingale.utilities.TimerNext;

public class Striker {

    private TimerNext nextShootTime;

    public Striker(int rate) {
        nextShootTime = new TimerNext(rate);
    }

    boolean isCocked() {
        nextShootTime.update(System.currentTimeMillis());
        return nextShootTime.isNow();
    }

}
