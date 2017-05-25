package sprites;

import client.Client;
import managers.Log;
import scripts.FRandom;
import scripts.Inertia;
import scripts.IsIntersection;
import managers.SoundManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * MAKE DESCRIPTION!
 *
 * Created by Aunmag on 23.10.2016.
 */

public class Actor extends Sprite {

    public static java.util.List<Actor> allActors = new ArrayList<>();

    public boolean isAlive = true;
    public boolean hasWeapon = false;
    float health = 1;

    // Melee:
    private static boolean isMeleeRadiusVisible = false;
    public static int meleeRadius = 14;
    public static float meleeDistance = meleeRadius * 1.5f;
    private static int meleeIntensity = 10;
    private static final long meleeTimePace = 400;
    private long meleeTimeLast = 0;
    private static final Color meleeColor = new Color(255, 0, 0, 128);
    public float meleeX;
    public float meleeY;

    // Movement velocity:
    public float velocityForward;
    public float velocityAside;
    public float velocityBack;
    public float runningAcceleration;

//    Inertia inertiaRadians = new Inertia(0.4);
    float currentMovementRadians = 0;
    Inertia inertiaVelocity = new Inertia(0.2f);
//    float currentMovementVelocity = 0;

    public String type;

    // Doing flags:
    public boolean isMovingForward = false;
    public boolean isMovingBack = false;
    public boolean isMovingLeft = false;
    public boolean isMovingRight = false;
    public boolean isRunning = false;
    public boolean isAttacking = false;

    public static float vZombieForward = 0.63f;

    private static SoundManager[] sounds = new SoundManager[6];

    public Actor(float x, float y, float degrees, String imagePath) {

        super(x, y, degrees, true, imagePath);

        String[] imagePathData = imagePath.split("/");
        type = imagePathData[1];

        switch (type) {
            case "human.png":
                type = "human";
                velocityForward = 1.38f;
                velocityAside = velocityForward * 0.6f;
                velocityBack = velocityForward * 0.8f;
                runningAcceleration = 2.76f; // 3.02
                hasWeapon = true;
                break;
            case "zombie.png":
                type = "zombie";
                velocityForward = vZombieForward;
                velocityAside = velocityForward * 0.6f;
                velocityBack = velocityForward * 0.8f;
                runningAcceleration = 1.63f;
                hasWeapon = false;
                break;
            default:
                type = "unknown";
                velocityForward = 1;
                velocityAside = velocityForward;
                velocityBack = velocityForward;
                runningAcceleration = 2;
                hasWeapon = false;
                Log.log("Spawn", "Unknown actor spawned.", null);
        }

        hasBody = true;
        bodyRadius = 7.2f;

        meleeUpdate();

    }

    public void hit(float intensity, float direction) {

        health -= intensity / 100;

        if (health < 0) {
            health = 0;
        }

        float impulse = intensity / 10;

        x += impulse * Math.cos(direction);
        y += impulse * Math.sin(direction);


        if (type.equals("human")) {
            sounds[FRandom.random.nextInt(6)].play();
        }

    }

    public void meleeAttack() {

        for (Actor i: Actor.allActors) {
            if (type.equals("zombie") && i.type.equals(type)) {
                continue;
            }
            if (IsIntersection.circleCircle(meleeX, meleeY, meleeRadius, i.x, i.y, i.bodyRadius)) {
                i.hit(meleeIntensity * health, radians);
            }
        }

        if (isMeleeRadiusVisible) {
            Client.getG().setColor(meleeColor);
            int roundX = (int) (meleeX - Client.getGX() - meleeRadius);
            int roundY = (int) (meleeY - Client.getGY() - meleeRadius);
            int roundR = meleeRadius * 2;
            Client.getG().fillRoundRect(roundX, roundY, roundR, roundR, roundR, roundR);
        }

    }

    private void meleeUpdate() {

        meleeX = (float) (x + meleeDistance * Math.cos(radians));
        meleeY = (float) (y + meleeDistance * Math.sin(radians));

    }

    private void detectCollision() {

        for (int i = Actor.allActors.size() - 1; i >= 0; i--) {
            Actor a = Actor.allActors.get(i);
            if (a.isAlive && !a.equals(this)) {
                if (IsIntersection.circleCircle(x, y, bodyRadius, a.x, a.y, a.bodyRadius)) {

                    float distanceMin = bodyRadius + a.bodyRadius;
                    float distanceCurrent = (float) (Math.abs(Math.sqrt(Math.pow(x - a.x, 2) + Math.pow(y - a.y, 2))));
                    float distanceIntersection = (distanceMin - distanceCurrent) / 2;
                    float direction = (float) (Math.atan2(y - a.y, x - a.x));

                    x += distanceIntersection * Math.cos(direction);
                    y += distanceIntersection * Math.sin(direction);
                    a.x += distanceIntersection * Math.cos(-direction);
                    a.y += distanceIntersection * Math.sin(-direction);

                }
            }
        }
    }

    public static void loadSounds() {

        sounds[0] = new SoundManager("/sounds/actors/human_hurt_1.wav");
        sounds[1] = new SoundManager("/sounds/actors/human_hurt_2.wav");
        sounds[2] = new SoundManager("/sounds/actors/human_hurt_3.wav");
        sounds[3] = new SoundManager("/sounds/actors/human_hurt_4.wav");
        sounds[4] = new SoundManager("/sounds/actors/human_hurt_5.wav");
        sounds[5] = new SoundManager("/sounds/actors/human_hurt_6.wav");

        int volume = 6;

        sounds[0].setVolume(volume);
        sounds[1].setVolume(volume);
        sounds[2].setVolume(volume);
        sounds[3].setVolume(volume);
        sounds[4].setVolume(volume);
        sounds[5].setVolume(volume);

    }

    // Movement methods:

    private void move(float movementRadians, float movementVelocity) {

        if (isRunning && isMovingForward) {
            movementVelocity *= runningAcceleration;
        }

//        currentMovementRadians = inertiaRadians.update(1, movementRadians);
        currentMovementRadians = movementRadians;

        velocity = inertiaVelocity.update(1, movementVelocity * health);
//        currentMovementVelocity = movementVelocity * health;

        x += velocity * Math.cos(currentMovementRadians);
        y += velocity * Math.sin(currentMovementRadians);

    }

    // Getters:

    public float getHealth() {

        return health;

    }

    // Updaters:

    @Override public void tick() {

        if (!isValid) {
            Actor.allActors.remove(this);
            return;
        }

        if (!isAlive) {
            return;
        }

        if (health <= 0) {
            isAlive = false;
            if (type.equals("zombie")) {
                isValid = false;
                Actor.allActors.remove(this);
                return;
            }
        }

        if (isMovingForward) move(radians, velocityForward);
        if (isMovingBack) move((float) (radians - Math.PI), velocityBack);
        if (isMovingLeft) move((float) (radians - Math.PI / 2), velocityAside);
        if (isMovingRight) move((float) (radians + Math.PI / 2), velocityAside);

        if (!isMovingForward && !isMovingBack && !isMovingLeft && !isMovingRight) {
            velocity = inertiaVelocity.update(1, 0);
            x += velocity * Math.cos(currentMovementRadians);
            y += velocity * Math.sin(currentMovementRadians);
        }

        meleeUpdate();

        detectCollision();

        if (isAttacking && !hasWeapon) {
            if (Client.getT() - meleeTimeLast >= meleeTimePace) {
                meleeAttack();
                meleeTimeLast = Client.getT();
            }
        }

    }

    public void render() {

        if (!isAlive || image == null || !IsVisible()) {
            return;
        }

        BufferedImage imageUpdated = image.get(radians);
        displayPositionPrepare();

        Client.getG().drawImage(imageUpdated, (int) displayX, (int) displayY, null);

        if (Sprite.isBodyRadiusVisible) {
            Client.getG().setColor(bodyColor);
            int roundX = (int) (x - Client.getGX() - bodyRadius);
            int roundY = (int) (y - Client.getGY() - bodyRadius);
            int roundR = (int) (bodyRadius * 2);
            Client.getG().fillRoundRect(roundX, roundY, roundR, roundR, roundR, roundR);
        }

    }

}
