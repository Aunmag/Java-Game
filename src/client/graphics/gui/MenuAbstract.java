package client.graphics.gui;

// Created by Aunmag on 20.11.2016.

import client.DataManager;
import client.Display;
import managers.ImageManager;
import managers.Log;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

abstract class MenuAbstract {

    private Image wallpaper;

    MenuAbstract(String wallpaperPath) {

        try {
            wallpaper = ImageIO.read(ImageManager.class.getResource(wallpaperPath));
            wallpaper = wallpaper.getScaledInstance(Display.getWidth(), Display.getHeight(), BufferedImage.SCALE_SMOOTH);
        } catch (Exception e) {
            wallpaper = null;
            Log.log("ImageManager error", "Can't load \"" + wallpaperPath+ "\" image.", e.toString());
        }

    }

    void renderWallpaper() {

        if (wallpaper == null) {
            return;
        }

        Display.getGraphicsHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.3));
        Display.getGraphicsHud().drawImage(wallpaper, 0, 0, null);
        Display.getGraphicsHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

    }

}
