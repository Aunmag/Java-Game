package aunmag.shooter.client;

import aunmag.nightingale.Configs;

public class Launcher {

    public static void main(String[] args) {
        Configs.setFullscreen(true);
        Configs.setSamplesLoadingEnabled(false); // TODO: This is temporary. Remove it
        new Game().run();
    }

}
