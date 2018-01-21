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
    private final float thresholdIsInjured = 2.0f / 3.0f;
    private final FluidValue hurt;
    private final float hurtFactor = 4.0f;
    private final float hurtTimeFadeIn = 0.06f;
    private final float hurtTimeFadeOut = hurtTimeFadeIn * 8;

    public Blackout(Actor player) {
        this.player = player;

        texture = Texture.getOrCreate("images/gui/blackout1600", false, false);
        texture.scaleAsWindow();
        hurt = new FluidValue(player.world.getTime(), hurtTimeFadeIn);
    }

    public void render() {
        updateHurt();

        renderBoundaries();
        UtilsGraphics.drawPrepare();
        renderRectangle();
    }

    private void updateHurt() {
        hurt.update();

        float damage = healthLast - player.getHealth();
        healthLast = player.getHealth();

        if (damage > 0) {
            hurt.timer.setDuration(hurtTimeFadeIn);
            hurt.setTarget(damage * hurtFactor + hurt.getTarget());
        }

        if (hurt.getTarget() != 0 && hurt.isTargetReached()) {
            hurt.timer.setDuration(hurtTimeFadeOut);
            hurt.setTarget(0);
        }
    }

    private void renderBoundaries() {
        float alpha = 1 - player.getHealth() / 1.4f;

        texture.bind();
        Application.getShader().setUniformProjection(Application.getWindow().projection);
        Application.getShader().setUniformColour(1, 1, 1, alpha);
        texture.render();
    }

    private void renderRectangle() {
        float alphaHurt = UtilsMath.limitNumber(hurt.getCurrent(), 0, 1);
        float alphaWound = 0;

        if (player.getHealth() <= thresholdIsInjured) {
            alphaWound = (1.0f - player.getHealth() / thresholdIsInjured) * 0.9f;
        }

        float alpha = alphaHurt + alphaWound - (alphaHurt * alphaWound);
        GL11.glColor4f(0f, 0f, 0f, UtilsMath.limitNumber(alpha, 0, 1));
        UtilsGraphics.fillScreen();
    }

}
