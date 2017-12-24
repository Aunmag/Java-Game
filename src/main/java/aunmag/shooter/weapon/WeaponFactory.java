package aunmag.shooter.weapon;

import aunmag.shooter.world.World;

public class WeaponFactory {

    private static final int SEMI_AUTO_SHOTS_PER_MINUTE = 400;

    public static Weapon laserGun(World world) {
        return new Weapon(
                world,
                WeaponType.laserGun,
                new Magazine(world, CartridgeType.LASER, true, 0, 0),
                new Striker(world, 1200),
                new Trigger(true)
        );
    }

    public static Weapon makarovPistol(World world) {
        return new Weapon(
                world,
                WeaponType.makarovPistol,
                new Magazine(world, CartridgeType._9x18mm_Makarov, true, 8, 2f),
                new Striker(world, SEMI_AUTO_SHOTS_PER_MINUTE),
                new Trigger(false)
        );
    }

    public static Weapon mp27(World world) {
        return new Weapon(
                world,
                WeaponType.mp27,
                new Magazine(world, CartridgeType._12_76_Magnum, false, 2, 0.25f),
                new Striker(world, SEMI_AUTO_SHOTS_PER_MINUTE),
                new Trigger(false)
        );
    }

    public static Weapon aks74u(World world) {
        return new Weapon(
                world,
                WeaponType.aks74u,
                new Magazine(world, CartridgeType._5_45x39mm, true, 30, 2f),
                new Striker(world, 675),
                new Trigger(true)
        );
    }

    public static Weapon pecheneg(World world) {
        return new Weapon(
                world,
                WeaponType.pecheneg,
                new Magazine(world, CartridgeType._7_62x54mmR, true, 200, 8f),
                new Striker(world, 650),
                new Trigger(true)
        );
    }

    public static Weapon saiga12k(World world) {
        return new Weapon(
                world,
                WeaponType.saiga12k,
                new Magazine(world, CartridgeType._12_76_Magnum, true, 8, 2f),
                new Striker(world, SEMI_AUTO_SHOTS_PER_MINUTE),
                new Trigger(false)
        );
    }

}
