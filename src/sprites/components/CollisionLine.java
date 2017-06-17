package sprites.components;

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

    public void render() {}

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
