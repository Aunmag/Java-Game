package aunmag.shooter.weapon;

import aunmag.nightingale.audio.AudioSample;
import aunmag.nightingale.audio.AudioSampleType;
import aunmag.nightingale.structures.Texture;

public class WeaponType {

    public final String name;
    public final Texture texture;
    public final int sample;
    public final float velocity;
    public final float velocityDeflection;
    public final float radiansDeflection;
    public final float recoilRadians;

    private WeaponType(
            String name,
            String path,
            float velocity,
            float radiansDeflection,
            float recoilRadians
    ) {
        path = "weapons/" + path;
        this.name = name;
        this.texture = Texture.getOrCreateAsSprite(path + "/image");
        this.sample = AudioSample.getOrCreate(path + "/shot", AudioSampleType.OGG);
        this.velocity = velocity;
        this.velocityDeflection = velocity * 0.03f;
        this.radiansDeflection = radiansDeflection;
        this.recoilRadians = recoilRadians;
    }

    /* Types */

    public static final WeaponType laserGun = new WeaponType(
            "Laser Gun",
            "laser_gun",
            5000,
            0f,
            0f
    );

    public static final WeaponType makarovPistol = new WeaponType(
            "Makarov pistol",
            "makarov_pistol",
            315,
            0.05f,
            0.01f
    );

    public static final WeaponType mp27 = new WeaponType(
            "MP-27",
            "mp_27",
            410,
            0.06f,
            0.06f
    );

    public static final WeaponType aks74u = new WeaponType(
            "AKS-74U",
            "aks_74u",
            735,
            0.03f,
            0.02f
    );

    public static final WeaponType pecheneg = new WeaponType(
            "Pecheneg",
            "pecheneg",
            825,
            0.02f,
            0.035f
    );

    public static final WeaponType saiga12k = new WeaponType(
            "Saiga-12K",
            "saiga_12k",
            410,
            0.07f,
            0.05f
    );

}
