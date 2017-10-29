package aunmag.shooter.weapon;

public class WeaponFactory {

    private static final int SEMI_AUTO_SHOTS_PER_MINUTE = 400;
    private static final float VELOCITY_DEFLECTION_FACTOR = 0.03f;

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
                VELOCITY_DEFLECTION_FACTOR,
                0.05f,
                0.01f,
                new Magazine(CartridgeType._9x18mm_Makarov, true, 8, 2000),
                new Striker(SEMI_AUTO_SHOTS_PER_MINUTE),
                new Trigger(false)
        );
    }

    public static Weapon mp27() {
        return new Weapon(
                410,
                VELOCITY_DEFLECTION_FACTOR,
                0.06f,
                0.06f,
                new Magazine(CartridgeType._12_76_Magnum, false, 2, 250),
                new Striker(SEMI_AUTO_SHOTS_PER_MINUTE),
                new Trigger(false)
        );
    }

    public static Weapon aks74u() {
        return new Weapon(
                735,
                VELOCITY_DEFLECTION_FACTOR,
                0.03f,
                0.02f,
                new Magazine(CartridgeType._5_45x39mm, true, 30, 2000),
                new Striker(675),
                new Trigger(true)
        );
    }

    public static Weapon pecheneg() {
        return new Weapon(
                825,
                VELOCITY_DEFLECTION_FACTOR,
                0.02f,
                0.035f,
                new Magazine(CartridgeType._7_62x54mmR, true, 200, 8000),
                new Striker(650),
                new Trigger(true)
        );
    }

    public static Weapon saiga12k() {
        return new Weapon(
                410,
                VELOCITY_DEFLECTION_FACTOR,
                0.07f,
                0.05f,
                new Magazine(CartridgeType._12_76_Magnum, true, 8, 2000),
                new Striker(SEMI_AUTO_SHOTS_PER_MINUTE),
                new Trigger(false)
        );
    }

}
