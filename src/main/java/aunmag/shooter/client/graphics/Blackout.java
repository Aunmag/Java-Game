package aunmag.shooter.client.graphics;

import aunmag.nightingale.Application;
import aunmag.nightingale.structures.Texture;
import aunmag.nightingale.utilities.FluidValue;
import aunmag.nightingale.utilities.UtilsGraphics;
import aunmag.nightingale.utilities.UtilsMath;
import aunmag.shooter.actor.Actor;
import org.lwjgl.opengl.GL11;

public class Blackout {

    private final Actor player;
    private final Texture texture;
    private final float healthMax = 1;
    private final float healthThird = healthMax / 3f;
    private float healthLast = healthMax;
    private final float timeHurtAscent = 0.06f;
    private final float timeHurtDecline = 0.48f;
    private final FluidValue intensity;

    public Blackout(Actor player) {
        this.player = player;

        texture = Texture.getOrCreate("images/gui/blackout1600", false, false);
        texture.scaleAsWindow();
        intensity = new FluidValue(player.world.getTime(), timeHurtAscent);
    }

    public void render() {
        renderBoundaries();

        UtilsGraphics.drawPrepare();
        renderRectangle();
        renderDynamicRectangle();
    }

    private void renderBoundaries() {
        float alpha = 1 - player.getHealth() / 1.4f;

        texture.bind();
        Application.getShader().setUniformProjection(Application.getWindow().projection);
        Application.getShader().setUniformColour(1, 1, 1, alpha);
        texture.render();
    }

    private void renderRectangle() {
        float healthDoubleThird = healthThird * 2;

        if (player.getHealth() <= healthDoubleThird) {
            float alpha = (healthMax - player.getHealth() / healthDoubleThird) * 0.9f;
            float width = Application.getWindow().getWidth();
            float height = Application.getWindow().getHeight();
            GL11.glColor4f(0f, 0f, 0f, alpha);
            UtilsGraphics.drawQuad(0, 0, width, height, true, false);
        }
    }

    private void renderDynamicRectangle() {
        intensity.update();
        if (player.getHealth() != healthLast) {
            if (intensity.timer.getDuration() != timeHurtAscent) {
                intensity.timer.setDuration(timeHurtAscent);
            }
            float intensityTarget = Math.abs(healthLast - player.getHealth()) * 4;
            intensity.setTarget(UtilsMath.limitNumber(intensityTarget, 0, 1));
            if (intensity.isTargetReached()) {
                healthLast = player.getHealth();
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
