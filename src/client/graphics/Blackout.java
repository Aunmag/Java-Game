package client.graphics;

import client.Display;
import managers.Log;
import managers.Inertia;
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
    private static final float timeHurtAscent = 0.06f;
    private static final float timeHurtDecline = 0.48f;
    private static Inertia inertia = new Inertia(timeHurtAscent);

    static {
        try {
            image = ImageIO.read(Blackout.class.getResource("/images/gui/blackout1600.png"));
            image = image.getScaledInstance(
                    Display.getWidth(),
                    Display.getHeight(),
                    BufferedImage.SCALE_SMOOTH
            );
        } catch (IOException e) {
            Log.log("Error", "Can't load blackout image.", e.toString());
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
        float alpha;

        if (healthCurrent != healthLast) {
            if (inertia.getTimeDuration() != timeHurtAscent) {
                inertia.setTimeDuration(timeHurtAscent);
            }
            float intensity = Math.abs(healthLast - healthCurrent) * 8 % 1;
            alpha = inertia.update(intensity);
            if (!inertia.getIsProcessing()) {
                healthLast = healthCurrent;
                inertia.setTimeDuration(timeHurtDecline);
            }
        } else {
            alpha = inertia.update(0);
        }

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
