package aunmag.shooter.client.graphics;

import aunmag.nightingale.Application;
import aunmag.nightingale.structures.Texture;
import aunmag.nightingale.utilities.FluidValue;
import aunmag.nightingale.utilities.UtilsGraphics;
import aunmag.nightingale.utilities.UtilsMath;
import aunmag.shooter.client.Game;
import org.lwjgl.opengl.GL11;
import aunmag.shooter.actor.Actor;

public class Blackout {

    private static Texture texture;
    private static final float healthMax = 1;
    private static final float healthThird = healthMax / 3f;
    private static float healthCurrent = healthMax;
    private static float healthLast = healthCurrent;
    private static final float timeHurtAscent = 0.06f;
    private static final float timeHurtDecline = 0.48f;
    private static FluidValue intensity;

    static {
        texture = Texture.getOrCreate("images/gui/blackout1600", false, false);
        texture.scaleAsWindow();

         intensity = new FluidValue(Application.time, timeHurtAscent); // TODO: Use world time
    }

    public static void render() {
        healthCurrent = Actor.getPlayer().getHealth();

        renderBoundaries();

        UtilsGraphics.drawPrepare();
        renderRectangle();
        renderDynamicRectangle();
        UtilsGraphics.drawFinish();
    }

    private static void renderBoundaries() {
        float alpha = 1 - healthCurrent / 1.4f;

        texture.bind();
        Application.getShader().setUniformProjection(Application.getWindow().projection);
        Application.getShader().setUniformColour(1, 1, 1, alpha);
        texture.render();
    }

    private static void renderRectangle() {
        float healthDoubleThird = healthThird * 2;

        if (healthCurrent <= healthDoubleThird) {
            float alpha = (healthMax - healthCurrent / healthDoubleThird) * 0.9f;
            float width = Application.getWindow().getWidth();
            float height = Application.getWindow().getHeight();
            GL11.glColor4f(0f, 0f, 0f, alpha);
            UtilsGraphics.drawQuad(0, 0, width, height, true, false);
        }
    }

    private static void renderDynamicRectangle() {
        intensity.update();
        if (healthCurrent != healthLast) {
            if (intensity.timer.getDuration() != timeHurtAscent) {
                intensity.timer.setDuration(timeHurtAscent);
            }
            float intensityTarget = Math.abs(healthLast - healthCurrent) * 4;
            intensity.setTarget(UtilsMath.limitNumber(intensityTarget, 0, 1));
            if (intensity.isTargetReached()) {
                healthLast = healthCurrent;
                intensity.timer.setDuration(timeHurtDecline);
            }
        } else {
            intensity.setTarget(0);
        }

        float alpha = intensity.getCurrent();

        float width = Application.getWindow().getWidth();
        float height = Application.getWindow().getHeight();
        GL11.glColor4f(0f, 0f, 0f, alpha);
        UtilsGraphics.drawQuad(0, 0, width, height, true, false);
    }

}
