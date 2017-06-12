package sprites.components;

import client.Constants;
import client.DataManager;
import managers.Utils;
import sprites.Sprite;
import sprites.basics.BasePoint;

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
        if (!Constants.isDebug || !DataManager.getCamera().calculateIsPointVisible(this)) {
            return;
        }

        DataManager.getGraphics().setColor(color);

        BasePoint onScreenPosition = DataManager.getCamera().calculateOnScreenPosition(this);
        int onScreenX = (int) (onScreenPosition.getX() - radius);
        int onScreenY = (int) (onScreenPosition.getY() - radius);
        Utils.drawCircle(DataManager.getGraphics(), onScreenX, onScreenY, (int) diameter);
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
