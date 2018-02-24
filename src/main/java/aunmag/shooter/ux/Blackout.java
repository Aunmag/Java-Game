package aunmag.shooter.ux;

import aunmag.nightingale.Application;
import aunmag.nightingale.structures.Texture;
import aunmag.nightingale.utilities.FluidValue;
import aunmag.nightingale.utilities.UtilsGraphics;
import aunmag.nightingale.utilities.UtilsMath;
import aunmag.shooter.environment.actor.Actor;
import org.lwjgl.opengl.GL11;

public class Blackout {

    private final Actor player;
    private final Texture texture;
    private float healthLast = 1.0f;
    private final FluidValue hurt;
    private final float hurtFactor = 4.0f;
    private final float hurtTimeFadeIn = 0.06f;
    private final float hurtTimeFadeOut = hurtTimeFadeIn * 8;

    public Blackout(Actor player) {
        this.player = player;

        texture = Texture.getOrCreate("images/gui/blackout1600", Texture.Type.STRETCHED);
        hurt = new FluidValue(player.world.getTime(), hurtTimeFadeIn);
    }

    public void render() {
        updateHurt();
        renderRectangle();
        renderBoundaries();
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

    /**
     * ~/src/python/blackout_wound.py
     */
    private void renderRectangle() {
        float alphaHurt = hurt.getCurrent();
        float alphaWound = (float) Math.pow(1.0f - player.getHealth(), 3);
        float alpha = alphaHurt + alphaWound - (alphaHurt * alphaWound);
        GL11.glColor4f(0f, 0f, 0f, UtilsMath.limitNumber(alpha, 0, 1));
        UtilsGraphics.fillScreen();
    }

    private void renderBoundaries() {
        float alpha = 1 - player.getHealth() / 1.4f;
        Application.getShader().bind();
        Application.getShader().setUniformColour(1, 1, 1, alpha);
        Application.getShader().setUniformProjection(Application.getWindow().projection);
        texture.bind();
        texture.render();
    }

}
