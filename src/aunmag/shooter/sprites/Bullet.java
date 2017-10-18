package aunmag.shooter.sprites;

import aunmag.nightingale.Configs;
import aunmag.shooter.world.World;
import aunmag.nightingale.basics.BaseSprite;
import aunmag.nightingale.collision.Collision;
import aunmag.nightingale.collision.CollisionLine;
import aunmag.nightingale.utilities.UtilsGraphics;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public class Bullet extends BaseSprite {

    private static final Vector4f color = new Vector4f(1.0f, 0.8f, 0.2f, 0.6f);
    private static final int VELOCITY_MIN = 1;

    private float velocity;
    private float velocityRecessionFactor;
    private Vector2f positionTail;
    private CollisionLine collision;
    private Actor shooter;

    public Bullet(
            float x,
            float y,
            float radians,
            float velocity,
            float velocityRecessionFactor,
            Actor shooter
    ) {
        super(x, y, radians, null);
        this.velocity = velocity;
        this.velocityRecessionFactor = velocityRecessionFactor;
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
            if (Collision.calculateIsCollision(actor.getCollision(), collision)) {
                actor.hit(velocity, getRadians(), shooter);
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
        GL11.glColor4f(color.x, color.y, color.z, color.w);
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
