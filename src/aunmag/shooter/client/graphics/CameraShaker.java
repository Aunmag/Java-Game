package aunmag.shooter.client.graphics;

import aunmag.nightingale.Application;
import aunmag.nightingale.utilities.FluidValue;
import aunmag.shooter.world.WorldTime;

public class CameraShaker {

    private static FluidValue fluidRadians;
    private static final int timeUp = 40;
    private static final int timeDown = timeUp * 8;

    static {
        float flexDegree = 0.8f;
        fluidRadians = new FluidValue(timeUp);
        fluidRadians.setFlexDegree(flexDegree);
    }

    public static void shake(float force) {
        fluidRadians.setTimeDuration(timeUp);
        fluidRadians.setValueTarget(force, WorldTime.getCurrentMilliseconds());
    }

    public static void update() {
        fluidRadians.update(WorldTime.getCurrentMilliseconds());
        if (fluidRadians.isTargetReached() && fluidRadians.getValueTarget() != 0) {
            fluidRadians.setTimeDuration(timeDown);
            fluidRadians.setValueTarget(0, WorldTime.getCurrentMilliseconds());
        }

        Application.getCamera().addRadiansOffset(fluidRadians.getValueCurrent());
    }

}
