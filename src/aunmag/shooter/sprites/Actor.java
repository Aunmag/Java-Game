package aunmag.shooter.sprites;

import aunmag.nightingale.Configs;
import aunmag.nightingale.audio.AudioSample;
import aunmag.nightingale.audio.AudioSampleType;
import aunmag.nightingale.audio.AudioSource;
import aunmag.nightingale.utilities.FluidToggle;
import aunmag.shooter.client.Game;
import aunmag.shooter.client.graphics.CameraShaker;
import aunmag.shooter.weapon.Weapon;
import aunmag.shooter.world.World;
import aunmag.nightingale.basics.BaseSprite;
import aunmag.nightingale.structures.Texture;
import aunmag.nightingale.utilities.FluidValue;
import aunmag.nightingale.utilities.UtilsMath;
import aunmag.nightingale.collision.CollisionCircle;
import aunmag.shooter.sprites.components.Hands;

public class Actor extends BaseSprite {

    private static int[] samples = new int[6];
    private static Actor player;

    private float health = 1;
    private int kills = 0;
    public String type;
    private Weapon weapon = null;
    private Hands hands = new Hands(this);
    private CollisionCircle collision = new CollisionCircle(getX(), getY(), 0.225f);
    private AudioSource audioSource = new AudioSource();

    private float velocity;
    private float velocityFactorAside;
    private float velocityFactorBack;
    private float velocityFactorSprint;

    private float currentMovementRadians = 0;
    private FluidValue inertiaVelocity = new FluidValue(250);
    private FluidValue offsetRadians = new FluidValue(60);

    public boolean isWalking = false;
    public boolean isWalkingForward = false;
    public boolean isWalkingBack = false;
    public boolean isWalkingLeft = false;
    public boolean isWalkingRight = false;
    public boolean isSprinting = false;
    public boolean isAttacking = false;
    public FluidToggle isAiming = new FluidToggle(250);

    static {
        for (int i = 0; i < samples.length; i++) {
            String sampleName = "sounds/actors/human_hurt_" + (i + 1);
            samples[i] = AudioSample.getOrCreate(sampleName, AudioSampleType.OGG);
        }
    }

    public Actor(
            float velocity,
            float velocityFactorAside,
            float velocityFactorBack,
            float velocityFactorSprint,
            Texture texture,
            String type
    ) {
        super(0, 0, 0, texture);
        this.velocity = velocity;
        this.velocityFactorAside = velocityFactorAside;
        this.velocityFactorBack = velocityFactorBack;
        this.velocityFactorSprint = velocityFactorSprint;
        this.type = type;

        inertiaVelocity.setFlexDegree(0.75f);
        offsetRadians.setFlexDegree(0.5f);
        isAiming.setFlexDegree(1.25f);
    }

    public void update() {
        if (!isAlive()) {
            remove();
            return;
        }

        offsetRadians.update(System.currentTimeMillis());
        if (offsetRadians.getValueTarget() != 0 && offsetRadians.isTargetReached()) {
            addRadiansCarefully(offsetRadians.getValueCurrent());
            offsetRadians.setValueTarget(0, System.currentTimeMillis());
            offsetRadians.reachTargetNow();
        }

        inertiaVelocity.update(System.currentTimeMillis());
        isAiming.update(System.currentTimeMillis());

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

    private void updateIsWalking() {
        isWalking = isWalkingForward || isWalkingLeft || isWalkingRight || isWalkingBack;
    }

    private void updateCollision() {
        for (Actor opponent: World.actors) {
            if (!opponent.isAlive() || opponent.isRemoved() || opponent == this) {
                continue;
            }

            collision.preventCollisionWith(opponent.collision);
            setPosition(collision.getX(), collision.getY());
            opponent.setPosition(opponent.collision.getX(), opponent.collision.getY());
        }
    }

    private void updateWeapon() {
        if (weapon == null) {
            return;
        }

        if (isAttacking) {
            weapon.trigger.pressBy(this);
        } else {
            weapon.trigger.release();
        }

        weapon.update();
    }

    private void walk() {
        if (isWalkingForward) {
            move(velocity, getRadians());
        }

        if (isWalkingBack) {
            move(velocity * velocityFactorBack, getRadians() - (float) Math.PI);
        }

        if (isWalkingLeft) {
            move(velocity * velocityFactorAside, getRadians() + (float) UtilsMath.PIx0_5);
        }

        if (isWalkingRight) {
            move(velocity * velocityFactorAside, getRadians() - (float) UtilsMath.PIx0_5);
        }
    }

    private void move(float velocity, float radians) {
        if (isSprinting && isWalkingForward) {
            velocity *= velocityFactorSprint;
        }
        velocity -= velocity * isAiming.getValueCurrent() / 2f;

        currentMovementRadians = radians;

        inertiaVelocity.setValueTarget(velocity * health, System.currentTimeMillis());
        float velocityCurrent = inertiaVelocity.getValueCurrent();

        float moveX = velocityCurrent * (float) Math.cos(currentMovementRadians);
        float moveY = velocityCurrent * (float) Math.sin(currentMovementRadians);
        addPosition(moveX, moveY);
    }

    private void stay() {
        inertiaVelocity.setValueTarget(0, System.currentTimeMillis());
        float velocityCurrent = inertiaVelocity.getValueCurrent();

        addPosition(
                velocityCurrent * (float) Math.cos(currentMovementRadians),
                velocityCurrent * (float) Math.sin(currentMovementRadians)
        );
    }

    // TODO: Change
    public void hit(float intensity, float radians, Actor attacker) {
        boolean wasDeadBefore = !isAlive();

        addHealth(-intensity * Configs.getPixelsPerMeter() / 7500f);

        if (!wasDeadBefore && !isAlive() && attacker != null) {
            attacker.increaseKills();
        }

        float impulse = intensity / 750f / Configs.getPixelsPerMeter();
        float impulseX = impulse * (float) Math.cos(radians);
        float impulseY = impulse * (float) Math.sin(radians);
        addPosition(impulseX, impulseY);
    }

    public void push(float force) {
        offsetRadians.setValueTarget(force, System.currentTimeMillis());

        if (this == Actor.player) {
            CameraShaker.shake(force);
        }
    }

    public void render() {
        if (weapon != null) {
            weapon.render();
        }

        if (Game.isVirtualMode()) {
            hands.render();
            collision.render();
        } else {
            super.render();
        }
    }

    public void remove() {
        if (isRemoved()) {
            return;
        }

        if (weapon != null) {
            weapon.remove();
        }

        super.remove();
    }

    private void soundHurt() {
        if (!type.equals("human")) {
            return;
        }

        if (audioSource.isPlaying()) {
            return;
        }

        int sample = samples[UtilsMath.random.nextInt(6)];
        audioSource.setSample(sample);
        audioSource.play();
    }

    public void increaseKills() {
        kills++;
    }

    /* Setters */

    private void addHealth(float addHealth) {
        health += addHealth;

        if (health < 0) {
            health = 0;
        } else if (health > 1) {
            health = 1;
        }

        if (!isAlive()) {
            remove();
        } else if (addHealth < -0.005) {
            soundHurt();
        }
    }

    public void setPosition(float x, float y) {
        super.setPosition(x, y);

        collision.setPosition(getX(), getY());
        hands.updatePosition();
        audioSource.setPosition(getX(), getY());

        if (weapon != null) {
            weapon.setRadians(getRadians());
            float weaponX = getX() + 0.375f * (float) Math.cos(getRadians());
            float weaponY = getY() + 0.375f * (float) Math.sin(getRadians());
            weapon.setPosition(weaponX, weaponY);
        }
    }

    public static void setPlayer(Actor player) {
        Actor.player = player;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    /* Getters */

    public float getRadians() {
        if (offsetRadians != null) {
            return super.getRadians() + offsetRadians.getValueCurrent();
        } else {
            return super.getRadians();
        }
    }

    public static Actor getPlayer() {
        return player;
    }

    public float getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean getHasWeapon() {
        return weapon != null;
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

    public float getVelocity() {
        return velocity;
    }

    public Weapon getWeapon() {
        return weapon;
    }

}
