package game.sprites;

import nightingale.basics.BaseSprite;
import nightingale.structures.Texture;

import java.util.ArrayList;
import java.util.List;

public class Object extends BaseSprite {

    public static List<Object> terrains = new ArrayList<>();
    public static List<Object> decorations = new ArrayList<>();
    public static List<Object> trees = new ArrayList<>();

    public Object(float x, float y, float radians, Texture texture) {
        super(x, y, radians, texture);
    }

    public void update() {}

}
