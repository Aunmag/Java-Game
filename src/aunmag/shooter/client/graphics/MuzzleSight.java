package aunmag.shooter.client.graphics;

import aunmag.nightingale.Application;
import aunmag.nightingale.Camera;
import aunmag.nightingale.utilities.UtilsGraphics;
import aunmag.nightingale.utilities.UtilsMath;
import aunmag.shooter.sprites.Actor;
import org.lwjgl.opengl.GL11;

public class MuzzleSight {

    private Actor shooter;
    private float length = 10;
    private final float OFFSET_Y_MIN = 0;
    private final float OFFSET_Y_MAX = 3200;

    private float offsetY = OFFSET_Y_MIN;

    public MuzzleSight(Actor shooter) {
        this.shooter = shooter;
    }

    public void update() {
        if (shooter.isAiming.isCompitelyOff()) {
            offsetY = 0;
            return;
        }

        float velocity = Application.getInput().getMouseVelocity().y();
        float acceleration = 1f;
        float smoothRange = 8f;

        if (offsetY < OFFSET_Y_MIN + smoothRange) {
            acceleration = offsetY / smoothRange;
        } else if (OFFSET_Y_MAX - smoothRange < offsetY) {
            acceleration = (OFFSET_Y_MAX - offsetY) / smoothRange;
        }

        float accelerationMin = 0.001f;
        if (acceleration < accelerationMin) {
            acceleration = accelerationMin;
        }

        offsetY += velocity * acceleration;

        if (offsetY < OFFSET_Y_MIN) {
            offsetY = OFFSET_Y_MIN;
        } else if (OFFSET_Y_MAX < offsetY) {
            offsetY = OFFSET_Y_MAX;
        }

        Application.getCamera().addOffsetYTemporary(offsetY * shooter.isAiming.getValueCurrent());
    }

    public void render() {
        if (shooter.isAiming.isCompitelyOff()) {
            return;
        }

        GL11.glColor4f(1f, 1f, 1f, shooter.isAiming.getValueCurrent());

        float cos = (float) Math.cos(shooter.getRadians());
        float sin = (float) Math.sin(shooter.getRadians());
        float cosSide = (float) Math.cos(shooter.getRadians() + UtilsMath.PIx0_5);
        float sinSide = (float) Math.sin(shooter.getRadians() + UtilsMath.PIx0_5);

        Camera camera = Application.getCamera();
        float distance = camera.getOffsetYBase() + camera.getOffsetYTemporary();
        distance /= camera.getZoomView();
        float x = shooter.getX() + distance * cos;
        float y = shooter.getY() + distance * sin;

        float length = this.length / camera.getZoomView();
        float offsetA = length / 2f;
        float offsetB = offsetA + length;

        UtilsGraphics.drawPrepare();

        float offsetACos = offsetA * cos;
        float offsetASin = offsetA * sin;
        float offsetBCos = offsetB * cos;
        float offsetBSin = offsetB * sin;
        UtilsGraphics.drawLine(x + offsetACos, y + offsetASin, x + offsetBCos, y + offsetBSin, true);
        UtilsGraphics.drawLine(x - offsetACos, y - offsetASin, x - offsetBCos, y - offsetBSin, true);

        offsetACos = offsetA * cosSide;
        offsetASin = offsetA * sinSide;
        offsetBCos = offsetB * cosSide;
        offsetBSin = offsetB * sinSide;
        UtilsGraphics.drawLine(x + offsetACos, y + offsetASin, x + offsetBCos, y + offsetBSin, true);
        UtilsGraphics.drawLine(x - offsetACos, y - offsetASin, x - offsetBCos, y - offsetBSin, true);
    }

}
