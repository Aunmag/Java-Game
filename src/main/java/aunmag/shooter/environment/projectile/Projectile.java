package aunmag.shooter.environment.projectile;

import aunmag.nightingale.Application;
import aunmag.nightingale.math.BodyLine;
import aunmag.nightingale.math.CollisionCL;
import aunmag.nightingale.utilities.Operative;
import aunmag.shooter.environment.actor.Actor;
import aunmag.shooter.environment.World;
import org.lwjgl.opengl.GL11;

public class Projectile extends Operative {

    private static final float VELOCITY_MIN = 0.5f;
    private static final float VELOCITY_FACTOR = 1f / 5f;

    public final BodyLine body;
    public final World world;
    public final ProjectileType type;
    private float velocity;
    private Actor shooter;

    public Projectile(
            World world,
            ProjectileType type,
            float x,
            float y,
            float radians,
            float velocity,
            Actor shooter
    ) {
        body = new BodyLine(x, y, x, y);
        body.radians = radians;
        body.color.set(type.color);
        this.world = world;
        this.type = type;
        this.velocity = velocity;
        this.shooter = shooter;
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

        body.pullUpTail();
        body.position.add(
                (float) (velocity * Math.cos(body.radians)),
                (float) (velocity * Math.sin(body.radians))
        );
    }

    private void updateCollision() {
        Actor farthestActor = null;
        float farthestActorDistance = 0;

        for (Actor actor: world.getActors().all) {
            if (new CollisionCL(actor.body, body).isTrue()) {
                float distance = body.position.distance(actor.body.position);
                if (farthestActor == null || distance > farthestActorDistance) {
                    farthestActor = actor;
                    farthestActorDistance = distance;
                }
            }
        }

        if (farthestActor != null) {
            farthestActor.hit(velocity * type.weight, shooter);
            body.position.sub(
                    farthestActorDistance * (float) Math.cos(body.radians),
                    farthestActorDistance * (float) Math.sin(body.radians)
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
        body.render();
    }

    /* Getters */

    public boolean isStopped() {
        return velocity <= 0;
    }

}
