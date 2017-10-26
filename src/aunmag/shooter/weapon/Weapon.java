package aunmag.shooter.weapon;

import aunmag.nightingale.audio.AudioSource;
import aunmag.nightingale.utilities.UtilsAudio;
import aunmag.shooter.world.World;
import aunmag.nightingale.basics.BaseSprite;
import aunmag.nightingale.structures.Texture;
import aunmag.nightingale.utilities.UtilsMath;

public class Weapon extends BaseSprite {

    private static final Texture texture = Texture.getOrCreateAsSprite("images/weapons/mp_27");

    private AudioSource audioSource;
    public final WeaponType type;
    public final Magazine magazine;
    public final Striker striker;
    public final Trigger trigger;

    public Weapon(
            WeaponType type,
            Magazine magazine,
            Striker striker,
            Trigger trigger
    ) {
        super(0, 0, 0, texture);
        this.type = type;
        this.magazine = magazine;
        this.striker = striker;
        this.trigger = trigger;
        audioSource = UtilsAudio.getOrCreateSoundOgg("sounds/weapons/shot_mp_27");
    }

    public void update() {
        magazine.update();

        if (trigger.isFiring() && striker.isCocked() && magazine.takeNextCartridge()) {
            makeShot();
        }
    }

    private void makeShot() {
        float push = UtilsMath.randomizeFlexibly(
                type.recoilRadians,
                type.recoilRadians * 0.25f
        );
        trigger.getShooter().push(UtilsMath.random.nextBoolean() ? push : -push);

        audioSource.play();

        float muzzleLength = texture.getCenterX();
        float bulletX = getX() + muzzleLength * (float) Math.cos(getRadians());
        float bulletY = getY() + muzzleLength * (float) Math.sin(getRadians());

        float bulletSize = type.bulletsPerShot / 4f;
        if (bulletSize < 1) {
            bulletSize = 1;
        }
        bulletSize = 1 / bulletSize;

        for (int bullet = 0; bullet < type.bulletsPerShot; bullet++) {
            makeBullet(bulletX, bulletY, bulletSize);
        }
    }

    private void makeBullet(float x, float y, float size) {
        float radians = UtilsMath.randomizeFlexibly(getRadians(), type.radiansDeflection);
        float velocity = UtilsMath.randomizeFlexibly(
                type.velocity,
                type.velocity * type.velocityDeflectionFactor
        );
        Projectile projectile = new Projectile(
                x,
                y,
                radians,
                velocity,
                type.velocityRecessionFactor,
                size,
                trigger.getShooter()
        );
        World.projectiles.add(projectile);
    }

    /* Setters */

    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        audioSource.setPosition(getX(), getY());
    }

}
