package basics;

/**
 * Created by Aunmag on 2017.06.17.
 */

public class BaseRectangle extends BasePoint {

    protected float width;
    protected float height;

    public BaseRectangle(float x, float y, float width, float height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

}
