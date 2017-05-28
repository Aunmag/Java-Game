package sprites;

import java.util.ArrayList;
import java.util.List;

/**
 * This is static level object like a wall, ground or tree.
 *
 * Created by Aunmag on 2016.09.28.
 */

public class Object extends Sprite {

    public static List<Object> allGround = new ArrayList<>();
    public static List<Object> allDecoration = new ArrayList<>();
    public static List<Object> allAir = new ArrayList<>();

    public Object(String name, boolean isUnique, float x, float y, float radians) {
        super(x, y, radians, isUnique, "objects/" + name + ".png");  // TODO: Simplify image path
    }

}
