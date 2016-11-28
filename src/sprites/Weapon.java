package sprites;

import java.util.ArrayList;
import java.util.List;
import client.Client;
import scripts.FRandom;
import managers.SoundManager;

/**
 * This is a weapon which is been used by the owner (an actor with this weapon). If the owner is holding this weapon and
 * is attacking (see more in Actor class) then the weapon shoots and makes bullets in according to its characteristics.
 *
 * Created by Aunmag on 03.10.2016.
 */

public class Weapon extends Sprite {

    public static List<Weapon> allWeapons = new ArrayList<>(); // the all valid weapons must keep here

    // Description:
    protected final String name; // uses for file paths
    protected final String title;

    // Characteristics:
    private final double fireRate; // time between each shoot
    private final double bulletsPerShot; // how many bullets will make per a shot
    private final double vMuzzle; // muzzle velocity of bullet
    private final double vKeep; // keep velocity of bullet
    private final double deflection; // deflection of bullet from straight direction

    // Other:
    private Actor owner; // an actor with hold this weapon
    private long tLastShoot = 0; // the time of the last shot
    private SoundManager soundShot = new SoundManager("/sounds/weapons/shot_mp_27.wav");

    public Weapon(Actor owner) {

        // Set basic sprite data:
        super(0, 0, 0, true, "weapons/mp_27.png");

        // Set characteristics:
        name = "test_weapon";
        title = "Test Weapon";
        fireRate = 700_000_000;
        bulletsPerShot = 16;
        vMuzzle = 58;
        vKeep = 0.92;
        deflection = 2;

        // Set owner:
        this.owner = owner;

        soundShot.setVolume(1);

    }

    private void makeShot() {

        // Make new bullets in according to number bullets per a shot:
        for (int bullet = 0; bullet < bulletsPerShot; bullet++) {
            double bulletVelocity = FRandom.rand(vMuzzle, deflection, 1); // random velocity of the bullet
            double bulletDegrees = FRandom.rand(degrees, deflection, 1) % 360; // random direction of the bullet
            Bullet.allBullets.add(new Bullet(x, y, bulletDegrees, bulletVelocity, vKeep)); // make the bullet
        }

        soundShot.play();

    }

    @Override public void tick() {

        // Disable weapon if owner is too:
        if (!isValid || (!owner.isAlive || !owner.isValid)) {
            isValid = false;
            allWeapons.remove(this);
            return;
        }

        // Put weapon in owner hands:
        setDegrees(owner.getDegrees());
        x = owner.x + 12 * Math.cos(radians);
        y = owner.y + 12 * Math.sin(radians);

        // Make shot in according to fire rate if owner is attacking and has weapon:
        if (owner.isAttacking && owner.hasWeapon && Client.getT() - tLastShoot >= fireRate) {
            makeShot();
            tLastShoot = Client.getT();
        }

    }

}
