package aunmag.shooter.weapon;

public class WeaponFactory {

    public static Weapon laserGun() {
        return new Weapon(
                5000,
                0f,
                0f,
                0f,
                new Magazine(CartridgeType.LASER, true, 0, 0),
                new Striker(1200),
                new Trigger(true)
        );
    }

    public static Weapon makarovPistol() {
        return new Weapon(
                315,
                0.3f,
                0.05f,
                0.01f,
                new Magazine(CartridgeType._9x18mm_Makarov, true, 8, 2000),
                new Striker(300),
                new Trigger(false)
        );
    }

    public static Weapon mp27() {
        return new Weapon(
                410,
                0.03f,
                0.06f,
                0.06f,
                new Magazine(CartridgeType._12_76_Magnum, false, 2, 250),
                new Striker(600),
                new Trigger(false)
        );
    }

}
