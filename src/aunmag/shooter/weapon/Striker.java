package aunmag.shooter.weapon;

import aunmag.nightingale.utilities.TimerNext;
import aunmag.shooter.client.Game;
import aunmag.shooter.world.World;

public class Striker {

    public final World world;
    private final TimerNext nextShotTime;

    public Striker(World world, int shotsPerMinute) {
        this.world = world;
        nextShotTime = new TimerNext(60f / (float) shotsPerMinute);
    }

    boolean isCocked() {
        nextShotTime.update(world.getTime().getCurrent());
        return nextShotTime.isNow();
    }

}
