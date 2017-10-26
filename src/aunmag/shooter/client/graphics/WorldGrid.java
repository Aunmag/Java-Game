package aunmag.shooter.client.graphics;

import aunmag.nightingale.Application;
import aunmag.nightingale.Camera;
import aunmag.nightingale.Configs;
import aunmag.nightingale.utilities.UtilsGraphics;

public class WorldGrid {

    private int step = 2;

    public void render() {
        Camera camera = Application.getCamera();

        int size = calculateAliquotValue(
                camera.getDistanceView() * Configs.getPixelsPerMeter() / camera.getScaleFull() * 2f,
                step * 2
        );
        int start = size / -2;
        int end = start + size;

        int initialX = calculateAliquotValue(camera.getX(), step);
        int initialY = calculateAliquotValue(camera.getY(), step);
        int minX = initialX - start;
        int maxX = initialX + start;
        int minY = initialY - start;
        int maxY = initialY + start;

        for (int i = start; i < end; i += step) {
            int x = initialX + i;
            UtilsGraphics.drawLine(x, minY, x, maxY, true);

            int y = initialY - i;
            UtilsGraphics.drawLine(minX, y, maxX, y, true);
        }
    }

    private int calculateAliquotValue(float value, int aliquot) {
        return (int) (value - value % aliquot);
    }

}
