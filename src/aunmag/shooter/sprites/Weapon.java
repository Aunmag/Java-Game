package aunmag.shooter.sprites;

import aunmag.nightingale.audio.AudioMaster;
import aunmag.nightingale.audio.AudioSource;
import aunmag.shooter.world.World;
import aunmag.nightingale.basics.BaseSprite;
import aunmag.nightingale.structures.Texture;
import aunmag.nightingale.utilities.UtilsMath;

public class Weapon extends BaseSprite {

    private static final Texture texture;
    private static final int sound;
    private AudioSource audioSource = new AudioSource();

    static {
        texture = Texture.getOrCreate("images/weapons/mp_27");
        sound = AudioMaster.getOrCreate("sounds/weapons/shot_mp_27");
    }

    private int fireRate;
    private int bulletsPerShot;
    private float velocity;
    private float velocityRecessionFactor;
    private float velocityDeflectionFactor;
    private float radiansDeflection;
    private float recoilRadians;

    private long timeNextShot = 0;

    public Weapon(
            int fireRate,
            int bulletsPerShot,
            float velocity,
            float velocityRecessionFactor,
            float velocityDeflectionFactor,
            float radiansDeflection,
            float recoilRadians
    ) {
        super(0, 0, 0, texture);

        this.fireRate = fireRate;
        this.bulletsPerShot = bulletsPerShot;
        this.velocity = velocity;
        this.velocityRecessionFactor = velocityRecessionFactor;
        this.velocityDeflectionFactor = velocityDeflectionFactor;
        this.radiansDeflection = radiansDeflection;
        this.recoilRadians = recoilRadians;
    }

    public void update() {}

    public void makeShotBy(Actor shooter) {
        if (System.currentTimeMillis() < timeNextShot) {
            return;
        }

        float push = UtilsMath.randomizeFlexibly(recoilRadians, recoilRadians * 0.25f);
        shooter.push(UtilsMath.random.nextBoolean() ? push : -push);

        audioSource.play(sound);

        float muzzleLength = texture.getCenterX();
        float bulletX = getX() + muzzleLength * (float) Math.cos(getRadians());
        float bulletY = getY() + muzzleLength * (float) Math.sin(getRadians());

        for (int bullet = 0; bullet < bulletsPerShot; bullet++) {
            makeBullet(shooter, bulletX, bulletY);
        }

        timeNextShot = System.currentTimeMillis() + fireRate;
    }

    private void makeBullet(Actor shooter, float x, float y) {
        float radians = UtilsMath.randomizeFlexibly(getRadians(), radiansDeflection);
        float velocity = UtilsMath.randomizeFlexibly(
                this.velocity,
                this.velocity * velocityDeflectionFactor
        );
        Bullet bullet = new Bullet(
                x,
                y,
                radians,
                velocity,
                velocityRecessionFactor,
                shooter
        );
        World.bullets.add(bullet);
    }

    /* Setters */

    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        audioSource.setPosition(getX(), getY());
    }

}
