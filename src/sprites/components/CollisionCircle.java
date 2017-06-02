package sprites.components;

import client.Client;
import sprites.Sprite;

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
        if (!isVisible) {
            return;
        }

        Client.getG().setColor(color);

        int onScreenX = Math.round(x - Client.getGX() - radius);
        int onScreenY = Math.round(y - Client.getGY() - radius);
        int onScreenDiameter = Math.round(diameter);

        Client.getG().fillRoundRect(
                onScreenX,
                onScreenY,
                onScreenDiameter,
                onScreenDiameter,
                onScreenDiameter,
                onScreenDiameter
        );
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
