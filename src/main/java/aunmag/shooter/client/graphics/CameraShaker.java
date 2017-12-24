package aunmag.shooter.client.graphics;

import aunmag.nightingale.Application;
import aunmag.nightingale.utilities.FluidValue;
import aunmag.shooter.client.Game;

public class CameraShaker {

    private static FluidValue fluidRadians;
    private static final float timeUp = 0.04f;
    private static final float timeDown = timeUp * 8;

    static {
        float flexDegree = 0.8f;
        fluidRadians = new FluidValue(Application.time, timeUp); // TODO: Use world time
        fluidRadians.setFlexDegree(flexDegree);
    }

    public static void shake(float force) {
        fluidRadians.timer.setDuration(timeUp);
        fluidRadians.setTarget(force);
    }

    public static void update() {
        fluidRadians.update();
        if (fluidRadians.isTargetReached() && fluidRadians.getTarget() != 0) {
            fluidRadians.timer.setDuration(timeDown);
            fluidRadians.setTarget(0);
        }

        Application.getCamera().addRadiansOffset(fluidRadians.getCurrent());
    }

}
