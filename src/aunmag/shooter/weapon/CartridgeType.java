package aunmag.shooter.weapon;

public class CartridgeType {

    public final ProjectileType projectile;
    public final int projectilesQuantity;

    private CartridgeType(ProjectileType projectile, int projectilesQuantity) {
        this.projectile = projectile;
        this.projectilesQuantity = projectilesQuantity;
    }

    /* Types */

    public static final CartridgeType shot = new CartridgeType(
            ProjectileType.small,
            16
    );

}
