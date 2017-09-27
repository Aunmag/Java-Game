package aunmag.shooter.sprites;

import aunmag.shooter.managers.SoundManager;
import aunmag.shooter.world.World;
import aunmag.nightingale.basics.BaseSprite;
import aunmag.nightingale.structures.Texture;
import aunmag.nightingale.utilities.UtilsMath;

public class Weapon extends BaseSprite {

    private static final Texture texture = Texture.getOrCreate("images/weapons/mp_27");
    private static final int fireRate = 700; // TODO: Rename
    private static final int bulletsPerShot = 16;
    private static final float velocityMuzzle = 58;
    private static final float velocityRecession = 69; // TODO: Implement bullet weight
    private static final float deflectionVelocity = 2;
    private static final float deflectionRadians = 0.06f;
    private static final float recoil = 0.06f;

    private long timeNextShot = 0;
    private SoundManager soundShot;

    public Weapon() {
        super(0, 0, 0, texture);

        soundShot = new SoundManager("/sounds/weapons/shot_mp_27.wav");
        soundShot.setVolume(1);
    }

    public void update() {}

    public void makeShotBy(Actor shooter) {
        if (System.currentTimeMillis() < timeNextShot) {
            return;
        }

        float push = UtilsMath.randomizeFlexibly(recoil, recoil / 4f);
        shooter.push(UtilsMath.random.nextBoolean() ? push : -push);

        soundShot.play();

        float muzzleLength = texture.getCenterX();
        float bulletX = getX() + muzzleLength * (float) Math.cos(getRadians());
        float bulletY = getY() + muzzleLength * (float) Math.sin(getRadians());

        for (int bullet = 0; bullet < bulletsPerShot; bullet++) {
            makeBullet(shooter, bulletX, bulletY);
        }

        timeNextShot = System.currentTimeMillis() + fireRate;
    }

    private void makeBullet(Actor shooter, float x, float y) {
        float radians = UtilsMath.randomizeFlexibly(getRadians(), deflectionRadians);
        float velocity = UtilsMath.randomizeFlexibly(velocityMuzzle, deflectionVelocity);
        Bullet bullet = new Bullet(x, y, radians, velocity, velocityRecession, shooter);
        World.bullets.add(bullet);
    }

}
