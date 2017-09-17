package game.client.graphics;

import nightingale.Application;
import nightingale.structures.Texture;
import nightingale.utilities.FloatSmooth;
import nightingale.utilities.UtilsGraphics;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import game.sprites.Actor;

public class Blackout {

    private static Texture texture = Texture.getOrCreate("images/gui/blackout1600");
    private static final float healthMax = 1;
    private static final float healthThird = healthMax / 3f;
    private static float healthCurrent = healthMax;
    private static float healthLast = healthCurrent;
    private static final int timeHurtAscent = 60;
    private static final int timeHurtDecline = 480;
    private static FloatSmooth intensity = new FloatSmooth(timeHurtAscent);

    static {
        texture.scaleAsWindow();
    }

    public static void render() {
        healthCurrent = correctHealth(Actor.getPlayer().getHealth());

        renderBoundaries();

        UtilsGraphics.drawPrepare();
        renderRectangle();
        renderDynamicRectangle();
        UtilsGraphics.drawFinish();
    }

    private static void renderBoundaries() {
        float alpha = 1 - healthCurrent / 1.4f;

        texture.bind();
        Application.getShader().setUniformSampler(0);
        Application.getShader().setUniformProjection(
                Application.getWindow().getProjectionCopy() // TODO: Optimize
        );
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
            UtilsGraphics.drawQuad(
                    new Vector2f(0, 0),
                    new Vector2f(width, 0),
                    new Vector2f(width, height),
                    new Vector2f(0, height),
                    true,
                    false
            );
        }
    }

    private static void renderDynamicRectangle() {
        long timeWorld = System.currentTimeMillis();

        if (healthCurrent != healthLast) {
            if (intensity.getTimeDuration() != timeHurtAscent) {
                intensity.setTimeDuration(timeHurtAscent);
            }
            float intensityTarget = Math.abs(healthLast - healthCurrent) * 8 % 1;
            intensity.setValueTarget(intensityTarget, timeWorld);
            if (intensity.isTargetReached()) {
                healthLast = healthCurrent;
                intensity.setTimeDuration(timeHurtDecline);
            }
        } else {
            intensity.setValueTarget(0, timeWorld);
        }

        intensity.update(timeWorld);
        float alpha = intensity.getValueCurrent();

        float width = Application.getWindow().getWidth();
        float height = Application.getWindow().getHeight();
        GL11.glColor4f(0f, 0f, 0f, alpha);
        UtilsGraphics.drawQuad(
                new Vector2f(0, 0),
                new Vector2f(width, 0),
                new Vector2f(width, height),
                new Vector2f(0, height),
                true,
                false
        );
    }

    private static float correctHealth(float health) {
        if (health > 1) {
            health = 1;
        } else if (health < 0) {
            health = 0;
        }

        return health;
    }

}
