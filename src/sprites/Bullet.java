package sprites;

import client.Client;
import scripts.IsIntersection;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * A bullet is been made with a weapon. Bullet is a line with its color and it has no image. It has its velocity (v),
 * direction and position. If a bullet intersects an actor then the bullets lose its speed and hit the actor.
 *
 * Created by Aunmag on 03.10.2016.
 */

public class Bullet extends Sprite {

    public static List<Bullet> all = new ArrayList<>(); // the all valid bullets must keep here

    // Characteristics:
//    private double velocity; // current bullet velocity
    private final double vRecession; // how fast bullet lose its velocity per a second
    private static final Color color = new Color(255, 204, 51, 160);

    // Tail position (depends on its velocity):
    private double x2;
    private double y2;

    public Bullet(double x, double y, double degrees, double velocity, double vRecession) {

        // Set basic sprite data:
        super(x, y, degrees, true, "");

        // Set characteristics:
        this.velocity = velocity;
        this.vRecession = vRecession;

    }

    @Override public void tick() {

        // Remove bullet if it isn't valid or has stopped already:
        if (!isValid || velocity <= 1) {
            Bullet.all.remove(this);
            return;
        }

        // Update velocity:
        velocity *= vRecession / Client.getFpsLimit(); // make bullet velocity slower in according to FPS

        // Update bullet position according to its velocity:
        x += velocity * Math.cos(radians);
        y += velocity * Math.sin(radians);

        // Define bullet tall (trace) according to its head and velocity too:
        x2 = x - velocity * Math.cos(radians);
        y2 = y - velocity * Math.sin(radians);

        // Test if bullet intersected any actor's body:
        for (Actor actor: Actor.allActors) {
            if (IsIntersection.circleLine(x, y, x2, y2, actor.x, actor.y, actor.bodyRadius)) {
                actor.hit(velocity, radians); // hit actor with bullet velocity
                velocity /= 60; // lose velocity (I think this must being improved)
            }
        }

    }

    @Override public void render() {

        // Test if bullet is visible:
        if (!IsVisible()) {
            return;
        }

        displayPositionPrepare();

        double displayX2 = displayX + velocity * Math.cos(radians);
        double displayY2 = displayY + velocity * Math.sin(radians);

        // Set bullet color:
        Client.getG().setColor(color);

        Client.getG().drawLine((int) displayX, (int) displayY, (int) displayX2, (int) displayY2);

    }

}
