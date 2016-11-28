package sprites;

import java.util.ArrayList;
import java.util.List;

/**
 * This is fixed level object like a wall, ground or tree.
 *
 * Created by Aunmag on 28.09.2016.
 */

public class Object extends Sprite {

    public static List<Object> allGroundObjects = new ArrayList<>(); // the all valid objects must keep here
    public static List<Object> allDecorationObjects = new ArrayList<>(); // the all valid objects must keep here
    public static List<Object> allAirObjects = new ArrayList<>(); // the all valid objects must keep here

    public Object(String name, boolean isUnique, double x, double y, double degrees) {

        // Set basic object data:
        super(x, y, degrees, isUnique, "objects/" + name + ".png");

    }

}
