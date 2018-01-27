package aunmag.shooter.environment.weapon.components;

import aunmag.nightingale.utilities.Timer;
import aunmag.shooter.environment.World;

public class Striker {

    public final World world;
    private final Timer nextShotTime;

    public Striker(World world, int shotsPerMinute) {
        this.world = world;
        nextShotTime = new Timer(world.getTime(), 60f / (float) shotsPerMinute);
    }

    public boolean isCocked() {
        boolean isCocked = nextShotTime.isDone();
        nextShotTime.next(true);
        return isCocked;
    }

}
