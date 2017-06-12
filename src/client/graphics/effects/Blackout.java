package client.graphics.effects;

// Created by AunmagUser on 13.11.2016.

import client.DataManager;
import client.Display;
import managers.Log;
import scripts.Inertia;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Blackout {

    private static Image image;
    private static float playerHealthLast;
    private static final float tHurtInertiaAscent = 0.06f;
    private static final float tHurtInertiaDecline = 0.48f;
    private static float hurtIntensity;
    private static Inertia hurtInertia = new Inertia(tHurtInertiaAscent);
    private static boolean isHurting = false;
    private static boolean isHurtAscenting = false;

    static {

        initialize();

    }

    public static void initialize() {

        try {
            image = ImageIO.read(Blackout.class.getResource("/images/gui/blackout1600.png"));
            image = image.getScaledInstance(Display.getWidth(), Display.getHeight(), BufferedImage.SCALE_SMOOTH);
        } catch (IOException e) {
            Log.log("Error", "Can't load blackout image.", e.toString());
            image = null;
        }

        playerHealthLast = 1;

    }

    public static void render() {

        float alpha;
        float playerHealth = DataManager.getPlayer().getHealth();

        // Tweak player health:
        if (playerHealth > 1) {
            playerHealth = 1;
        } else if (playerHealth < 0) {
            playerHealth = 0;
        }

        // Set blackout color:
        Display.getGraphicsHud().setColor(Color.BLACK);

        // Render blackout rectangle:
        if (playerHealth <= 0.66) {
            alpha = (1 - playerHealth / 0.66f) * 0.9f;
            Display.getGraphicsHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            Display.getGraphicsHud().fillRect(0, 0, Display.getWidth(), Display.getHeight());
        }

        // Render blackout boundaries:
        if (image != null) {
            alpha = 1 - playerHealth / 1.4f;
            Display.getGraphicsHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            Display.getGraphicsHud().drawImage(image, 0, 0, null);
        }

        // Render blackout hurt:

        if (playerHealth != playerHealthLast) {
            if (hurtInertia.getTimeDuration() != tHurtInertiaAscent) {
                hurtInertia.setTimeDuration(tHurtInertiaAscent);
            }
            float intensity = Math.abs(playerHealthLast - playerHealth) * 8 % 1;
            alpha = hurtInertia.update(intensity);
            if (!hurtInertia.getIsProcessing()) {
                playerHealthLast = playerHealth;
                hurtInertia.setTimeDuration(tHurtInertiaDecline);
            }
        } else {
            alpha = hurtInertia.update(0);
        }

        Display.getGraphicsHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
        Display.getGraphicsHud().fillRect(0, 0, Display.getWidth(), Display.getHeight());

        // Reset default hud alpha:

        Display.getGraphicsHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

    }

}
