package sprites;

import client.Client;
import scripts.IsIntersection;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A bullet is been made with a weapon. Bullet is a line with its color and it has no imageManager. It has its v,
 * direction and position. If a bullet intersects an actor then the bullets lose its speed and hit the actor.
 *
 * Created by Aunmag on 03.10.2016.
 */

public class Bullet extends Sprite {

    public static List<Bullet> allBullets = new ArrayList<>(); // the all valid bullets must keep here

    // Characteristics:
    private double v; // current bullet velocity
    private final double vKeep; // how fast bullet lose (keep contrariwise) its v per one frame
    private static final Color color = new Color(255, 204, 51, 160);

    // State:
    private boolean hasCollided = false; // this is true if bullet has intersected an actor's body

    // Tail position (depends on its velocity):
    private double x2;
    private double y2;

    public Bullet(double x, double y, double degrees, double v, double vKeep) {

        // Set basic sprite data:
        super(x, y, degrees, true, "");

        // Set characteristics:
        this.v = v;
        this.vKeep = vKeep;

    }

    @Override public void tick() {

        if (!isValid) {
            Bullet.allBullets.remove(this);
            return;
        }

        v *= vKeep; // make bullet velocity slower

        // Test if bullet has stopped already:
        if (v <= 1 || hasCollided) {
            isValid = false;
            Bullet.allBullets.remove(this);
            return;
        }

        // Update bullet position according to its velocity:
        x += v * Math.cos(radians);
        y += v * Math.sin(radians);

        // Define bullet tall (trace) according to its head and velocity too:
        x2 = x - v * Math.cos(radians);
        y2 = y - v * Math.sin(radians);

        // Test if bullet intersected any actor's body:
        for (Actor actor: Actor.allActors) {
            if (IsIntersection.circleLine(x, y, x2, y2, actor.x, actor.y, actor.bodyRadius)) {
                actor.hit(v, radians); // hit actor with bullet velocity
                hasCollided = true; // commit collision
                v /= 4; // lose velocity (I think this must being improved)
            }
        }

    }

    @Override public void render() {

        // Test if bullet is visible:
        if (!IsVisible()) {
            return;
        }

        // Put sprite according to camera position:
        double gX = Client.getGX();
        double gY = Client.getGY();

        // Prepare bullet color:
        Client.getG().setColor(color);

        // Render imageManager:
        Client.getG().drawLine((int) (x - gX), (int) (y - gY), (int) (x2 - gX), (int) (y2 - gY));

    }

}
