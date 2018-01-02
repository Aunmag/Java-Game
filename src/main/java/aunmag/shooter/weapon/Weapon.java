package aunmag.shooter.weapon;

import aunmag.nightingale.audio.AudioSource;
import aunmag.nightingale.collision.CollisionEmpty;
import aunmag.nightingale.utilities.UtilsMath;
import aunmag.shooter.world.World;

public class Weapon extends CollisionEmpty {

    public final World world;
    public final WeaponType type;
    public final Magazine magazine;
    public final Striker striker;
    public final Trigger trigger;
    private AudioSource audioSource;

    public Weapon(
            World world,
            WeaponType type,
            Magazine magazine,
            Striker striker,
            Trigger trigger
    ) {
        super(0, 0, 0);
        this.world = world;
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
        trigger.getShooter().push(calculateRandomRecoil());

        audioSource.play();

        float muzzleLength = type.texture.getCenterX();
        float bulletX = getX() + muzzleLength * (float) Math.cos(getRadians());
        float bulletY = getY() + muzzleLength * (float) Math.sin(getRadians());

        for (int bullet = 0; bullet < magazine.cartridgeType.shot; bullet++) {
            makeBullet(bulletX, bulletY);
        }
    }

    private void makeBullet(float x, float y) {
        Projectile projectile = new Projectile(
                world,
                magazine.cartridgeType,
                x,
                y,
                calculateRandomRadians(),
                calculateRandomVelocity(),
                trigger.getShooter()
        );
        world.getProjectiles().add(projectile);
    }

    private float calculateRandomRecoil() {
        float recoil = UtilsMath.randomizeFlexibly(type.recoil, type.recoilDeflection);

        if (UtilsMath.random.nextBoolean()) {
            recoil = -recoil;
        }

        return recoil;
    }

    private float calculateRandomRadians() {
        return UtilsMath.randomizeFlexibly(getRadians(), type.radiansDeflection);
    }

    private float calculateRandomVelocity() {
        return UtilsMath.randomizeFlexibly(type.velocity, type.velocityDeflection);
    }

    /* Setters */

    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        audioSource.setPosition(getX(), getY());
    }

}
