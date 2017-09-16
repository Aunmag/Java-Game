package client.graphics;

import client.Application;
import client.Display;
import nightingale.utilities.FloatSmooth;
import nightingale.utilities.UtilsLog;
import utilities.UtilsGraphics;
import sprites.Actor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Aunmag on 2016.11.13.
 */

public class Blackout {

    private static Image image;
    private static final float healthMax = 1;
    private static final float healthThird = healthMax / 3f;
    private static float healthCurrent = healthMax;
    private static float healthLast = healthCurrent;
    private static final int timeHurtAscent = 60;
    private static final int timeHurtDecline = 480;
    private static FloatSmooth intensity = new FloatSmooth(timeHurtAscent);

    static {
        try {
            image = ImageIO.read(Blackout.class.getResource("/images/gui/blackout1600.png"));
            image = image.getScaledInstance(
                    Display.getWidth(),
                    Display.getHeight(),
                    BufferedImage.SCALE_SMOOTH
            );
        } catch (IOException e) {
            UtilsLog.log("Error", "Can't load blackout image.", e.toString());
            image = null;
        }
    }

    public static void render() {
        healthCurrent = correctHealth(Actor.getPlayer().getHealth());

        Display.getGraphicsHud().setColor(Color.BLACK);

        if (image != null) {
            renderBoundaries();
        }
        renderRectangle();
        renderDynamicRectangle();

        UtilsGraphics.resetAlpha(Display.getGraphicsHud());
    }

    private static void renderBoundaries() {
        float alpha = 1 - healthCurrent / 1.4f;
        Display.getGraphicsHud().setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER,
                alpha
        ));
        Display.getGraphicsHud().drawImage(image, 0, 0, null);
    }

    private static void renderRectangle() {
        float healthDoubleThird = healthThird * 2;

        if (healthCurrent <= healthDoubleThird) {
            float alpha = (healthMax - healthCurrent / healthDoubleThird) * 0.9f;
            Display.getGraphicsHud().setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER,
                    alpha
            ));
            Display.getGraphicsHud().fillRect(0, 0, Display.getWidth(), Display.getHeight());
        }
    }

    private static void renderDynamicRectangle() {
        long timeWorld = Application.getTimeCurrent();

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

        Display.getGraphicsHud().setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER,
                alpha
        ));
        Display.getGraphicsHud().fillRect(0, 0, Display.getWidth(), Display.getHeight());
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
