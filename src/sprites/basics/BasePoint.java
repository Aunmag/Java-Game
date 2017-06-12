package sprites.basics;

/**
 * Created by Aunmag on 2017.05.27.
 */

public class BasePoint {

    protected float x = 0;
    protected float y = 0;

    public BasePoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /* Setters */

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /* Getters */

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}
