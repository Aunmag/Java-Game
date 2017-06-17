package gui.menus;

import client.Display;
import gui.components.GuiButton;
import gui.components.GuiText;
import managers.ImageManager;
import managers.Log;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Aunmag on 2016.11.20.
 */

public abstract class Menu {

    private Image wallpaper;
    private static AlphaComposite alphaOn = AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER,
            0.3f
    );
    private static final AlphaComposite alphaOff = AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER,
            1
    );
    protected Set<GuiText> guiTexts = new HashSet<>();
    protected Set<GuiButton> allButtons = new HashSet<>();

    Menu(String wallpaperPath) {

        try {
            wallpaper = ImageIO.read(ImageManager.class.getResource(wallpaperPath));
            wallpaper = wallpaper.getScaledInstance(Display.getWidth(), Display.getHeight(), BufferedImage.SCALE_SMOOTH);
        } catch (Exception e) {
            wallpaper = null;
            Log.log("ImageManager error", "Can't load \"" + wallpaperPath+ "\" image.", e.toString());
        }

    }

    public void activate() {
        MenuManager.activateMenu(this);
    }

    public void update() {
        for (GuiButton button: allButtons) {
            button.update();
        }
    }

    public void render() {
        renderWallpaper();

        for (GuiText label: guiTexts) {
            label.render();
        }

        for (GuiButton button: allButtons) {
            button.render();
        }
    }

    void renderWallpaper() {
        if (wallpaper == null) {
            return;
        }

        Display.getGraphicsHud().setComposite(alphaOn);
        Display.getGraphicsHud().drawImage(wallpaper, 0, 0, null);
        Display.getGraphicsHud().setComposite(alphaOff);
    }

    public abstract void deactivate();


}
