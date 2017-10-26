package aunmag.shooter.weapon;

import aunmag.nightingale.audio.AudioSource;
import aunmag.nightingale.utilities.UtilsAudio;
import aunmag.shooter.world.World;
import aunmag.nightingale.basics.BaseSprite;
import aunmag.nightingale.structures.Texture;
import aunmag.nightingale.utilities.UtilsMath;

public class Weapon extends BaseSprite {

    private static final Texture texture = Texture.getOrCreateAsSprite("images/weapons/mp_27");

    public final float velocity;
    public final float velocityDeflectionFactor;
    public final float radiansDeflection;
    public final float recoilRadians;
    public final Magazine magazine;
    public final Striker striker;
    public final Trigger trigger;
    private AudioSource audioSource;

    public Weapon(
            float velocity,
            float velocityDeflectionFactor,
            float radiansDeflection,
            float recoilRadians,
            Magazine magazine,
            Striker striker,
            Trigger trigger
    ) {
        super(0, 0, 0, texture);
        this.velocity = velocity;
        this.velocityDeflectionFactor = velocityDeflectionFactor;
        this.radiansDeflection = radiansDeflection;
        this.recoilRadians = recoilRadians;
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
        float push = UtilsMath.randomizeFlexibly(recoilRadians, recoilRadians * 0.25f);
        trigger.getShooter().push(UtilsMath.random.nextBoolean() ? push : -push);

        audioSource.play();

        float muzzleLength = texture.getCenterX();
        float bulletX = getX() + muzzleLength * (float) Math.cos(getRadians());
        float bulletY = getY() + muzzleLength * (float) Math.sin(getRadians());

        for (int bullet = 0; bullet < magazine.cartridgeType.projectilesQuantity; bullet++) {
            makeBullet(bulletX, bulletY);
        }
    }

    private void makeBullet(float x, float y) {
        float radians = UtilsMath.randomizeFlexibly(getRadians(), radiansDeflection);
        float velocity = UtilsMath.randomizeFlexibly(
                this.velocity,
                this.velocity * velocityDeflectionFactor
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
