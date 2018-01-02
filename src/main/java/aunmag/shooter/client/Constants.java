package aunmag.shooter.client;

import aunmag.nightingale.data.DataEngine;

import java.io.InputStream;
import java.util.Properties;

public class Constants {

    public static final String TITLE = "A Zombie Shooter Game";
    public static final String DEVELOPER = "Aunmag";
    public static final String VERSION;

    static {
        String filename = "/a-zombie-shooter-game.properties";
        String version = "X.X.X";
        String versionEngineRequired = "X.X.X";
        String versionEngineActual = DataEngine.VERSION;

        try {
            InputStream inputStream = Constants.class.getResourceAsStream(filename);
            Properties properties = new Properties();
            properties.load(inputStream);
            version = properties.getProperty("version");
            versionEngineRequired = properties.getProperty("versionEngine");
            inputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        VERSION = version;

        if (!versionEngineRequired.equals(versionEngineActual)) {
            String message = String.format(
                    "Different engine version found. Required %s got %s instead.",
                    versionEngineRequired,
                    versionEngineActual
            );
            System.err.println(message);
        }
    }

}
