package aunmag.shooter.weapon;

public class WeaponFactory {

    public static Weapon mp27() {
        return new Weapon(
                410,
                0.03f,
                0.06f,
                0.06f,
                new Magazine(CartridgeType._12_76_MAGNUM, false, 2, 250),
                new Striker(100),
                new Trigger(false)
        );
    }

}
