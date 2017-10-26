package aunmag.shooter.factories;

import aunmag.shooter.weapon.Weapon;
import aunmag.shooter.weapon.WeaponMagazine;
import aunmag.shooter.weapon.WeaponStriker;
import aunmag.shooter.weapon.WeaponTrigger;

public class FactoryWeapon {

    public static Weapon mp27() {
        return new Weapon(
                16,
                136,
                6,
                0.03f,
                0.06f,
                0.06f,
                new WeaponMagazine(false, 2, 250),
                new WeaponStriker(100),
                new WeaponTrigger(false)
        );
    }

}
