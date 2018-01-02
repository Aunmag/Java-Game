package aunmag.shooter.weapon;

import aunmag.nightingale.Application;
import aunmag.nightingale.utilities.UtilsMath;
import aunmag.shooter.actor.Actor;
import aunmag.nightingale.collision.Collision;
import aunmag.nightingale.collision.CollisionLine;
import aunmag.shooter.world.World;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public class Projectile extends CollisionLine {

    private static final float VELOCITY_MIN = 0.5f;
    private static final float VELOCITY_FACTOR = 1f / 5f;

    public final World world;
    public final CartridgeType type;
    private float velocity;
    private Actor shooter;

    public Projectile(
            World world,
            CartridgeType type,
            float x,
            float y,
            float radians,
            float velocity,
            Actor shooter
    ) {
        super(x, y);
        this.world = world;
        this.type = type;
        this.velocity = velocity;
        this.shooter = shooter;
        this.color = new Vector4f(type.color); // TODO: Optimize
        setRadians(radians);
    }

    public void update() {
        if (isStopped()) {
            remove();
            return;
        }

        updatePosition();
        updateCollision();
        updateVelocity();

        if (type.velocityRecessionFactor <= 0) {
            stop();
        }
    }

    private void updatePosition() {
        double velocity = this.velocity * VELOCITY_FACTOR * world.getTime().getDelta();

        pullUpTail();
        addPosition(
                (float) (velocity * Math.cos(getRadians())),
                (float) (velocity * Math.sin(getRadians()))
        );
    }

    private void updateCollision() {
        Actor farthestActor = null;
        float farthestActorDistance = 0;

        for (Actor actor: world.getActors()) {
            if (!actor.isAlive() || actor.isRemoved()) {
                continue;
            }

            if (Collision.calculateIsCollision(actor, this)) {
                float distance = UtilsMath.calculateDistanceBetween(this, actor);
                if (farthestActor == null || distance > farthestActorDistance) {
                    farthestActor = actor;
                    farthestActorDistance = distance;
                }
            }
        }

        if (farthestActor != null) {
            farthestActor.hit(velocity * type.weight, shooter);
            addPosition(
                    -farthestActorDistance * (float) Math.cos(getRadians()),
                    -farthestActorDistance * (float) Math.sin(getRadians())
            );
            stop();
        }
    }

    private void updateVelocity() {
        velocity -= velocity * (type.velocityRecessionFactor * world.getTime().getDelta());

        if (velocity <= VELOCITY_MIN) {
            stop();
        }
    }

    private void stop() {
        velocity = 0;
    }

    public void render() {
        GL11.glLineWidth(type.size * Application.getCamera().getScaleFull());
        super.render();
    }

    /* Getters */

    public boolean isStopped() {
        return velocity <= 0;
    }

}
