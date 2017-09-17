package game.utilities;

import java.awt.*;

public class UtilsGraphics {

    private static final AlphaComposite alphaDefault = AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, 1
    );

    public static void drawCircle(Graphics2D graphics, int x, int y, int diameter) {
        float radius = diameter / 2f;
        x -= radius;
        y -= radius;
        graphics.drawRoundRect(x, y, diameter, diameter, diameter, diameter);
    }

    public static void fillCircle(Graphics2D graphics, int x, int y, int diameter) {
        float radius = diameter / 2f;
        x -= radius;
        y -= radius;
        graphics.fillRoundRect(x, y, diameter, diameter, diameter, diameter);
    }

    public static void resetAlpha(Graphics2D graphics) {
        graphics.setComposite(alphaDefault);
    }

}
