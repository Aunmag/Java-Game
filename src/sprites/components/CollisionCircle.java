package sprites.components;

import client.Camera;
import client.Constants;
import client.Display;
import utilities.UtilsGraphics;
import sprites.Sprite;
import nightingale.basics.BasePoint;

import java.awt.*;

/**
 * Created by Aunmag on 2017.05.26.
 */

public class CollisionCircle extends Collision {

    protected float radius;
    protected float diameter;
    protected float lastDistanceBetween;

    public CollisionCircle(Sprite target, float radius) {
        super(target);
        this.radius = radius;
        this.diameter = radius * 2;
    }

    public void render() {
        render(renderColor);
    }

    public void render(Color color) {
        if (!Constants.IS_DEBUG || !Camera.calculateIsPointVisible(this)) {
            return;
        }

        Display.getGraphics().setColor(color);

        BasePoint onScreenPosition = Camera.calculateOnScreenPosition(this);
        int onScreenX = (int) onScreenPosition.getX();
        int onScreenY = (int) onScreenPosition.getY();
        UtilsGraphics.fillCircle(Display.getGraphics(), onScreenX, onScreenY, (int) diameter);
    }

    /* Setters */

    protected void setLastDistanceBetween(float lastDistanceBetween) {
        this.lastDistanceBetween = lastDistanceBetween;
    }

    /* Getters */

    public float getRadius() {
        return radius;
    }

    public float getLastDistanceBetween() {
        return lastDistanceBetween;
    }

}
