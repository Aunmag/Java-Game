package sprites.components;

import client.Client;
import sprites.Sprite;

/**
 * Created by Aunmag on 2017.05.26.
 */

public class CollisionLine extends Collision {

    private float x2;
    private float y2;

    public CollisionLine(Sprite target) {
        super(target);
        x2 = target.getX();
        y2 = target.getY();
    }

    public void render() {
        if (!isVisible) {
            return;
        }

        int onScreenX1 = (int) (x - Client.getGX());
        int onScreenY1 = (int) (y - Client.getGY());
        int onScreenX2 = (int) (x2 - Client.getGX());
        int onScreenY2 = (int) (y2 - Client.getGY());

        Client.getG().setColor(renderColor);
        Client.getG().drawLine(onScreenX1, onScreenY1, onScreenX2, onScreenY2);
    }

    /* Getters */

    public float getX2() {
        return x2;
    }

    public float getY2() {
        return y2;
    }

    /* Setters */

    public void setPosition(float x1, float y1, float x2, float y2) {
        this.x = x1;
        this.y = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

}
