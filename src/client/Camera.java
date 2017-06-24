package client;

import client.input.Input;
import managers.MathManager;
import managers.Utils;
import utilities.basics.BasePoint;
import utilities.basics.BasePosition;

import java.awt.*;

/**
 * Created by Aunmag on 2017.06.03.
 */

public class Camera {

    private static BasePosition target;
    private static float x = 0;
    private static float y = 0;
    private static float radians = 0;
    private static float centerX;
    private static float centerY;
    private static float offset;
    private static float zoom;
    private static final float zoomLimitMin = 1;
    private static final float zoomLimitMax = 4;
    private static int distanceView;
    private static final int distanceViewBuffer = 192;

    static {
        setZoom(1);
        updatePosition();
    }

    public static void update() {
        updateZoom();
        updatePosition();
    }

    private static void updateZoom() {
        int mouseWheelRotation = Input.getMouseWheelRotation();

        if (mouseWheelRotation == 0) {
            return;
        }

        float zoomChange = zoom * mouseWheelRotation * 0.1f;
        setZoom(zoom - zoomChange);
    }

    private static void updatePosition() {
        if (target != null) {
            x = target.getX() - centerX;
            y = target.getY() - centerY;
            radians = target.getRadians();
        }

        if (offset != 0) {
            x += offset * Math.cos(radians);
            y += offset * Math.sin(radians);
        }
    }

    public static void render() {
//        if (Constants.isDebug) {
//            renderCameraCenter();
//            renderDistanceViewBoundaries();
//        }
    }

    public static void renderCameraCenter() {
        /* Used for debug */

        Display.getGraphics().setColor(new Color(0, 255, 0));
        Utils.drawCircle(Display.getGraphics(), (int) centerX, (int) centerY, 4);
    }

    public static void renderDistanceViewBoundaries() {
        /* Used for debug */

        int x = (int) centerX;
        int y = (int) centerY;

        // Render distance view boundaries
        Display.getGraphics().setColor(new Color(255, 0, 0));
        Utils.drawCircle(Display.getGraphics(), x, y, distanceView * 2);

        // Render distance view boundaries without buffer
        Display.getGraphics().setColor(new Color(255, 255, 0));
        Utils.drawCircle(Display.getGraphics(), x, y, distanceView * 2);
    }

    public static BasePoint calculateOnScreenPosition(BasePoint onWorldPosition) {
        return calculateOnScreenPosition(onWorldPosition.getX(), onWorldPosition.getY());
    }

    public static BasePoint calculateOnScreenPosition(float onWorldX, float onWorldY) {
        float onScreenX = onWorldX - x;
        float onScreenY = onWorldY - y;
        return new BasePoint(onScreenX, onScreenY);
    }

    public static boolean calculateIsPointVisible(BasePoint point) {
        return calculateIsPointVisible(point.getX(), point.getY());
    }

    public static boolean calculateIsPointVisible(float pointX, float pointY) {
        float distanceBetween = MathManager.calculateDistanceBetween(
                x + centerX,
                y + centerY,
                pointX,
                pointY
        );
        return distanceBetween < distanceView;
    }

    public static boolean calculateIsLineVisible(float x1, float y1, float x2, float y2) {
        boolean isPoint1Visible = calculateIsPointVisible(x1, y1);
        boolean isPoint2Visible = calculateIsPointVisible(x2, y2);
        return isPoint1Visible && isPoint2Visible;
    }

    /* Setters */

    public static void setTarget(BasePosition target) {
        Camera.target = target;
    }

    public static void setZoom(float zoom) {
        if (zoom == Camera.zoom) {
            return;
        } else if (zoom < zoomLimitMin) {
            zoom = zoomLimitMin;
        } else if (zoom > zoomLimitMax) {
            zoom = zoomLimitMax;
        }

        Camera.zoom = zoom;

        offset = (Display.getHalfHeight() - 30) / zoom;
        distanceView = (int) (Display.getHalfMaximal() / zoom) + distanceViewBuffer;

        centerX = Display.getHalfWidth() / zoom;
        centerY = Display.getHalfHeight() / zoom;
    }

    /* Getters */

    public static float getRadians() {
        return radians;
    }

    public static float getZoom() {
        return zoom;
    }

    public static float getCenterX() {
        return centerX;
    }

    public static float getCenterY() {
        return centerY;
    }

}
