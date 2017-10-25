package aunmag.shooter.weapon;

import aunmag.nightingale.utilities.TimerNext;

public class WeaponStriker {

    private TimerNext nextShootTime;

    public WeaponStriker(int rate) {
        nextShootTime = new TimerNext(rate);
    }

    boolean isCocked() {
        nextShootTime.update(System.currentTimeMillis());
        return nextShootTime.isNow();
    }

}
