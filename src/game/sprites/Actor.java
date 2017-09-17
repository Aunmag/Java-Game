package game.sprites;

import nightingale.basics.BaseSprite;
import nightingale.structures.Texture;
import nightingale.utilities.FloatSmooth;
import nightingale.utilities.UtilsLog;
import nightingale.utilities.UtilsMath;
import game.managers.SoundManager;
import nightingale.collision.CollisionCircle;
import game.sprites.components.Hands;

import java.util.*;

public class Actor extends BaseSprite {

    public static List<Actor> all = new ArrayList<>();
    private static SoundManager[] sounds = new SoundManager[6];
    private static Actor player;

    private boolean isAlive = true;
    private boolean hasWeapon = false;
    private float health = 1;
    private int kills = 0;
    public String type;
    private Weapon weapon = null;
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
        super(x, y, radians, findTexture(type));
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

    private static Texture findTexture(String type) {
        if (type.equals("human")) {
            return Texture.getOrCreate("images/actors/human");
        } else {
            return Texture.getOrCreate("images/actors/zombie");
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

        inertiaVelocity.update(System.currentTimeMillis());

        updateIsWalking();

        if (isWalking) {
            walk();
        } else {
            stay();
        }

        updateCollision();
        hands.update();
        updateWeapon();
    }

    private void updateIsAlive() {
        if (!isAlive) {
            return;
        }

        if (health <= 0) {
            health = 0;
            isAlive = false;
            if (type.equals("zombie")) {
                remove();
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

    private void updateWeapon() {
        if (weapon == null) {
            return;
        }

        weapon.setRadians(getRadians());
        float weaponX = getX() + 12 * (float) Math.cos(getRadians());
        float weaponY = getY() + 12 * (float) Math.sin(getRadians());
        weapon.setPosition(weaponX, weaponY);

        if (isAttacking) {
            weapon.makeShotBy(this);
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
            move(getRadians() + (float) UtilsMath.PIx0_5, velocityAside);
        }

        if (isWalkingRight) {
            move(getRadians() - (float) UtilsMath.PIx0_5, velocityAside);
        }
    }

    private void move(float movementRadians, float movementVelocity) {
        if (isSprinting && isWalkingForward) {
            movementVelocity *= velocitySprint;
        }

        currentMovementRadians = movementRadians;

        inertiaVelocity.setValueTarget(movementVelocity * health, System.currentTimeMillis());
        float velocityCurrent = inertiaVelocity.getValueCurrent();

        addPosition(
                velocityCurrent * (float) Math.cos(currentMovementRadians),
                velocityCurrent * (float) Math.sin(currentMovementRadians)
        );
    }

    private void stay() {
        inertiaVelocity.setValueTarget(0, System.currentTimeMillis());
        float velocityCurrent = inertiaVelocity.getValueCurrent();

        addPosition(
                velocityCurrent * (float) Math.cos(currentMovementRadians),
                velocityCurrent * (float) Math.sin(currentMovementRadians)
        );
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
        if (weapon != null) {
            weapon.render();
        }
        super.render();
//        hands.render();
//        collision.render();
    }

    public void remove() {
        if (weapon != null) {
            weapon.remove();
        }
        super.remove();
    }

    private void soundHurt() {
        sounds[UtilsMath.random.nextInt(6)].play();
    }

    public void increaseKills() {
        kills++;
    }

    /* Setters */

    public static void setPlayer(Actor player) {
        Actor.player = player;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
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
