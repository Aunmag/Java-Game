package aunmag.shooter.client.graphics;

import aunmag.nightingale.Application;
import aunmag.nightingale.Camera;
import aunmag.nightingale.utilities.FluidValue;
import aunmag.nightingale.utilities.UtilsMath;

public class CameraShaker {

    private static FluidValue radians;
    private static final float timeUp = 0.04f;
    private static final float timeDown = timeUp * 8;

    static {
        float flexDegree = 0.8f;
        radians = new FluidValue(Application.time, timeUp); // TODO: Use world time
        radians.setFlexDegree(flexDegree);
    }

    public static void shake(float force) {
        radians.timer.setDuration(timeUp);
        radians.setTarget(force);
    }

    public static void update() {
        Camera camera = Application.getCamera();

        radians.update();
        if (radians.isTargetReached() && radians.getTarget() != 0) {
            radians.timer.setDuration(timeDown);
            radians.setTarget(0);
        }

        if (radians.getCurrent() == 0.0f) {
            return;
        }

        float radiansCamera = camera.getRadians();
        float radiansShaker = radians.getCurrent();
        float radiansSum = UtilsMath.correctRadians(radiansCamera + radiansShaker);

        camera.setRadians(radiansSum);
        camera.mount.radians = radiansSum;
        camera.mount.apply();
    }

}
