package aunmag.shooter.weapon;

public class WeaponFactory {

    private static final int SEMI_AUTO_SHOTS_PER_MINUTE = 400;

    public static Weapon laserGun() {
        return new Weapon(
                WeaponType.laserGun,
                new Magazine(CartridgeType.LASER, true, 0, 0),
                new Striker(1200),
                new Trigger(true)
        );
    }

    public static Weapon makarovPistol() {
        return new Weapon(
                WeaponType.makarovPistol,
                new Magazine(CartridgeType._9x18mm_Makarov, true, 8, 2f),
                new Striker(SEMI_AUTO_SHOTS_PER_MINUTE),
                new Trigger(false)
        );
    }

    public static Weapon mp27() {
        return new Weapon(
                WeaponType.mp27,
                new Magazine(CartridgeType._12_76_Magnum, false, 2, 0.25f),
                new Striker(SEMI_AUTO_SHOTS_PER_MINUTE),
                new Trigger(false)
        );
    }

    public static Weapon aks74u() {
        return new Weapon(
                WeaponType.aks74u,
                new Magazine(CartridgeType._5_45x39mm, true, 30, 2f),
                new Striker(675),
                new Trigger(true)
        );
    }

    public static Weapon pecheneg() {
        return new Weapon(
                WeaponType.pecheneg,
                new Magazine(CartridgeType._7_62x54mmR, true, 200, 8f),
                new Striker(650),
                new Trigger(true)
        );
    }

    public static Weapon saiga12k() {
        return new Weapon(
                WeaponType.saiga12k,
                new Magazine(CartridgeType._12_76_Magnum, true, 8, 2f),
                new Striker(SEMI_AUTO_SHOTS_PER_MINUTE),
                new Trigger(false)
        );
    }

}
