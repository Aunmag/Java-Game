package aunmag.shooter.weapon;

import aunmag.nightingale.audio.AudioSource;
import aunmag.nightingale.utilities.UtilsAudio;
import aunmag.shooter.sprites.Bullet;
import aunmag.shooter.world.World;
import aunmag.nightingale.basics.BaseSprite;
import aunmag.nightingale.structures.Texture;
import aunmag.nightingale.utilities.UtilsMath;

public class Weapon extends BaseSprite {

    private static final Texture texture = Texture.getOrCreateAsSprite("images/weapons/mp_27");

    private AudioSource audioSource;
    private int bulletsPerShot;
    private float velocity;
    private float velocityRecessionFactor;
    private float velocityDeflectionFactor;
    private float radiansDeflection;
    private float recoilRadians;
    public final WeaponMagazine magazine;
    public final WeaponStriker striker;
    public final WeaponTrigger trigger;

    public Weapon(
            int bulletsPerShot,
            float velocity,
            float velocityRecessionFactor,
            float velocityDeflectionFactor,
            float radiansDeflection,
            float recoilRadians,
            WeaponMagazine magazine,
            WeaponStriker striker,
            WeaponTrigger trigger
    ) {
        super(0, 0, 0, texture);

        this.bulletsPerShot = bulletsPerShot;
        this.velocity = velocity;
        this.velocityRecessionFactor = velocityRecessionFactor;
        this.velocityDeflectionFactor = velocityDeflectionFactor;
        this.radiansDeflection = radiansDeflection;
        this.recoilRadians = recoilRadians;

        audioSource = UtilsAudio.getOrCreateSoundOgg("sounds/weapons/shot_mp_27");

        this.magazine = magazine;
        this.striker = striker;
        this.trigger = trigger;
    }

    public void update() {
        magazine.update();

        if (trigger.isFiring() && striker.isCocked() && magazine.takeNextCartridge()) {
            makeShot();
        }
    }

    private void makeShot() {
        float push = UtilsMath.randomizeFlexibly(recoilRadians, recoilRadians * 0.25f);
        trigger.getShooter().push(UtilsMath.random.nextBoolean() ? push : -push);

        audioSource.play();

        float muzzleLength = texture.getCenterX();
        float bulletX = getX() + muzzleLength * (float) Math.cos(getRadians());
        float bulletY = getY() + muzzleLength * (float) Math.sin(getRadians());

        float bulletSize = bulletsPerShot / 4f;
        if (bulletSize < 1) {
            bulletSize = 1;
        }
        bulletSize = 1 / bulletSize;

        for (int bullet = 0; bullet < bulletsPerShot; bullet++) {
            makeBullet(bulletX, bulletY, bulletSize);
        }
    }

    private void makeBullet(float x, float y, float size) {
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
                size,
                trigger.getShooter()
        );
        World.bullets.add(bullet);
    }

    /* Setters */

    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        audioSource.setPosition(getX(), getY());
    }

}
