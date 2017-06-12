package client.graphics.effects;

// Created by AunmagUser on 13.11.2016.

import client.DataManager;
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
            image = image.getScaledInstance(DataManager.getDisplayWidth(), DataManager.getDisplayHeight(), BufferedImage.SCALE_SMOOTH);
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
        DataManager.getHud().setColor(Color.BLACK);

        // Render blackout rectangle:
        if (playerHealth <= 0.66) {
            alpha = (1 - playerHealth / 0.66f) * 0.9f;
            DataManager.getHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            DataManager.getHud().fillRect(0, 0, DataManager.getDisplayWidth(), DataManager.getDisplayHeight());
        }

        // Render blackout boundaries:
        if (image != null) {
            alpha = 1 - playerHealth / 1.4f;
            DataManager.getHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            DataManager.getHud().drawImage(image, 0, 0, null);
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

        DataManager.getHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
        DataManager.getHud().fillRect(0, 0, DataManager.getDisplayWidth(), DataManager.getDisplayHeight());

        // Reset default hud alpha:

        DataManager.getHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

    }

}
