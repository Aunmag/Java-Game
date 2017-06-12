package managers;

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

}
