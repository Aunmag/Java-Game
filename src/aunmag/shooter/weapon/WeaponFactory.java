package aunmag.shooter.weapon;

public class WeaponFactory {

    public static Weapon mp27() {
        return new Weapon(
                WeaponType.mp27,
                new Magazine(CartridgeType.shot, false, 2, 250),
                new Striker(100),
                new Trigger(false)
        );
    }

}
