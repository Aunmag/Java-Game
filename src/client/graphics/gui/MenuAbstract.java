package client.graphics.gui;

// Created by Aunmag on 20.11.2016.

import client.DataManager;
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
            wallpaper = wallpaper.getScaledInstance(DataManager.getDisplayWidth(), DataManager.getDisplayHeight(), BufferedImage.SCALE_SMOOTH);
        } catch (Exception e) {
            wallpaper = null;
            Log.log("ImageManager error", "Can't load \"" + wallpaperPath+ "\" image.", e.toString());
        }

    }

    void renderWallpaper() {

        if (wallpaper == null) {
            return;
        }

        DataManager.getHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.3));
        DataManager.getHud().drawImage(wallpaper, 0, 0, null);
        DataManager.getHud().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

    }

}
