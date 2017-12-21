package aunmag.shooter.weapon;

import org.joml.Vector4f;
import org.joml.Vector4fc;

public class CartridgeType {

    public static final Vector4fc color = new Vector4f(1.0f, 0.8f, 0.2f, 1.0f);

    public final int shot;
    public final float weight;
    public final float velocityRecessionFactor;
    public final float size;

    private CartridgeType(
            int shot,
            float weight,
            float velocityRecessionFactor,
            float size
    ) {
        this.shot = shot;
        this.weight = weight / (float) shot;
        this.velocityRecessionFactor = velocityRecessionFactor;
        this.size = size;
    }

    /* Types */

    public static final CartridgeType LASER = new CartridgeType(
            1,
            1f,
            0f,
            0.05f
    );

    public static final CartridgeType _9x18mm_Makarov = new CartridgeType(
            1,
            6.1f,
            6f,
            0.08f
    );

    public static final CartridgeType _12_76_Magnum = new CartridgeType(
            16,
            48f,
            4f,
            0.02f
    );

    public static final CartridgeType _5_45x39mm = new CartridgeType(
            1,
            3.4f,
            6f,
            0.08f
    );

    public static final CartridgeType _7_62x54mmR = new CartridgeType(
            1,
            9.6f,
            4.5f,
            0.08f
    );

}
