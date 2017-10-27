package aunmag.shooter.weapon;

import org.joml.Vector3f;
import org.joml.Vector3fc;

public class ProjectileType {

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

    public static final ProjectileType _12_76_MAGNUM = new ProjectileType(
            48f / 16f,
            4f,
            0.02f,
            new Vector3f(1.0f, 0.8f, 0.2f)
    );

}
