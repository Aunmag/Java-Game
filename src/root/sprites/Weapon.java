package root.sprites;

// Created by Aunmag on 03.10.2016.

import java.util.ArrayList;
import java.util.List;
import root.scripts.FRandom;

public class Weapon extends Sprite {

    public static List<Weapon> allWeapons = new ArrayList<>();

    // Description:
    protected final String name; // this name uses in files
    protected final String title; // just title of weapon for players

    // Specifications:
    protected final double fireRate; // time between each shoot
    protected final double bulletsPerShot; // how many bullets creates per a shoot

    // Ballistics:
    protected final double velocityMuzzle; // muzzle velocity of bullet
    protected final double keepVelocity; // keep velocity of bullet
    protected final double deflection; // deflection of bullet from straight direction

    // Other:
    protected Actor owner;
    protected long lastTime;
    protected long lastShoot = 0;

    public Weapon(Actor owner) {

        super(0, 0, 0, true, "/weapons/aks74u2.png");

        name = "test_weapon";
        title = "Test Weapon";
        fireRate = 700000000;
        bulletsPerShot = 12;
        velocityMuzzle = 52;
        keepVelocity = 0.92;
        deflection = 4;

//        name = "test_weapon";
//        title = "Test Weapon";
//        fireRate = 75_000_000;
//        bulletsPerShot = 1;
//        velocityMuzzle = 60;
//        keepVelocity = 0.95;
//        deflection = 2;

        this.owner = owner;

    }

    private void makeShot() {

        for (int bullet = 0; bullet < bulletsPerShot; bullet++) {
            double bulletVelocity = FRandom.rand(velocityMuzzle, deflection, 1);
            double bulletDegrees = FRandom.rand(degrees, deflection, 1) % 360;
            Bullet.allBullets.add(new Bullet(x, y, bulletDegrees, bulletVelocity, keepVelocity));
        }

    }

    @Override public void tick() {

        if (!owner.isAlive || !owner.isValid) {
            isValid = false;
        }

        x = owner.x + 22 * Math.cos(radians);
        y = owner.y + 22 * Math.sin(radians);
        setDegrees(owner.getDegrees());

        if (owner.isAttacking && owner.hasWeapon) {
            lastTime = System.nanoTime();
            if (lastTime - lastShoot >= fireRate) {
                lastShoot = lastTime;
                makeShot();
            }
        }

    }

}
