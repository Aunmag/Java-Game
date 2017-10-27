package aunmag.shooter.weapon;

public class CartridgeType {

    public final ProjectileType projectile;
    public final int projectilesQuantity;

    private CartridgeType(ProjectileType projectile, int projectilesQuantity) {
        this.projectile = projectile;
        this.projectilesQuantity = projectilesQuantity;
    }

    /* Types */

    public static final CartridgeType LASER = new CartridgeType(
            ProjectileType.LASER,
            1
    );

    public static final CartridgeType _12_76_MAGNUM = new CartridgeType(
            ProjectileType._12_76_MAGNUM,
            16
    );

}
