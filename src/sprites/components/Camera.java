package sprites.components;

import client.Constants;
import client.DataManager;
import client.Display;
import managers.MathManager;
import managers.Utils;
import sprites.basics.BasePoint;
import sprites.basics.BasePosition;

import java.awt.*;

/**
 * Created by Aunmag on 2017.06.03.
 */

public class Camera extends BasePosition {

    private BasePosition target;
    private float zoom;
    private static final float zoomLimitMin = 1;
    private static final float zoomLimitMax = 4;
    private float centerX;
    private float centerY;
    private float offset;
    private int distanceView;
    private static final int distanceViewBuffer = 192;

    public Camera(BasePosition target) {
        super(target.getX(), target.getY(), target.getRadians());
        this.target = target;
        setZoom(1);
        updatePosition();
    }

    public void update() {
        updateZoom();
        updatePosition();
    }

    private void updateZoom() {
        int mouseWheelRotation = DataManager.getInput().getMouseWheelRotation();

        if (mouseWheelRotation == 0) {
            return;
        }

        float zoomChange = zoom * mouseWheelRotation * 0.1f;
        setZoom(zoom - zoomChange);
    }

    private void updatePosition() {
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

    public void render() {
        if (Constants.isDebug) {
            renderCameraCenter();
            renderDistanceViewBoundaries();
        }
    }

    public void renderCameraCenter() {
        /* Used for debug */

        Display.getGraphics().setColor(new Color(0, 255, 0));
        Utils.drawCircle(Display.getGraphics(), (int) centerX, (int) centerY, 4);
    }

    public void renderDistanceViewBoundaries() {
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

    public BasePoint calculateOnScreenPosition(BasePoint onWorldPosition) {
        return calculateOnScreenPosition(onWorldPosition.getX(), onWorldPosition.getY());
    }

    public BasePoint calculateOnScreenPosition(float onWorldX, float onWorldY) {
        float onScreenX = onWorldX - x;
        float onScreenY = onWorldY - y;
        return new BasePoint(onScreenX, onScreenY);
    }

    public boolean calculateIsPointVisible(BasePoint point) {
        return calculateIsPointVisible(point.getX(), point.getY());
    }

    public boolean calculateIsPointVisible(float pointX, float pointY) {
        float distanceBetween = MathManager.calculateDistanceBetween(
                x + centerX,
                y + centerY,
                pointX,
                pointY
        );
        return distanceBetween < distanceView;
    }

    public boolean calculateIsLineVisible(float x1, float y1, float x2, float y2) {
        boolean isPoint1Visible = calculateIsPointVisible(x1, y1);
        boolean isPoint2Visible = calculateIsPointVisible(x2, y2);
        return isPoint1Visible && isPoint2Visible;
    }

    /* Setters */

    public void setZoom(float zoom) {
        if (zoom == this.zoom) {
            return;
        } else if (zoom < zoomLimitMin) {
            zoom = zoomLimitMin;
        } else if (zoom > zoomLimitMax) {
            zoom = zoomLimitMax;
        }

        this.zoom = zoom;

        offset = (Display.getHalfHeight() - 30) / zoom;
        distanceView = (int) (Display.getHalfMaximal() / zoom) + distanceViewBuffer;

        centerX = Display.getHalfWidth() / zoom;
        centerY = Display.getHalfHeight() / zoom;
    }

    /* Getters */

    public float getZoom() {
        return zoom;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

}
