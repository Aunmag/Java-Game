package sprites;

import client.Client;
import client.Constants;
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
    private static final Color color = new Color(255, 204, 51, 160);

    private float x2;
    private float y2;

    private float velocity;
    private final float velocityRecession; // TODO: Implement bullet weight
    private CollisionLine collision = new CollisionLine(this);

    public Bullet(float x, float y, float radians, float velocity, float velocityRecession) {
        super(x, y, radians, true, null);
        this.velocity = velocity;
        this.velocityRecession = velocityRecession;
        updatePositionTail();
    }

    public void update() {
        updateIsValid();

        if (!isValid) {
            Bullet.all.remove(this);
            return;
        }

        updatePosition();
        updateCollision();
    }

    private void updateIsValid() {
        if (velocity <= 1) {
            isValid = false;
        }
    }

    private void updatePosition() {
        velocity *= velocityRecession / Constants.FPS_LIMIT;

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
                actor.hit(velocity, radians);
                velocity /= 60; // TODO: Improve
            }
        }
    }

    public void render() {
        if (!calculateIsVisible()) {
            return;
        }

        int onScreenX1 = (int) (x - Client.getGX());
        int onScreenY1 = (int) (y - Client.getGY());
        int onScreenX2 = (int) (x2 - Client.getGX());
        int onScreenY2 = (int) (y2 - Client.getGY());

        Client.getG().setColor(color);
        Client.getG().drawLine(onScreenX1, onScreenY1, onScreenX2, onScreenY2);

        collision.render();
    }

}
