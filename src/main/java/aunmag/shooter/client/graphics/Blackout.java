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
    private float healthLast = 1.0f;
    private final FluidValue intensity;
    private final float intensityFactor = 4.0f;
    private final float timeFadeIn = 0.06f;
    private final float timeFadeOut = timeFadeIn * 8;
    private final float thresholdIsInjured = 2.0f / 3.0f;

    public Blackout(Actor player) {
        this.player = player;

        texture = Texture.getOrCreate("images/gui/blackout1600", false, false);
        texture.scaleAsWindow();
        intensity = new FluidValue(player.world.getTime(), timeFadeIn);
    }

    public void render() {
        renderBoundaries();
        UtilsGraphics.drawPrepare();
        renderRectangle();
    }

    private void renderBoundaries() {
        float alpha = 1 - player.getHealth() / 1.4f;

        texture.bind();
        Application.getShader().setUniformProjection(Application.getWindow().projection);
        Application.getShader().setUniformColour(1, 1, 1, alpha);
        texture.render();
    }

    private void renderRectangle() {
        updateIntensity();

        float hurt = UtilsMath.limitNumber(intensity.getCurrent(), 0, 1);
        float wound = 0;

        if (player.getHealth() <= thresholdIsInjured) {
            wound = (1.0f - player.getHealth() / thresholdIsInjured) * 0.9f;
        }

        float alpha = hurt + wound - (hurt * wound);
        GL11.glColor4f(0f, 0f, 0f, UtilsMath.limitNumber(alpha, 0, 1));
        UtilsGraphics.fillScreen();
    }

    private void updateIntensity() {
        intensity.update();

        float damage = healthLast - player.getHealth();
        healthLast = player.getHealth();

        if (damage > 0) {
            intensity.timer.setDuration(timeFadeIn);
            intensity.setTarget(damage * intensityFactor + intensity.getTarget());
        }

        if (intensity.getTarget() != 0 && intensity.isTargetReached()) {
            intensity.timer.setDuration(timeFadeOut);
            intensity.setTarget(0);
        }
    }

}
