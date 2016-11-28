package client.graphics.effects;

// Created by AunmagUser on 13.11.2016.

import client.Client;
import managers.Log;
import scripts.Inertia;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Blackout {

    private static Image image;
    private static double playerHealthLast;
    private static final double tHurtInertiaAscent = 0.06;
    private static final double tHurtInertiaDecline = 0.48;
    private static double hurtIntensity;
    private static Inertia hurtInertia = new Inertia(tHurtInertiaAscent);
    private static boolean isHurting = false;
    private static boolean isHurtAscenting = false;

    static {

        initialize();

    }

    public static void initialize() {

        try {
            image = ImageIO.read(Blackout.class.getResource("/images/gui/blackout1600.png"));
            image = image.getScaledInstance(Client.getWidth(), Client.getHeight(), BufferedImage.SCALE_SMOOTH);
        } catch (IOException e) {
            Log.log("Error", "Can't load blackout image.", e);
            image = null;
        }

        playerHealthLast = 1;

    }

    public static void render() {

        double alpha;
        double playerHealth = Client.getPlayer().getHealth();

        // Tweak player health:
        if (playerHealth > 1) {
            playerHealth = 1;
        } else if (playerHealth < 0) {
            playerHealth = 0;
        }

        // Set blackout color:
        Client.getHud().setColor(Color.BLACK);

        // Render blackout rectangle:
        if (playerHealth <= 0.66) {
            alpha = (1 - playerHealth / 0.66) * 0.9;
            Client.getHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            Client.getHud().fillRect(0, 0, Client.getWidth(), Client.getHeight());
        }

        // Render blackout boundaries:
        if (image != null) {
            alpha = 1 - playerHealth / 1.4;
            Client.getHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            Client.getHud().drawImage(image, 0, 0, null);
        }

        // Render blackout hurt:

        if (playerHealth != playerHealthLast) {
            if (hurtInertia.getTDuration() != tHurtInertiaAscent) {
                hurtInertia.setTDuration(tHurtInertiaAscent);
            }
            double intensity = Math.abs(playerHealthLast - playerHealth) * 8 % 1;
            alpha = hurtInertia.update(1, intensity);
            if (!hurtInertia.getState()) {
                playerHealthLast = playerHealth;
                hurtInertia.setTDuration(tHurtInertiaDecline);
            }
        } else {
            alpha = hurtInertia.update(1, 0);
        }

        Client.getHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
        Client.getHud().fillRect(0, 0, Client.getWidth(), Client.getHeight());

        // Reset default hud alpha:

        Client.getHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

    }

}
