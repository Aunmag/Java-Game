package client.graphics.gui;

// Created by Aunmag on 20.11.2016.

import client.Client;
import managers.image.ImageManager;
import managers.Log;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

abstract class MenuAbstract {

    private Image wallpaper;

    MenuAbstract(String wallpaperPath) {

        try {
            wallpaper = ImageIO.read(ImageManager.class.getResource(wallpaperPath));
            wallpaper = wallpaper.getScaledInstance(Client.getDisplayWidth(), Client.getDisplayHeight(), BufferedImage.SCALE_SMOOTH);
        } catch (Exception e) {
            wallpaper = null;
            Log.log("ImageManager error", "Can't load \"" + wallpaperPath+ "\" image.", e.toString());
        }

    }

    void renderWallpaper() {

        if (wallpaper == null) {
            return;
        }

        Client.getHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.3));
        Client.getHud().drawImage(wallpaper, 0, 0, null);
        Client.getHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

    }

}
