package aunmag.shooter.factories;

import aunmag.nightingale.structures.Texture;
import aunmag.shooter.sprites.Actor;

public class FactoryActor {

    public static Actor human() {
        return new Actor(
                1.38f,
                0.6f,
                0.8f,
                2.76f,
                Texture.getOrCreate("images/actors/human"),
                "human"
        );
    }

    public static Actor zombie() {
        return new Actor(
                0.63f,
                0.6f,
                0.8f,
                1.63f,
                Texture.getOrCreate("images/actors/zombie"),
                "zombie"
        );
    }

}
