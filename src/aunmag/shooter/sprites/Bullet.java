package aunmag.shooter.sprites;

import aunmag.shooter.world.World;
import aunmag.nightingale.basics.BaseSprite;
import aunmag.nightingale.collision.Collision;
import aunmag.nightingale.collision.CollisionLine;
import aunmag.nightingale.utilities.UtilsGraphics;
import org.joml.Vector2f;

import java.awt.Color;

public class Bullet extends BaseSprite {

    private static final Color color = new Color(255, 204, 51, 160);

    private float velocity;
    private final float velocityRecession; // TODO: Implement bullet weight
    private Vector2f positionTail;
    private CollisionLine collision;
    private Actor shooter;

    public Bullet(
            float x,
            float y,
            float radians,
            float velocity,
            float velocityRecession,
            Actor shooter
    ) {
        super(x, y, radians, null);
        this.velocity = velocity;
        this.velocityRecession = velocityRecession;
        this.shooter = shooter;
        positionTail = new Vector2f(x, y);
        collision = new CollisionLine(x, y, x, y);
    }

    public void update() {
        updateVelocity();
        updatePosition();
        updateCollision();
    }

    private void updateVelocity() {
        velocity *= velocityRecession / 75;

        if (velocity <= 1) {
            remove();
        }
    }

    private void updatePosition() {
        addPosition(
                velocity * (float) Math.cos(getRadians()),
                velocity * (float) Math.sin(getRadians())
        );
    }

    private void updateCollision() {
        for (Actor actor: World.actors) {
            if (Collision.calculateIsCollision(actor.getCollision(), collision)) {
                actor.hit(velocity, getRadians(), shooter);
                velocity /= 60; // TODO: Improve
            }
        }
    }

    public void render() {
        UtilsGraphics.setDrawColor(color);
        UtilsGraphics.drawLine(getX(), getY(), positionTail.x(), positionTail.y(), true);
    }

    /* Setters */

    public void setPosition(float x, float y) {
        positionTail.set(getX(), getY());
        collision.setPosition(x, y, getX(), getY());
        super.setPosition(x, y);
    }

}
