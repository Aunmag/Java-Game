package aunmag.shooter.environment.decorations;

import aunmag.nightingale.structures.Texture;

public class GroundType {

    public final Texture texture;

    public GroundType(String name) {
        texture = Texture.getOrCreate("images/textures/" + name, Texture.Type.SPRITE);
    }

    public static final GroundType bluff = new GroundType("bluff");
    public static final GroundType bluffCorner = new GroundType("bluff_corner");

}
