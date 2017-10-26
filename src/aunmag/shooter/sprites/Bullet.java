package aunmag.shooter.sprites;

import aunmag.nightingale.Application;
import aunmag.nightingale.Configs;
import aunmag.shooter.world.World;
import aunmag.nightingale.basics.BaseSprite;
import aunmag.nightingale.collision.Collision;
import aunmag.nightingale.collision.CollisionLine;
import aunmag.nightingale.utilities.UtilsGraphics;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class Bullet extends BaseSprite {

    private static final float sizeScale = 2f;
    private static final Vector3f color = new Vector3f(1.0f, 0.8f, 0.2f);
    private static final float VELOCITY_MIN = 0.03125f;

    private float velocity;
    private float velocityRecessionFactor;
    private float size;
    private Vector2f positionTail;
    private CollisionLine collision;
    private Actor shooter;

    public Bullet(
            float x,
            float y,
            float radians,
            float velocity,
            float velocityRecessionFactor,
            float size,
            Actor shooter
    ) {
        super(x, y, radians, null);
        this.velocity = velocity;
        this.velocityRecessionFactor = velocityRecessionFactor;
        this.size = size;
        this.shooter = shooter;
        positionTail = new Vector2f(x, y);
        collision = new CollisionLine(x, y, x, y);
    }

    public void update() {
        updatePosition();
        updateCollision();
        updateVelocity();
    }

    private void updatePosition() {
        float velocity = this.velocity / Configs.getFpsLimit();
        addPosition(
                velocity * (float) Math.cos(getRadians()),
                velocity * (float) Math.sin(getRadians())
        );
    }

    private void updateCollision() {
        for (Actor actor: World.actors) {
            if (!actor.isAlive() || actor.isRemoved()) {
                continue;
            }

            if (Collision.calculateIsCollision(actor.getCollision(), collision)) {
                actor.hit(velocity * 0.16f, shooter); // TODO: Implement weight
                remove();
            }
        }
    }

    private void updateVelocity() {
        velocity -= velocity * (velocityRecessionFactor / Configs.getFpsLimit());

        if (velocity <= VELOCITY_MIN) {
            remove();
        }
    }

    public void render() {
        GL11.glLineWidth(size * sizeScale * Application.getCamera().getScaleFull() / Configs.getPixelsPerMeter());
        GL11.glColor3f(color.x, color.y, color.z);
        UtilsGraphics.drawLine(getX(), getY(), positionTail.x(), positionTail.y(), true);
    }

    public void remove() {
        velocity = 0;
        super.remove();
    }

    /* Setters */

    public void setPosition(float x, float y) {
        positionTail.set(getX(), getY());
        collision.setPosition(x, y, getX(), getY());
        super.setPosition(x, y);
    }

}
