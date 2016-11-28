package root.sprites;

// Created by Aunmag on 23.10.2016.

import root.scripts.Inertia;
import root.scripts.IsIntersection;
import java.awt.*;
import java.util.*;

public class Actor extends Sprite {

    public static java.util.List<Actor> allActors = new ArrayList<>();

    public boolean isAlive = true;
    public boolean hasWeapon = false;
    double health = 1;

    // Melee:
    private static boolean isMeleeRadiusVisible = false;
    private static boolean isMeleeAttacking = false;
    public static int meleeRadius = 14;
    public static double meleeDistance = meleeRadius * 1.5;
    private static int meleeIntensity = 10;
    private static final long meleeTimePace = 400000000;
    private long meleeTimeLast = 0;
    private static final Color meleeColor = new Color(255, 0, 0, 128);
    public double meleeX;
    public double meleeY;

    // Movement velocity:
    public double velocityForward;
    public double velocityAside;
    public double velocityBack;
    public double runningAcceleration;

//    Inertia inertiaRadians = new Inertia(0.4);
    double currentMovementRadians = 0;
    Inertia inertiaVelocity = new Inertia(0.2);
    double currentMovementVelocity = 0;

    public String type = "unknown";

    // Doing flags:
    public boolean isMovingForward = false;
    public boolean isMovingBack = false;
    public boolean isMovingLeft = false;
    public boolean isMovingRight = false;
    public boolean isRunning = false;
    public boolean isAttacking = false;

    public Actor(double x, double y, double degrees, String image) {

        super(x, y, degrees, true, image);

        isBodyRadiusVisible = false;

//        inertiaRadians.valueRound = Math.PI * 2;

    }

    public void hit(double intensity, double direction) {

        health -= intensity / 100;

        if (health < 0) {
            health = 0;
        }

        double impulse = intensity / 10;

        x += impulse * Math.cos(direction);
        y += impulse * Math.sin(direction);

    }

    public void meleeAttack() {

        isMeleeAttacking = true;

//        meleeX = x + meleeDistance * Math.cos(radians);
//        meleeY = y + meleeDistance * Math.sin(radians);

        for (Actor i: Actor.allActors) {
            if (type.equals("zombie") && i.type.equals(type)) {
                continue;
            }
            if (IsIntersection.circleCircle(meleeX, meleeY, meleeRadius, i.x, i.y, i.bodyRadius)) {
                i.hit(meleeIntensity * health, radians);
            }
        }


    }

    // Movement methods:

    private void move(double movementRadians, double movementVelocity) {

        if (isRunning) {
            movementVelocity *= runningAcceleration;
        }

//        currentMovementRadians = inertiaRadians.update(1, movementRadians);
        currentMovementRadians = movementRadians;

        currentMovementVelocity = inertiaVelocity.update(1, movementVelocity * health);

        x += currentMovementVelocity * Math.cos(currentMovementRadians);
        y += currentMovementVelocity * Math.sin(currentMovementRadians);

    }

    @Override public void tick() {

        if (!isAlive) {
            return;
        }

        if (health <= 0) {
            isAlive = false;
            if (type.equals("zombie")) {
                isValid = false;
            }
        }

        if (isMovingForward) move(radians, velocityForward);
        if (isMovingBack) move(radians - Math.PI, velocityBack);
        if (isMovingLeft) move(radians - Math.PI / 2, velocityAside);
        if (isMovingRight) move(radians + Math.PI / 2, velocityAside);

        if (!isMovingForward && !isMovingBack && !isMovingLeft && !isMovingRight) {
            currentMovementVelocity = inertiaVelocity.update(1, 0);
            x += currentMovementVelocity * Math.cos(currentMovementRadians);
            y += currentMovementVelocity * Math.sin(currentMovementRadians);
        }

        meleeX = x + meleeDistance * Math.cos(radians);
        meleeY = y + meleeDistance * Math.sin(radians);

        if (isAttacking && !hasWeapon) {
            long meleeTimeCurrent = System.nanoTime();
            if (meleeTimeCurrent - meleeTimeLast >= meleeTimePace) {
                meleeAttack();
                meleeTimeLast = meleeTimeCurrent;
            }
        }

    }

    @Override public void render(Graphics g, double xOffset, double yOffset) {

        if (!isAlive || image == null) {
            return;
        }

        int displayX = (int) (x - image.rotatedOffsetX - xOffset);
        int displayY = (int) (y - image.rotatedOffsetY - yOffset);
        g.drawImage(image.get(radians), displayX, displayY, null);

        if (isBodyRadiusVisible) {
            g.setColor(bodyColor);
            int roundX = (int) (x - xOffset - bodyRadius);
            int roundY = (int) (y - yOffset - bodyRadius);
            int roundR = bodyRadius * 2;
            g.fillRoundRect(roundX, roundY, roundR, roundR, roundR, roundR);
        }

        if (isAttacking && isMeleeRadiusVisible && !hasWeapon && isMeleeAttacking) {
            g.setColor(meleeColor);
            int roundX = (int) (meleeX - xOffset - meleeRadius);
            int roundY = (int) (meleeY - yOffset - meleeRadius);
            int roundR = meleeRadius * 2;
            g.fillRoundRect(roundX, roundY, roundR, roundR, roundR, roundR);
            isMeleeAttacking = false;
        }

    }

    // Getters:

    public double getHealth() {

        return health;

    }

}
