package aunmag.shooter.environment.weapon;

import aunmag.nightingale.audio.AudioSample;
import aunmag.nightingale.audio.AudioSampleType;
import aunmag.nightingale.structures.Texture;
import aunmag.shooter.environment.magazine.MagazineType;
import aunmag.shooter.environment.projectile.ProjectileType;

public class WeaponType {

    private static final int SEMI_AUTO_SHOTS_PER_MINUTE = 400;

    public final String name;
    public final Texture texture;
    public final int sample;
    public final int shotsPerMinute;
    public final float velocity;
    public final float velocityDeflection;
    public final float radiansDeflection;
    public final float recoil;
    public final float recoilDeflection;
    public final boolean isAutomatic;
    public final MagazineType magazine;

    private WeaponType(
            String name,
            String path,
            int shotsPerMinute,
            float velocity,
            float radiansDeflection,
            float recoil,
            boolean isAutomatic,
            MagazineType magazine
    ) {
        path = "weapons/" + path;
        this.name = name;
        this.texture = Texture.getOrCreateAsSprite(path + "/image");
        this.sample = AudioSample.getOrCreate(path + "/shot", AudioSampleType.OGG);
        this.shotsPerMinute = shotsPerMinute;
        this.velocity = velocity;
        this.velocityDeflection = velocity * 0.03f;
        this.radiansDeflection = radiansDeflection;
        this.recoil = recoil;
        this.recoilDeflection = recoil * 0.25f;
        this.isAutomatic = isAutomatic;
        this.magazine = magazine;
    }

    /* Types */

    public static final WeaponType laserGun = new WeaponType(
            "Laser Gun",
            "laser_gun",
            1200,
            5000,
            0f,
            0f,
            true,
            new MagazineType(ProjectileType.LASER, true, 0, 0)
    );

    public static final WeaponType makarovPistol = new WeaponType(
            "Makarov pistol",
            "makarov_pistol",
            SEMI_AUTO_SHOTS_PER_MINUTE,
            315,
            0.05f,
            0.01f,
            false,
            new MagazineType(ProjectileType._9x18mm_Makarov, true, 8, 2f)
    );

    public static final WeaponType mp27 = new WeaponType(
            "MP-27",
            "mp_27",
            SEMI_AUTO_SHOTS_PER_MINUTE,
            410,
            0.06f,
            0.06f,
            false,
            new MagazineType(ProjectileType._12_76_Magnum, false, 2, 0.25f)
    );

    public static final WeaponType aks74u = new WeaponType(
            "AKS-74U",
            "aks_74u",
            675,
            735,
            0.03f,
            0.02f,
            true,
            new MagazineType(ProjectileType._5_45x39mm, true, 30, 2f)
    );

    public static final WeaponType pecheneg = new WeaponType(
            "Pecheneg",
            "pecheneg",
            650,
            825,
            0.02f,
            0.035f,
            true,
            new MagazineType(ProjectileType._7_62x54mmR, true, 200, 8f)
    );

    public static final WeaponType saiga12k = new WeaponType(
            "Saiga-12K",
            "saiga_12k",
            SEMI_AUTO_SHOTS_PER_MINUTE,
            410,
            0.07f,
            0.05f,
            false,
            new MagazineType(ProjectileType._12_76_Magnum, true, 8, 2f)
    );

}
