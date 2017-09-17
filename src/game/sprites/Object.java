package game.sprites;

import nightingale.basics.BaseSprite;
import nightingale.structures.Texture;

import java.util.ArrayList;
import java.util.List;

public class Object extends BaseSprite {

    public static List<Object> allGround = new ArrayList<>();
    public static List<Object> allDecoration = new ArrayList<>();
    public static List<Object> allAir = new ArrayList<>();

    public Object(float x, float y, float radians, Texture texture) {
        super(x, y, radians, texture);
    }

    public void update() {}

}
