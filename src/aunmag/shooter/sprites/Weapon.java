package aunmag.shooter.sprites;

import aunmag.shooter.managers.SoundManager;
import aunmag.shooter.world.World;
import aunmag.nightingale.basics.BaseSprite;
import aunmag.nightingale.structures.Texture;
import aunmag.nightingale.utilities.UtilsMath;

public class Weapon extends BaseSprite {

    private static final Texture texture;
    private static final SoundManager sound;

    static {
        texture = Texture.getOrCreate("images/weapons/mp_27");
        sound = new SoundManager("/sounds/weapons/shot_mp_27.wav");
        sound.setVolume(1);
    }

    private int fireRate;
    private int bulletsPerShot;
    private float velocityMuzzle;
    private float velocityRecession; // TODO: Implement bullet weight
    private float velocityDeflectionFactor;
    private float deflectionRadians;
    private float recoil;

    private long timeNextShot = 0;

    public Weapon(
            int fireRate,
            int bulletsPerShot,
            float velocityMuzzle,
            float velocityRecession,
            float velocityDeflectionFactor,
            float deflectionRadians,
            float recoil
    ) {
        super(0, 0, 0, texture);

        this.fireRate = fireRate;
        this.bulletsPerShot = bulletsPerShot;
        this.velocityMuzzle = velocityMuzzle;
        this.velocityRecession = velocityRecession;
        this.velocityDeflectionFactor = velocityDeflectionFactor;
        this.deflectionRadians = deflectionRadians;
        this.recoil = recoil;
    }

    public void update() {}

    public void makeShotBy(Actor shooter) {
        if (System.currentTimeMillis() < timeNextShot) {
            return;
        }

        float push = UtilsMath.randomizeFlexibly(recoil, recoil / 4f);
        shooter.push(UtilsMath.random.nextBoolean() ? push : -push);

        sound.play();

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
        float velocity = UtilsMath.randomizeFlexibly(
                velocityMuzzle,
                velocityMuzzle * velocityDeflectionFactor
        );
        Bullet bullet = new Bullet(x, y, radians, velocity, velocityRecession, shooter);
        World.bullets.add(bullet);
    }

}
