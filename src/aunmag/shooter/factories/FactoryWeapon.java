package aunmag.shooter.factories;

import aunmag.shooter.weapon.Weapon;
import aunmag.shooter.weapon.Magazine;
import aunmag.shooter.weapon.Striker;
import aunmag.shooter.weapon.Trigger;

public class FactoryWeapon {

    public static Weapon mp27() {
        return new Weapon(
                16,
                136,
                6,
                0.03f,
                0.06f,
                0.06f,
                new Magazine(false, 2, 250),
                new Striker(100),
                new Trigger(false)
        );
    }

}
