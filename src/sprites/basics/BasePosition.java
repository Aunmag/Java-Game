package sprites.basics;

/**
 * Created by Aunmag on 2017.05.27.
 */

public class BasePosition extends BasePoint {

    protected float radians = 0;

    /* Setters */

    public void setRadians(float radians) {
        this.radians = radians;
    }

    /* Getters */

    public float getRadians() {
        return radians;
    }

}
