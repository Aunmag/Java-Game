package aunmag.shooter.client;

import aunmag.nightingale.Configs;

public class Launcher {

    public static void main(String[] args) {
        Configs.setFullscreen(true);
        new Game().run();
    }

}
