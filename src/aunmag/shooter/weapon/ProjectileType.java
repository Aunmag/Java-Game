package aunmag.shooter.weapon;

import org.joml.Vector3f;
import org.joml.Vector3fc;

public class ProjectileType {

    public static final Vector3fc colorDefault = new Vector3f(1.0f, 0.8f, 0.2f);

    public final float weight;
    public final float velocityRecessionFactor;
    public final float size;
    public final Vector3fc color;

    private ProjectileType(
            float weight,
            float velocityRecessionFactor,
            float size,
            Vector3fc color
    ) {
        this.weight = weight;
        this.velocityRecessionFactor = velocityRecessionFactor;
        this.size = size;
        this.color = color;
    }

    /* Types */

    public static final ProjectileType LASER = new ProjectileType(
            1f,
            0f,
            0.05f,
            colorDefault
    );

    public static final ProjectileType _9x18mm_Makarov = new ProjectileType(
            6.1f,
            6f,
            0.08f,
            colorDefault
    );

    public static final ProjectileType _12_76_Magnum = new ProjectileType(
            48f / 16f,
            4f,
            0.02f,
            colorDefault
    );

    public static final ProjectileType _5_45x39mm = new ProjectileType(
            3.4f,
            6f,
            0.08f,
            colorDefault
    );

    public static final ProjectileType _7_62x54mmR = new ProjectileType(
            9.6f,
            4.5f,
            0.08f,
            colorDefault
    );

}
