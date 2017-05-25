package sprites;

import java.util.ArrayList;
import java.util.List;
import client.Client;
import managers.MathManager;
import managers.SoundManager;

/**
 * This is a weapon which is been used by the owner (an actor with this weapon). If the owner is holding this weapon and
 * is attacking (see more in Actor class) then the weapon shoots and makes bullets in according to its characteristics.
 *
 * Created by Aunmag on 03.10.2016.
 */

public class Weapon extends Sprite {

    public static List<Weapon> all = new ArrayList<>(); // the all valid weapons must keep here

    // Characteristics:
    private final float fireRate; // time between each shoot
    private final float bulletsPerShot; // how many bullets will make per a shot
    private final float vMuzzle; // muzzle velocity of bullet
    private final float vRecession; // how fast bullet lose its velocity per a second
    private final float deflection; // deflection of bullet from straight direction

    // Misc:
    private Actor owner; // an actor witch hold this weapon
    private long tLastShoot = 0; // the time of the last shot
    private SoundManager soundShot;

    public Weapon(Actor owner) {

        // Set basic sprite data:
        super(0, 0, 0, true, "weapons/mp_27.png");

        // Set characteristics:
        fireRate = 700; // 700 or 130
        bulletsPerShot = 16; // 16 or 1
        vMuzzle = 58; // 58 or 61
        vRecession = 69;
//        vRecession = 0.92;
        deflection = 2; // 2 or 1.2

        // Set owner:
        this.owner = owner;

        // Set sound:
        soundShot = new SoundManager("/sounds/weapons/shot_mp_27.wav");
        soundShot.setVolume(1); // tweak volume

    }

    private void makeShot() {

        /*
         * Make required amount of bullets and play shot sound.
         */

        // Make new bullets in according to number bullets per a shot:
        for (int bullet = 0; bullet < bulletsPerShot; bullet++) {
            float bulletVelocity = MathManager.randomizeBetween(vMuzzle - deflection, vMuzzle + deflection); // random velocity of the bullet
            float deflectionRadians = deflection / 60f;
            float bulletRadians = MathManager.randomizeBetween(radians - deflectionRadians, radians + deflectionRadians); // random direction of the bullet
            Bullet.all.add(new Bullet(x, y, bulletRadians, bulletVelocity, vRecession)); // make the bullet
        }

        soundShot.play();

    }

    @Override public void tick() {

        // Disable weapon if owner is invalid:
        if (!isValid || (!owner.isAlive || !owner.isValid)) {
            isValid = false;
            all.remove(this);
            return;
        }

        // Put weapon in owner hands:
        setRadians(owner.getRadians());
        x = (float) (owner.x + 12 * Math.cos(radians));
        y = (float) (owner.y + 12 * Math.sin(radians));

        // Make shot in according to fire rate if owner is attacking and has weapon:
        if (owner.isAttacking && owner.hasWeapon && Client.getT() - tLastShoot >= fireRate) {
            makeShot();
            tLastShoot = Client.getT();
        }

    }

}
