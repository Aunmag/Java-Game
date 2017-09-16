package sprites;

import nightingale.basics.BaseSprite;
import nightingale.structures.Texture;

import java.util.ArrayList;
import java.util.List;

/**
 * This is static level object like a wall, ground or tree.
 *
 * Created by Aunmag on 2016.09.28.
 */

public class Object extends BaseSprite {

    public static List<Object> allGround = new ArrayList<>();
    public static List<Object> allDecoration = new ArrayList<>();
    public static List<Object> allAir = new ArrayList<>();

    public Object(float x, float y, float radians, Texture texture) {
        super(x, y, radians, texture);
    }

    public void update() {}

}
