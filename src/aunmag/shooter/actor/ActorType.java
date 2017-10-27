package aunmag.shooter.actor;

import aunmag.nightingale.structures.Texture;

public class ActorType {

    public final Texture texture;
    public final float strength;
    public final float velocity;
    public final float velocityFactorSprint;
    public final float damage;

    private ActorType(
            Texture texture,
            float strength,
            float velocity,
            float velocityFactorSprint,
            float damage
    ) {
        this.texture = texture;
        this.strength = strength;
        this.velocity = velocity;
        this.velocityFactorSprint = velocityFactorSprint;
        this.damage = damage;
    }

    /* Types */

    public static final ActorType human = new ActorType(
            Texture.getOrCreateAsSprite("images/actors/human"),
            37.5f,
            0.043f,
            2.76f,
            37.5f / 16f
    );

    public static final ActorType zombieEasy = new ActorType(
            Texture.getOrCreateAsSprite("images/actors/zombie"),
            0.4f * human.strength,
            0.4f * human.velocity,
            0.4f * human.velocityFactorSprint,
            human.strength / 8f
    );

    public static final ActorType zombieMedium = new ActorType(
            Texture.getOrCreateAsSprite("images/actors/zombie"),
            0.6f * human.strength,
            0.6f * human.velocity,
            0.6f * human.velocityFactorSprint,
            human.strength / 6f
    );

    public static final ActorType zombieHard = new ActorType(
            Texture.getOrCreateAsSprite("images/actors/zombie"),
            0.8f * human.strength,
            0.8f * human.velocity,
            0.8f * human.velocityFactorSprint,
            human.strength / 4f
    );

}
