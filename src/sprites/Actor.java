package sprites;

import client.Application;
import nightingale.utilities.FloatSmooth;
import nightingale.utilities.UtilsLog;
import nightingale.utilities.UtilsMath;
import managers.ImageManager;
import managers.SoundManager;
import nightingale.collision.CollisionCircle;
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
    private CollisionCircle collision = new CollisionCircle(getX(), getY(), 7.2f);

    private float velocity = 0;
    private float velocityAside = 0;
    private float velocityBack = 0;
    private float velocitySprint = 0;
    public static float velocityForwardZombie = 0.63f; // TODO: Improve
    private float currentMovementRadians = 0;
    private FloatSmooth inertiaVelocity = new FloatSmooth(250);

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

        inertiaVelocity.setFlexDegree(0.75f);

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
                UtilsLog.log("Actor", message);
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

        inertiaVelocity.update(Application.getTimeCurrent());

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
        collision.setPosition(getX(), getY());

        for (Actor actor: all) {
            if (!actor.isAlive || actor.equals(this)) {
                continue;
            }

            collision.preventCollisionWith(actor.collision);
            setPosition(collision.getX(), collision.getY());
            actor.setPosition(actor.collision.getX(), actor.collision.getY());
        }
    }

    private void walk() {
        if (isWalkingForward) {
            move(getRadians(), velocity);
        }

        if (isWalkingBack) {
            move(getRadians() - (float) Math.PI, velocityBack);
        }

        if (isWalkingLeft) {
            move(getRadians() - (float) UtilsMath.PIx0_5, velocityAside);
        }

        if (isWalkingRight) {
            move(getRadians() + (float) UtilsMath.PIx0_5, velocityAside);
        }
    }

    private void move(float movementRadians, float movementVelocity) {
        if (isSprinting && isWalkingForward) {
            movementVelocity *= velocitySprint;
        }

        currentMovementRadians = movementRadians;

        inertiaVelocity.setValueTarget(movementVelocity * health, Application.getTimeCurrent());
        float velocityCurrent = inertiaVelocity.getValueCurrent();

        addPosition(
                velocityCurrent * (float) Math.cos(currentMovementRadians),
                velocityCurrent * (float) Math.sin(currentMovementRadians)
        );
    }

    private void stay() {
        inertiaVelocity.setValueTarget(0, Application.getTimeCurrent());
        float velocityCurrent = inertiaVelocity.getValueCurrent();

        addPosition(
                velocityCurrent * (float) Math.cos(currentMovementRadians),
                velocityCurrent * (float) Math.sin(currentMovementRadians)
        );
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
        addPosition(
                impulse * (float) Math.cos(radians),
                impulse * (float) Math.sin(radians)
        );

        if (type.equals("human")) {
            soundHurt();
        }
    }

    public void render() {
        super.render();
//        hands.render();
//        collision.render();
    }

    private void soundHurt() {
        sounds[UtilsMath.random.nextInt(6)].play();
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
