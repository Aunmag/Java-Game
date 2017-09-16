package gui.menus;

import client.Constants;
import client.Display;
import gui.components.GuiButton;
import gui.components.GuiLabel;
import managers.ImageManager;
import nightingale.utilities.UtilsLog;
import utilities.UtilsGraphics;

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
    private static AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
    protected Set<GuiLabel> allLabels = new HashSet<>();
    protected Set<GuiButton> allButtons = new HashSet<>();

    Menu(String wallpaperPath) {
        try {
            wallpaper = ImageIO.read(ImageManager.class.getResource(wallpaperPath));
            wallpaper = wallpaper.getScaledInstance(
                    Display.getWidth(),
                    Display.getHeight(),
                    BufferedImage.SCALE_SMOOTH
            );
        } catch (Exception e) {
            wallpaper = null;
            String message = String.format("Can't load \"%s\" image.", wallpaperPath);
            UtilsLog.log("ImageManager error", message, e.toString());
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

        if (Constants.IS_DEBUG) {
            renderGrid();
        }

        for (GuiLabel label: allLabels) {
            label.render();
        }

        for (GuiButton button: allButtons) {
            button.render();
        }
    }

    protected void renderWallpaper() {
        if (wallpaper == null) {
            return;
        }

        Display.getGraphicsHud().setComposite(alpha);
        Display.getGraphicsHud().drawImage(wallpaper, 0, 0, null);
        UtilsGraphics.resetAlpha(Display.getGraphicsHud());
    }

    private void renderGrid() {
        Display.getGraphicsHud().setColor(Color.GRAY);
        int grid = 12;
        int stepX = Display.getWidth() / 12;
        int stepY = Display.getHeight() / 12;
        int textMargin = 8;

        for (int n = 0; n < grid; n++) {
            int x1 = stepX * n;
            int y1 = 0;
            int x2 = stepX * n;
            int y2 = Display.getHeight();
            Display.getGraphicsHud().drawLine(x1, y1, x2, y2);
            Display.getGraphicsHud().drawString("" + n, x1 + textMargin, textMargin * 2);

            x1 = 0;
            y1 = stepY * n;
            x2 = Display.getWidth();
            y2 = stepY * n;
            Display.getGraphicsHud().drawLine(x1, y1, x2, y2);
            Display.getGraphicsHud().drawString("" + n, textMargin, y1 + textMargin * 2);
        }
    }

    public abstract void deactivate();

}
