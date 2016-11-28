package root.sprites;

// Created by Aunmag on 03.10.2016.

import root.scripts.IsIntersection;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bullet extends Sprite {

    public static List<Bullet> allBullets = new ArrayList<>();

    private double velocity;
    private final double velocityKeep;
    private static final Color color = new Color(255, 204, 51, 160);

    private boolean hasCollided = false;

    // Line position:
//    double x1;
//    double y1;
    public double x2;
    public double y2;

    public Bullet(double x, double y, double degrees, double velocity, double velocityKeep) {

        super(x, y, degrees, true, "");

        this.velocity = velocity;
        this.velocityKeep = velocityKeep;

    }

    @Override public void tick() {

        velocity *= velocityKeep;

        if (velocity <= 1 || hasCollided) {
            isValid = false;
            return;
        }

        x += velocity*Math.cos(radians);
        y += velocity*Math.sin(radians);
        x2 = x - velocity * Math.cos(radians);
        y2 = y - velocity * Math.sin(radians);

        for (Actor actor: Actor.allActors) {
            if (IsIntersection.circleLine(x, y, x2, y2, actor.x, actor.y, actor.bodyRadius)) {
                actor.hit(velocity, radians);
                hasCollided = true;
                velocity /= 4;
            }
        }

//        for (Zombie zombie: Zombie.allZombies) {
//            if (IsIntersection.circleLine(x, y, x2, y2, zombie.x, zombie.y, zombie.bodyRadius)) {
//                zombie.hit(velocity, radians);
//                hasCollided = true;
//                velocity /= 4;
//            }
//        }

    }

    @Override public void render(Graphics g, double xOff, double yOff) {

        g.setColor(color);
        g.drawLine((int) (x - xOff), (int) (y - yOff), (int) (x2 - xOff), (int) (y2 - yOff));

    }

}
