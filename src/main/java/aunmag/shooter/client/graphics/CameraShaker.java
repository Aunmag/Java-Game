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
        fluidRadians = new FluidValue(timeUp);
        fluidRadians.setFlexDegree(flexDegree);
    }

    public static void shake(float force) {
        fluidRadians.setTimeDuration(timeUp);
        fluidRadians.setValueTarget(force, Game.getWorld().getTime().getCurrent());
    }

    public static void update() {
        fluidRadians.update(Game.getWorld().getTime().getCurrent());
        if (fluidRadians.isTargetReached() && fluidRadians.getValueTarget() != 0) {
            fluidRadians.setTimeDuration(timeDown);
            fluidRadians.setValueTarget(0, Game.getWorld().getTime().getCurrent());
        }

        Application.getCamera().addRadiansOffset(fluidRadians.getValueCurrent());
    }

}
