package aunmag.shooter.weapon;

import aunmag.nightingale.Application;
import aunmag.nightingale.Configs;
import aunmag.nightingale.utilities.UtilsMath;
import aunmag.shooter.actor.Actor;
import aunmag.shooter.world.World;
import aunmag.nightingale.basics.BaseSprite;
import aunmag.nightingale.collision.Collision;
import aunmag.nightingale.collision.CollisionLine;
import aunmag.nightingale.utilities.UtilsGraphics;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

public class Projectile extends BaseSprite {

    private static final float VELOCITY_MIN = 0.5f;
    private static final float VELOCITY_FACTOR = 1f / 5f;

    public final ProjectileType type;
    private float velocity;
    private Vector2f positionTail;
    private CollisionLine collision;
    private Actor shooter;
    private boolean isStopped = false;

    public Projectile(
            ProjectileType type,
            float x,
            float y,
            float radians,
            float velocity,
            Actor shooter
    ) {
        super(x, y, radians, null);
        this.type = type;
        this.velocity = velocity;
        this.shooter = shooter;
        positionTail = new Vector2f(x, y);
        collision = new CollisionLine(x, y, x, y);
    }

    public void update() {
        if (isStopped) {
            remove();
            return;
        }

        updatePosition();
        updateCollision();
        updateVelocity();

        if (type.velocityRecessionFactor <= 0) {
            isStopped = true;
        }
    }

    private void updatePosition() {
        float velocity = this.velocity * VELOCITY_FACTOR / Configs.getFpsLimit();

        positionTail.set(getX(), getY());

        addPosition(
                velocity * (float) Math.cos(getRadians()),
                velocity * (float) Math.sin(getRadians())
        );

        collision.setPosition(getX(), getY(), positionTail.x(), positionTail.y());
    }

    private void updateCollision() {
        for (Actor actor: World.actors) {
            if (!actor.isAlive() || actor.isRemoved()) {
                continue;
            }

            if (Collision.calculateIsCollision(actor.getCollision(), collision)) {
                actor.hit(velocity * type.weight, shooter);

                float distanceOverhead = UtilsMath.calculateDistanceBetween(
                        actor.getX(),
                        actor.getY(),
                        getX(),
                        getY()
                );

                addPosition(
                        -distanceOverhead * (float) Math.cos(getRadians()),
                        -distanceOverhead * (float) Math.sin(getRadians())
                );

                isStopped = true;
                break;
            }
        }
    }

    private void updateVelocity() {
        velocity -= velocity * (type.velocityRecessionFactor / Configs.getFpsLimit());

        if (velocity <= VELOCITY_MIN) {
            remove();
        }
    }

    public void render() {
        GL11.glLineWidth(type.size * Application.getCamera().getScaleFull());
        GL11.glColor3f(type.color.x(), type.color.y(), type.color.z());
        UtilsGraphics.drawLine(getX(), getY(), positionTail.x(), positionTail.y(), true);
    }

}
