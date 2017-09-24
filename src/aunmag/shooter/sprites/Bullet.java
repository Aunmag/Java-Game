package aunmag.shooter.sprites;

import aunmag.shooter.world.World;
import aunmag.nightingale.basics.BasePoint;
import aunmag.nightingale.basics.BaseSprite;
import aunmag.nightingale.collision.Collision;
import aunmag.nightingale.collision.CollisionLine;
import aunmag.nightingale.utilities.UtilsGraphics;

import java.awt.Color;

public class Bullet extends BaseSprite {

    private static final Color color = new Color(255, 204, 51, 160);

    private BasePoint positionTail;

    private float velocity;
    private final float velocityRecession; // TODO: Implement bullet weight
    private CollisionLine collision;

    Actor shooter;

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
        positionTail = new BasePoint(x, y);
        updatePositionTail();
        collision = new CollisionLine(
                getX(),
                getY(),
                positionTail.getX(),
                positionTail.getY()
        );
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
        addPosition(velocity * getCos(), velocity * getSin());
        updatePositionTail();
    }

    private void updatePositionTail() {
        positionTail.setPosition(
                getX() - velocity * getCos(),
                getY() - velocity * getSin()
        );
    }

    private void updateCollision() {
        collision.setPosition(getX(), getY(), positionTail.getX(), positionTail.getY());

        for (Actor actor: World.actors) {
            if (Collision.calculateIsCollision(actor.getCollision(), collision)) {
                actor.hit(velocity, getRadians(), shooter);
                velocity /= 60; // TODO: Improve
            }
        }
    }

    public void render() {
//        if (!Camera.calculateIsLineVisible(x, y, x2, y2)) {
//            return;
//        }

        UtilsGraphics.setDrawColor(color);
        UtilsGraphics.drawLine(
                getX(),
                getY(),
                positionTail.getX(),
                positionTail.getY(),
                true);

//        collision.render();
    }

}
