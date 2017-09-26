package aunmag.shooter.client.graphics;

import aunmag.nightingale.Application;
import aunmag.nightingale.utilities.UtilsGraphics;
import aunmag.shooter.sprites.Actor;
import org.lwjgl.opengl.GL11;

public class MuzzleSight {

    private Actor shooter;

    public MuzzleSight(Actor shooter) {
        this.shooter = shooter;
    }

    public void render() {
        if (shooter.isAiming.isCompitelyOff()) {
            return;
        }

        float alpha = shooter.isAiming.getValueCurrent();
        GL11.glColor4f(1f, 1f, 1f, alpha);

        float x = Application.getWindow().getCenterX();
        float y = Application.getWindow().getCenterY();

        float length = 10;
        float offsetA = length / 2f;
        float offsetB = offsetA + length;

        UtilsGraphics.drawPrepare();
        UtilsGraphics.drawLine(x, y + offsetA, x, y + offsetB, false);
        UtilsGraphics.drawLine(x, y - offsetA, x, y - offsetB, false);
        UtilsGraphics.drawLine(x + offsetA, y, x + offsetB, y, false);
        UtilsGraphics.drawLine(x - offsetA, y, x - offsetB, y, false);

        GL11.glColor4f(1f, 1f, 1f, alpha / 2f);
        float offsetC = offsetA * 2f + offsetB;
        float offsetD = Application.getCamera().getOffsetYTemporary();
        UtilsGraphics.drawLine(x, y + offsetD, x, y + offsetC, false);
    }

}
