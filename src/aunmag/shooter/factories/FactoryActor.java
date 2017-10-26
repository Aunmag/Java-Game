package aunmag.shooter.factories;

import aunmag.nightingale.structures.Texture;
import aunmag.shooter.sprites.Actor;

public class FactoryActor {

    public static Actor human() {
        return new Actor(
                0.043f,
                0.6f,
                0.8f,
                2.76f,
                Texture.getOrCreateAsSprite("images/actors/human"),
                "human"
        );
    }

    public static Actor zombie() {
        return new Actor(
                0.02f,
                0.6f,
                0.8f,
                1.63f,
                Texture.getOrCreateAsSprite("images/actors/zombie"),
                "zombie"
        );
    }

}
