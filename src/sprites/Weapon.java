package sprites;

import java.util.ArrayList;
import java.util.List;
import client.Client;
import managers.MathManager;
import managers.SoundManager;

/**
 * This is a weapon which is been used by the owner (an actor with this weapon). If the owner is
 * holding this weapon and is attacking (see more in Actor class) then the weapon shoots and makes
 * bullets in according to its characteristics.
 *
 * Created by Aunmag on 2016.10.03.
 */

public class Weapon extends Sprite {

    public static List<Weapon> all = new ArrayList<>();
    public static List<Weapon> invalids = new ArrayList<>();

    private static final int fireRate = 700; // TODO: Rename
    private static final int bulletsPerShot = 16;
    private static final float velocityMuzzle = 58;
    private static final float velocityRecession = 69; // TODO: Implement bullet weight
    private static final float deflectionVelocity = 2;
    private static final float deflectionRadians = 0.06f;

    private Actor owner;
    private long timeNextShot = 0;
    private SoundManager soundShot;

    public Weapon(Actor owner) {
        super(0, 0, 0, true, "weapons/mp_27.png");

        this.owner = owner;

        soundShot = new SoundManager("/sounds/weapons/shot_mp_27.wav");
        soundShot.setVolume(1);
    }

    public void update() {
        updateOwner();
        updatePosition();

        if (owner.getHasWeapon() && owner.isAttacking && Client.getT() >= timeNextShot) {
            makeShot();
        }
    }

    private void updateOwner() {
        if (owner == null || !owner.getIsAlive()) {
            delete();
        }
    }

    private void updatePosition() {
        radians = owner.getRadians();
        x = owner.getX() + 12 * (float) Math.cos(radians);
        y = owner.getY() + 12 * (float) Math.sin(radians);
    }

    private void makeShot() {
        soundShot.play();

        for (int bullet = 0; bullet < bulletsPerShot; bullet++) {
            makeBullet();
        }

        timeNextShot = Client.getT() + fireRate;
    }

    private void makeBullet() {
        float bulletRadians = MathManager.randomizeFlexibly(radians, deflectionRadians);
        float bulletVelocity = MathManager.randomizeFlexibly(velocityMuzzle, deflectionVelocity);
        Bullet.all.add(new Bullet(x, y, bulletRadians, bulletVelocity, velocityRecession));
    }

    public void delete() {
        isValid = false;
        invalids.add(this);
    }

}
