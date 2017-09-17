package sprites;

import java.util.ArrayList;
import java.util.List;

import nightingale.basics.BaseSprite;
import nightingale.structures.Texture;
import nightingale.utilities.UtilsMath;
import managers.SoundManager;

/**
 * This is a weapon which is been used by the owner (an actor with this weapon). If the owner is
 * holding this weapon and is attacking (see more in Actor class) then the weapon shoots and makes
 * bullets in according to its characteristics.
 *
 * Created by Aunmag on 2016.10.03.
 */

public class Weapon extends BaseSprite {

    public static List<Weapon> all = new ArrayList<>();

    private static final Texture texture = Texture.getOrCreate("images/weapons/mp_27");
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
        super(0, 0, 0, texture);

        this.owner = owner;

        soundShot = new SoundManager("/sounds/weapons/shot_mp_27.wav");
        soundShot.setVolume(1);
    }

    public void update() {
        updateOwner();
        updatePosition();

        if (owner.getHasWeapon() && owner.isAttacking && System.currentTimeMillis() >= timeNextShot) {
            makeShot();
        }
    }

    private void updateOwner() {
        if (owner == null || !owner.getIsAlive()) {
            remove();
        }
    }

    private void updatePosition() {
        setRadians(owner.getRadians());
        setPosition(
                owner.getX() + 12 * (float) Math.cos(getRadians()),
                owner.getY() + 12 * (float) Math.sin(getRadians())
        );
    }

    private void makeShot() {
        soundShot.play();

        float muzzleLength = texture.getCenterX();
        float bulletX = getX() + muzzleLength * (float) Math.cos(getRadians());
        float bulletY = getY() + muzzleLength * (float) Math.sin(getRadians());

        for (int bullet = 0; bullet < bulletsPerShot; bullet++) {
            makeBullet(bulletX, bulletY);
        }

        timeNextShot = System.currentTimeMillis() + fireRate;
    }

    private void makeBullet(float x, float y) {
        float bulletRadians = UtilsMath.randomizeFlexibly(getRadians(), deflectionRadians);
        float bulletVelocity = UtilsMath.randomizeFlexibly(velocityMuzzle, deflectionVelocity);
        Bullet bullet = new Bullet(x, y, bulletRadians, bulletVelocity, velocityRecession, owner);
        Bullet.all.add(bullet);
    }

}
