package aunmag.shooter.weapon;

import aunmag.nightingale.audio.AudioSource;
import aunmag.shooter.world.World;
import aunmag.nightingale.basics.BaseSprite;
import aunmag.nightingale.utilities.UtilsMath;

public class Weapon extends BaseSprite {

    public final WeaponType type;
    public final Magazine magazine;
    public final Striker striker;
    public final Trigger trigger;
    private AudioSource audioSource;

    public Weapon(
            WeaponType type,
            Magazine magazine,
            Striker striker,
            Trigger trigger
    ) {
        super(0, 0, 0, type.texture);
        this.type = type;
        this.magazine = magazine;
        this.striker = striker;
        this.trigger = trigger;

        audioSource = new AudioSource();
        audioSource.setSample(type.sample);
    }

    public void update() {
        magazine.update();

        if (trigger.isFiring() && striker.isCocked() && magazine.takeNextCartridge()) {
            makeShot();
        }
    }

    private void makeShot() {
        float push = UtilsMath.randomizeFlexibly(type.recoilRadians, type.recoilRadians * 0.25f);
        trigger.getShooter().push(UtilsMath.random.nextBoolean() ? push : -push);

        audioSource.play();

        float muzzleLength = type.texture.getCenterX();
        float bulletX = getX() + muzzleLength * (float) Math.cos(getRadians());
        float bulletY = getY() + muzzleLength * (float) Math.sin(getRadians());

        for (int bullet = 0; bullet < magazine.cartridgeType.projectilesQuantity; bullet++) {
            makeBullet(bulletX, bulletY);
        }
    }

    private void makeBullet(float x, float y) {
        float radians = UtilsMath.randomizeFlexibly(getRadians(), type.radiansDeflection);
        float velocity = UtilsMath.randomizeFlexibly(
                type.velocity,
                type.velocityDeflection
        );
        Projectile projectile = new Projectile(
                magazine.cartridgeType.projectile,
                x,
                y,
                radians,
                velocity,
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
