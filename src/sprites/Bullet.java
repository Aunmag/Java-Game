package sprites;

import client.DataManager;
import client.Constants;
import client.Display;
import client.TimeManager;
import sprites.basics.BasePoint;
import sprites.components.Collision;
import sprites.components.CollisionLine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * A bullet is been made with a weapon. Bullet is a line with its color and it has no image. It has
 * its velocity, direction and position. If a bullet collides an actor then the bullets lose its
 * speed and hit the actor.
 *
 * Created by Aunmag on 2016.10.03.
 */

public class Bullet extends Sprite {

    public static List<Bullet> all = new ArrayList<>();
    public static List<Bullet> invalids = new ArrayList<>();

    private static final Color color = new Color(255, 204, 51, 160);

    private float x2;
    private float y2;

    private float velocity;
    private final float velocityRecession; // TODO: Implement bullet weight
    private CollisionLine collision = new CollisionLine(this);

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
        updatePositionTail();
    }

    public void update() {
        updateVelocity();
        updatePosition();
        updateCollision();
    }

    private void updateVelocity() {
        velocity *= velocityRecession / TimeManager.FPS_LIMIT;

        if (velocity <= 1) {
            delete();
        }
    }

    private void updatePosition() {
        x += velocity * Math.cos(radians);
        y += velocity * Math.sin(radians);
        updatePositionTail();
    }

    private void updatePositionTail() {
        x2 = x - velocity * (float) Math.cos(radians);
        y2 = y - velocity * (float) Math.sin(radians);
    }

    private void updateCollision() {
        collision.setPosition(x, y, x2, y2);

        for (Actor actor: Actor.all) {
            if (Collision.calculateIsCollision(actor.getCollision(), collision)) {
                actor.hit(velocity, radians, shooter);
                velocity /= 60; // TODO: Improve
            }
        }
    }

    public void render() {
        if (!DataManager.getCamera().calculateIsLineVisible(x, y, x2, y2)) {
            return;
        }

        BasePoint onScreenPosition = DataManager.getCamera().calculateOnScreenPosition(x, y);
        int onScreenX1 = (int) onScreenPosition.getX();
        int onScreenY1 = (int) onScreenPosition.getY();

        onScreenPosition = DataManager.getCamera().calculateOnScreenPosition(x2, y2);
        int onScreenX2 = (int) onScreenPosition.getX();
        int onScreenY2 = (int) onScreenPosition.getY();

        Display.getGraphics().setColor(color);
        Display.getGraphics().drawLine(onScreenX1, onScreenY1, onScreenX2, onScreenY2);

        collision.render();
    }

    public void delete() {
        isValid = false;
        invalids.add(this);
    }

}
