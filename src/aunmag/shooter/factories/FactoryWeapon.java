package aunmag.shooter.factories;

import aunmag.shooter.weapon.*;

public class FactoryWeapon {

    public static Weapon mp27() {
        return new Weapon(
                WeaponType.mp27,
                new Magazine(false, 2, 250),
                new Striker(100),
                new Trigger(false)
        );
    }

}
