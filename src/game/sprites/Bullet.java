package game.sprites;

import nightingale.basics.BasePoint;
import nightingale.basics.BaseSprite;
import nightingale.collision.Collision;
import nightingale.collision.CollisionLine;
import nightingale.utilities.UtilsGraphics;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Bullet extends BaseSprite {

    public static List<Bullet> all = new ArrayList<>();
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
        addPosition(
                velocity * (float) Math.cos(getRadians()),
                velocity * (float) Math.sin(getRadians())
        );
        updatePositionTail();
    }

    private void updatePositionTail() {
        positionTail.setPosition(
                getX() - velocity * (float) Math.cos(getRadians()),
                getY() - velocity * (float) Math.sin(getRadians())
        );
    }

    private void updateCollision() {
        collision.setPosition(getX(), getY(), positionTail.getX(), positionTail.getY());

        for (Actor actor: Actor.all) {
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
        UtilsGraphics.drawLine(this, positionTail, true);

//        collision.render();
    }

}
