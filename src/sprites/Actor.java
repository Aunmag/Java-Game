package sprites;

import client.Constants;
import managers.Log;
import managers.MathManager;
import scripts.Inertia;
import managers.SoundManager;
import sprites.components.Collision;
import sprites.components.CollisionCircle;
import sprites.components.Hands;

import java.util.*;

/**
 * TODO: Make description
 *
 * Created by Aunmag on 2016.10.23.
 */

public class Actor extends Sprite {

    public static List<Actor> all = new ArrayList<>();
    public static List<Actor> invalids = new ArrayList<>();

    private boolean isAlive = true;
    private boolean hasWeapon = false;

    private float health = 1;

    private float velocityForward;
    private float velocityAside;
    private float velocityBack;
    private float sprintAcceleration;
    public static float velocityForwardZombie = 0.63f; // TODO: Improve

    private float currentMovementRadians = 0;
    private Inertia inertiaVelocity = new Inertia(0.2f); // TODO: Improve

    public String group; // TODO: Maybe create separate class

    public boolean isWalking = false;
    public boolean isWalkingForward = false;
    public boolean isWalkingBack = false;
    public boolean isWalkingLeft = false;
    public boolean isWalkingRight = false;
    public boolean isSprinting = false;
    public boolean isAttacking = false;

    private Hands hands = new Hands(this);
    private CollisionCircle collision = new CollisionCircle(this, 7.2f);

    private static SoundManager[] sounds = new SoundManager[6]; // TODO: Improve

    public Actor(float x, float y, float radians, String imagePath) {

        super(x, y, radians, true, imagePath);

        String[] imagePathData = imagePath.split("/");
        group = imagePathData[1];

        switch (group) {
            case "human.png":
                group = "human";
                velocityForward = 1.38f;
                velocityAside = velocityForward * 0.6f;
                velocityBack = velocityForward * 0.8f;
                sprintAcceleration = 2.76f; // 3.02
                hasWeapon = true;
                break;
            case "zombie.png":
                group = "zombie";
                velocityForward = velocityForwardZombie;
                velocityAside = velocityForward * 0.6f;
                velocityBack = velocityForward * 0.8f;
                sprintAcceleration = 1.63f;
                hasWeapon = false;
                break;
            default:
                group = "unknown";
                velocityForward = 1;
                velocityAside = velocityForward;
                velocityBack = velocityForward;
                sprintAcceleration = 2;
                hasWeapon = false;
                Log.log("Spawn", "Unknown actor spawned.", null);
        }

    }

    public void hit(float intensity, float radiansFrom) {
        health -= intensity / 100;
        updateIsAlive();

        float impulse = intensity / 10;
        x += impulse * Math.cos(radiansFrom);
        y += impulse * Math.sin(radiansFrom);

        if (group.equals("human")) {
            soundHurt();
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

    public void update() {
        updateIsAlive();

        if (!isAlive) {
            return;
        }

        updateIsWalking();

        if (isWalking) {
            walk();
        } else {
            stay();
        }

        updateCollision();
        hands.update();
    }

    private void updateIsAlive() {
        if (!isAlive) {
            return;
        }

        if (health <= 0) {
            health = 0;
            isAlive = false;
            if (group.equals("zombie")) {
                delete();
            }
        }
    }

    private void updateIsWalking() {
        isWalking = isWalkingForward || isWalkingLeft || isWalkingRight || isWalkingBack;
    }

    private void updateCollision() {
        collision.setPosition(x, y);

        for (Actor actor: all) {
            if (!actor.isAlive || actor.equals(this)) {
                continue;
            }

            if (Collision.calculateIsCollision(collision, actor.getCollision())) {
                float distanceBetween = collision.getLastDistanceBetween();
                float distanceToCollision = collision.getRadius() + actor.getCollision().getRadius();
                float distanceIntersection = (distanceToCollision - distanceBetween) / 2;
                float radiansBetween = MathManager.calculateRadiansBetween(this, actor);
                x += distanceIntersection * Math.cos(radiansBetween);
                y += distanceIntersection * Math.sin(radiansBetween);
                actor.x += distanceIntersection * Math.cos(-radiansBetween);
                actor.y += distanceIntersection * Math.sin(-radiansBetween);
            }
        }
    }

    private void walk() {
        if (isWalkingForward) {
            move(radians, velocityForward);
        }

        if (isWalkingBack) {
            move(radians - (float) Math.PI, velocityBack);
        }

        if (isWalkingLeft) {
            move(radians - (float) Constants.PI_0_5, velocityAside);
        }

        if (isWalkingRight) {
            move(radians + (float) Constants.PI_0_5, velocityAside);
        }
    }

    private void move(float movementRadians, float movementVelocity) {
        if (isSprinting && isWalkingForward) {
            movementVelocity *= sprintAcceleration;
        }

        currentMovementRadians = movementRadians;

        float velocity = inertiaVelocity.update(1, movementVelocity * health);
        x += velocity * Math.cos(currentMovementRadians);
        y += velocity * Math.sin(currentMovementRadians);
    }

    private void stay() {
        float velocity = inertiaVelocity.update(1, 0);
        x += velocity * Math.cos(currentMovementRadians);
        y += velocity * Math.sin(currentMovementRadians);
    }

    public void render() {
        super.render();
        hands.render();
        collision.render();
    }

    private void soundHurt() {
        sounds[MathManager.random.nextInt(6)].play();
    }

    public void delete() {
        isValid = false;
        invalids.add(this);
    }

    /* Getters */

    public float getHealth() {
        return health;
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    public boolean getHasWeapon() {
        return hasWeapon;
    }

    public CollisionCircle getCollision() {
        return collision;
    }

    public Hands getHands() {
        return hands;
    }

}
