package managers;

import client.Display;

import java.awt.*;

/**
 * Created by Aunmag on 2017.06.12.
 */

public class Utils {

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

    public static int calculateScreenGridX(int n) {
        return Display.getWidth() / 12 * n;
    }

    public static int calculateScreenGridY(int n) {
        return Display.getHeight() / 12 * n;
    }

}
