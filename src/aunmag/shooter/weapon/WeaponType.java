package aunmag.shooter.weapon;

public class WeaponType {

    public final int bulletsPerShot;
    public final float velocity;
    public final float velocityDeflectionFactor;
    public final float radiansDeflection;
    public final float recoilRadians;

    private WeaponType(
            int bulletsPerShot,
            float velocity,
            float velocityDeflectionFactor,
            float radiansDeflection,
            float recoilRadians
    ) {
        this.bulletsPerShot = bulletsPerShot;
        this.velocity = velocity;
        this.velocityDeflectionFactor = velocityDeflectionFactor;
        this.radiansDeflection = radiansDeflection;
        this.recoilRadians = recoilRadians;
    }

    /* Types */

    public static final WeaponType mp27 = new WeaponType(
                16,
                136,
                0.03f,
                0.06f,
                0.06f
    );

}
