package aunmag.shooter.weapon;

public class WeaponType {

    public final float velocity;
    public final float velocityDeflectionFactor;
    public final float radiansDeflection;
    public final float recoilRadians;

    private WeaponType(
            float velocity,
            float velocityDeflectionFactor,
            float radiansDeflection,
            float recoilRadians
    ) {
        this.velocity = velocity;
        this.velocityDeflectionFactor = velocityDeflectionFactor;
        this.radiansDeflection = radiansDeflection;
        this.recoilRadians = recoilRadians;
    }

    /* Types */

    public static final WeaponType mp27 = new WeaponType(
                136,
                0.03f,
                0.06f,
                0.06f
    );

}
