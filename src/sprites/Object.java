package sprites;

import java.util.ArrayList;
import java.util.List;

/**
 * This is static level object like a wall, ground or tree.
 *
 * Created by Aunmag on 28.09.2016.
 */

public class Object extends Sprite {

    // Layers (the all valid objects must keep here):
    public static List<Object> allGround = new ArrayList<>(); // the lower objects like grass or sand
    public static List<Object> allDecoration = new ArrayList<>(); // additional intangible ground decorations as garbage
    public static List<Object> allAir = new ArrayList<>(); // the higher intangible objects like roofs or crown

    public Object(String name, boolean isUnique, float x, float y, float radians) {

        // Set basic object data:
        super(x, y, radians, isUnique, "objects/" + name + ".png");

    }

}
