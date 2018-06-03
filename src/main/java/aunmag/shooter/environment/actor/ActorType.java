package aunmag.shooter.environment.actor;

import aunmag.nightingale.structures.Texture;

public class ActorType {

    private static final float strengthDefault = 37.5f * 200f;

    public final float weight;
    public final float strength;
    public final float velocity;
    public final float velocityFactorSprint;
    public final float damage;
    public final Texture texture;

    private ActorType(
            String name,
            float weight,
            float strength,
            float velocity,
            float velocityFactorSprint,
            float damage
    ) {
        this.weight = weight;
        this.strength = strength;
        this.velocity = velocity;
        this.velocityFactorSprint = velocityFactorSprint;
        this.damage = damage;

        texture = Texture.getOrCreate("actors/" + name + "/image", Texture.Type.SPRITE);
    }

    /* Types */

    public static final ActorType human = new ActorType(
            "human",
            80_000,
            strengthDefault,
            2.58f,
            2.76f,
            strengthDefault / 16f
    );

    public static final ActorType zombieEasy = new ActorType(
            "zombie",
            human.weight,
            0.4f * human.strength,
            0.4f * human.velocity,
            0.4f * human.velocityFactorSprint,
            human.strength / 8f
    );

    public static final ActorType zombieMedium = new ActorType(
            "zombie",
            human.weight,
            0.6f * human.strength,
            0.6f * human.velocity,
            0.6f * human.velocityFactorSprint,
            human.strength / 6f
    );

    public static final ActorType zombieHard = new ActorType(
            "zombie",
            human.weight,
            0.8f * human.strength,
            0.8f * human.velocity,
            0.8f * human.velocityFactorSprint,
            human.strength / 4f
    );

}
