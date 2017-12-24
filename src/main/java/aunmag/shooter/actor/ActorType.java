package aunmag.shooter.actor;

public class ActorType {

    private static final float strengthDefault = 37.5f * 200f;

    public final float strength;
    public final float velocity;
    public final float velocityFactorSprint;
    public final float damage;

    private ActorType(
            float strength,
            float velocity,
            float velocityFactorSprint,
            float damage
    ) {
        this.strength = strength;
        this.velocity = velocity;
        this.velocityFactorSprint = velocityFactorSprint;
        this.damage = damage;
    }

    /* Types */

    public static final ActorType human = new ActorType(
            strengthDefault,
            2.58f,
            2.76f,
            strengthDefault / 16f
    );

    public static final ActorType zombieEasy = new ActorType(
            0.4f * human.strength,
            0.4f * human.velocity,
            0.4f * human.velocityFactorSprint,
            human.strength / 8f
    );

    public static final ActorType zombieMedium = new ActorType(
            0.6f * human.strength,
            0.6f * human.velocity,
            0.6f * human.velocityFactorSprint,
            human.strength / 6f
    );

    public static final ActorType zombieHard = new ActorType(
            0.8f * human.strength,
            0.8f * human.velocity,
            0.8f * human.velocityFactorSprint,
            human.strength / 4f
    );

}
