package aunmag.shooter.actor;

import aunmag.nightingale.audio.AudioSample;
import aunmag.nightingale.audio.AudioSampleType;
import aunmag.nightingale.audio.AudioSource;
import aunmag.nightingale.utilities.FluidToggle;
import aunmag.shooter.client.Game;
import aunmag.shooter.client.graphics.CameraShaker;
import aunmag.shooter.weapon.Weapon;
import aunmag.nightingale.utilities.FluidValue;
import aunmag.nightingale.utilities.UtilsMath;
import aunmag.nightingale.collision.CollisionCircle;
import aunmag.shooter.world.World;

public class Actor extends CollisionCircle {

    private static Actor player;
    private static final int[] samples = new int[6];
    private static final float velocityFactorAside = 0.6f;
    private static final float velocityFactorBack = 0.8f;
    private static int indexOfLastCollisionCheckedActor = 0;

    public final World world;
    public final ActorType type;
    private float health = 1;
    private int kills = 0;
    private Weapon weapon = null;
    private Hands hands = new Hands(this);
    private AudioSource audioSource = new AudioSource();

    private FluidValue offsetRadians = new FluidValue(0.06f);
    public boolean isWalkingForward = false;
    public boolean isWalkingBack = false;
    public boolean isWalkingLeft = false;
    public boolean isWalkingRight = false;
    public boolean isSprinting = false;
    public boolean isAttacking = false;
    public FluidToggle isAiming = new FluidToggle(0.25f);

    static {
        for (int i = 0; i < samples.length; i++) {
            String sampleName = "sounds/actors/human_hurt_" + (i + 1);
            samples[i] = AudioSample.getOrCreate(sampleName, AudioSampleType.OGG);
        }
    }

    public Actor(ActorType type, World world) {
        super(0, 0, 0.225f);
        this.type = type;
        this.world = world;
        offsetRadians.setFlexDegree(0.5f);
        isAiming.setFlexDegree(1.25f);
    }

    public void update() {
        if (!isAlive()) {
            remove();
            return;
        }

        offsetRadians.update(world.getTime().getCurrent());
        if (offsetRadians.getValueTarget() != 0 && offsetRadians.isTargetReached()) {
            addRadiansCarefully(offsetRadians.getValueCurrent());
            offsetRadians.setValueTarget(0, world.getTime().getCurrent());
            offsetRadians.reachTargetNow();
        }

        isAiming.update(world.getTime().getCurrent());
        walk();
        updateCollision();
        hands.update();
        updateWeapon();
    }

    private void updateCollision() {
        for (int index = world.getActors().size() - 1; index >= 0; index--) {
            if (index == indexOfLastCollisionCheckedActor) {
                break;
            }

            Actor opponent = world.getActors().get(index);

            if (!opponent.isAlive() || opponent.isRemoved() || opponent == this) {
                continue;
            }

            preventCollisionWith(opponent);
        }

        indexOfLastCollisionCheckedActor++;
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

    private void updateWeaponPosition() {
        if (weapon != null) {
            weapon.setRadians(getRadians());
            float x = getX() + 0.375f * (float) Math.cos(getRadians());
            float y = getY() + 0.375f * (float) Math.sin(getRadians());
            weapon.setPosition(x, y);
        }
    }

    private void walk() {
        if (isWalkingForward) {
            move(type.velocity, 0);
        }

        if (isWalkingBack) {
            move(type.velocity * velocityFactorBack, (float) -Math.PI);
        }

        if (isWalkingLeft) {
            move(type.velocity * velocityFactorAside, (float) UtilsMath.PIx0_5);
        }

        if (isWalkingRight) {
            move(type.velocity * velocityFactorAside, (float) -UtilsMath.PIx0_5);
        }
    }

    private void move(double velocity, float radiansTurn) {
        if (isSprinting && isWalkingForward) {
            velocity *= type.velocityFactorSprint;
        }

        velocity -= velocity * isAiming.getValueCurrent() / 2f;
        velocity *= health;
        velocity *= world.getTime().getDelta();

        float moveX = (float) (velocity * Math.cos(getRadians() + radiansTurn));
        float moveY = (float) (velocity * Math.sin(getRadians() + radiansTurn));
        addPosition(moveX, moveY);
    }

    public void hit(float intensity, Actor attacker) {
        intensity /= type.strength;

        boolean wasDeadBefore = !isAlive();
        addHealth(-intensity);

        if (!wasDeadBefore && !isAlive() && attacker != null) {
            attacker.increaseKills();
        }

        push(UtilsMath.random.nextBoolean() ? intensity : -intensity);
    }

    public void push(float force) {
        offsetRadians.setValueTarget(force, world.getTime().getCurrent());

        if (this == Actor.player) {
            CameraShaker.shake(force);
        }
    }

    public void render() {
        hands.render();
        super.render();
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
        if (type != ActorType.human) {
            return;
        }

        if (audioSource.isPlaying()) {
            return;
        }

        int sample = samples[UtilsMath.random.nextInt(samples.length)];
        audioSource.setSample(sample);
        audioSource.play();
    }

    private void increaseKills() {
        kills++;
    }

    public static void finalizeUpdate() {
        indexOfLastCollisionCheckedActor = 0;
    }

    /* Setters */

    private void addHealth(float addHealth) {
        health = UtilsMath.limitNumber(health + addHealth, 0, 1);

        if (!isAlive()) {
            remove();
        } else if (addHealth < -0.005) {
            soundHurt();
        }
    }

    public void setPosition(float x, float y) {
        super.setPosition(x, y);

        hands.updatePosition();
        audioSource.setPosition(getX(), getY());
        updateWeaponPosition();
    }

    public void setRadians(float radians) {
        super.setRadians(radians);
        updateWeaponPosition(); // TODO: Optimize
    }

    public static void setPlayer(Actor player) {
        Actor.player = player;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
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

    public Hands getHands() {
        return hands;
    }

    public int getKills() {
        return kills;
    }

    public Weapon getWeapon() {
        return weapon;
    }

}
