package aunmag.shooter.factories;

import aunmag.shooter.sprites.Weapon;

public class FactoryWeapon {

    public static Weapon mp27() {
        return new Weapon(
                700,
                16,
                58,
                69,
                0.03f,
                0.06f,
                0.06f
        );
    }

}
