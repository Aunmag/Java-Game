package sprites;

import client.Constants;
import managers.Log;
import managers.MathManager;
import managers.ImageManager;
import managers.Inertia;
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
    private static SoundManager[] sounds = new SoundManager[6];

    private final static ImageManager imageHuman = new ImageManager("actors/human");
    private final static ImageManager imageZombie = new ImageManager("actors/zombie");

    private static Actor player;

    private boolean isAlive = true;
    private boolean hasWeapon = false;
    private float health = 1;
    private int kills = 0;
    public String type;
    private Hands hands = new Hands(this);
    private CollisionCircle collision = new CollisionCircle(this, 7.2f);

    private float velocity = 0;
    private float velocityAside = 0;
    private float velocityBack = 0;
    private float velocitySprint = 0;
    public static float velocityForwardZombie = 0.63f; // TODO: Improve
    private float currentMovementRadians = 0;
    private Inertia inertiaVelocity = new Inertia(0.2f); // TODO: Improve

    public boolean isWalking = false;
    public boolean isWalkingForward = false;
    public boolean isWalkingBack = false;
    public boolean isWalkingLeft = false;
    public boolean isWalkingRight = false;
    public boolean isSprinting = false;
    public boolean isAttacking = false;

    public Actor(float x, float y, float radians, String type) {
        super(x, y, radians, findImage(type));
        this.type = type;

        if (type.equals("human")) {
            velocity = 1.38f;
            velocityAside = velocity * 0.6f;
            velocityBack = velocity * 0.8f;
            velocitySprint = 2.76f;
            hasWeapon = true;
        } else {
            if (!type.equals("zombie")) {
                this.type = "zombie";
                String message = String.format(
                        "Got unknown \"%s\" actor type. Used \"%s\" instead.",
                        type,
                        this.type
                );
                Log.log("Actor", message);
            }
            velocity = velocityForwardZombie;
            velocityAside = velocity * 0.6f;
            velocityBack = velocity * 0.8f;
            velocitySprint = 1.63f;
            hasWeapon = false;
        }
    }

    private static ImageManager findImage(String type) {
        if (type.equals("human")) {
            return imageHuman;
        } else {
            return imageZombie;
        }
    }

    public static void loadSounds() {
        for (int i = 0; i < sounds.length; i++) {
            int number = i + 1;
            SoundManager sound = new SoundManager("/sounds/actors/human_hurt_" + number +".wav");
            sound.setVolume(6);
            sounds[i] = sound;
        }
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
            if (type.equals("zombie")) {
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
            move(radians, velocity);
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
            movementVelocity *= velocitySprint;
        }

        currentMovementRadians = movementRadians;

        float velocityCurrent = inertiaVelocity.update(movementVelocity * health);
        x += velocityCurrent * Math.cos(currentMovementRadians);
        y += velocityCurrent * Math.sin(currentMovementRadians);
    }

    private void stay() {
        float velocityCurrent = inertiaVelocity.update(0);
        x += velocityCurrent * Math.cos(currentMovementRadians);
        y += velocityCurrent * Math.sin(currentMovementRadians);
    }

    public void hit(float intensity, float radians) {
        hit(intensity, radians, null);
    }

    public void hit(float intensity, float radians, Actor attacker) {
        boolean wasAlreadyDead = !isAlive;

        health -= intensity / 100;
        updateIsAlive();

        if (!wasAlreadyDead && !isAlive && attacker != null) {
            attacker.increaseKills();
        }

        float impulse = intensity / 10;
        x += impulse * Math.cos(radians);
        y += impulse * Math.sin(radians);

        if (type.equals("human")) {
            soundHurt();
        }
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

    public void increaseKills() {
        kills++;
    }

    /* Setters */

    public static void setPlayer(Actor player) {
        Actor.player = player;
    }

    /* Getters */

    public static Actor getPlayer() {
        return player;
    }

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

    public int getKills() {
        return kills;
    }

}
