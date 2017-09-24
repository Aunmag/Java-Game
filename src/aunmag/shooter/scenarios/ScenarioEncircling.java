package aunmag.shooter.scenarios;

import aunmag.nightingale.Application;
import aunmag.shooter.ai.Ai;
import aunmag.shooter.client.Game;
import aunmag.shooter.managers.NextTimer;
import aunmag.shooter.managers.SoundManager;
import aunmag.shooter.sprites.Actor;
import aunmag.shooter.world.World;
import aunmag.nightingale.basics.BaseOperative;
import aunmag.nightingale.gui.GuiButton;
import aunmag.nightingale.gui.GuiButtonBack;
import aunmag.nightingale.gui.GuiLabel;
import aunmag.nightingale.gui.GuiPage;
import aunmag.nightingale.structures.Texture;
import aunmag.nightingale.utilities.UtilsMath;

public class ScenarioEncircling implements BaseOperative {

    private static SoundManager sound = new SoundManager("/sounds/music/death.wav");
    private NextTimer timeSpawn = new NextTimer(250);

    private int wave = 0;
    private int waveFinal = 8;
    private final int zombiesQuantityInitial = 32;
    private int zombiesQuantityToSpawn = zombiesQuantityInitial;
    private final float zombiesVelocityIncrease = 0.05f;

    private NextTimer timeNotification = new NextTimer(5_000);
    private GuiLabel notification = null;

    static {
        sound.setVolume(-4);
    }

    public ScenarioEncircling() {
        startNextWave();
    }

    public void update() {
        if (!Actor.getPlayer().isAlive()) {
            gameOver(false);
            return;
        }

        confinePlayerPosition();

        if (zombiesQuantityToSpawn > 0) {
            timeSpawn.update(System.currentTimeMillis());
            if (timeSpawn.isNow()) {
                spawnZombie();
            }
        } else if (countAliveZombies() == 0) {
            startNextWave();
        }
    }

    private void confinePlayerPosition() {
        int boundary = 128 * 8;
        Actor player = Actor.getPlayer();
        float playerX = player.getX();
        float playerY = player.getY();

        if (-boundary > playerX) {
            player.setX(-boundary);
        } else if (playerX > boundary) {
            player.setX(boundary);
        }

        if (-boundary > playerY) {
            player.setY(-boundary);
        } else if (playerY > boundary) {
            player.setY(boundary);
        }
    }

    private void startNextWave() {
        if (wave == waveFinal) {
            gameOver(true);
            return;
        }

        wave++;
        zombiesQuantityToSpawn = zombiesQuantityInitial * wave;
        Actor.velocityForwardZombie += zombiesVelocityIncrease;

        notification = createNotification();
        timeNotification.update(System.currentTimeMillis());
    }

    private void spawnZombie() {
        float direction = UtilsMath.randomizeBetween(0, (float) UtilsMath.PIx2);
        float distance = Application.getCamera().getDistanceView() + 20;
        float x = Actor.getPlayer().getX() - distance * (float) Math.cos(direction);
        float y = Actor.getPlayer().getY() - distance * (float) Math.sin(direction);

        Actor zombie = new Actor(x, y, -direction, "zombie");
        World.actors.add(zombie);
        World.ais.add(new Ai(zombie));

        zombiesQuantityToSpawn--;
    }

    private GuiLabel createNotification() {
        if (notification != null) {
            notification.delete();
        }

        String message = String.format("Wave %s / %s", wave, waveFinal);
        return new GuiLabel(5, 1, 2, 1, message);
    }

    private int countAliveZombies() {
        return World.actors.size() - 1;
    }

    public void render() {
        if (notification != null) {
            timeNotification.update(System.currentTimeMillis());
            if (!timeNotification.isNow()) {
                notification.render();
            } else {
                notification.delete();
                notification = null;
            }
        }
    }

    public void remove() {
        if (notification != null) {
            notification.delete();
        }
    }

    public void gameOver(boolean isVictory) {
        createGameOverPage(isVictory);
        Game.deleteWorld();

        if (!isVictory) {
            sound.play();
        }
    }

    private void createGameOverPage(boolean isVictory) {
        int wavesSurvived = isVictory ? wave : wave - 1;
        String title = isVictory ? "Well done!" : "You have died";
        String kills = String.format("Zombies killed: %s", Actor.getPlayer().getKills());
        String waves = String.format("Waves survived: %s / %s", wavesSurvived, waveFinal);

        GuiLabel[] labels = new GuiLabel[] {
                new GuiLabel(4, 4, 4, 1, title),
                new GuiLabel(4, 5, 4, 1, kills),
                new GuiLabel(4, 6, 4, 1, waves),
        };

        GuiButton[] buttons = new GuiButton[] {
                new GuiButtonBack(4, 8, 4, 1, "Back to main menu"),
        };

        Texture wallpaper = Texture.getOrCreate(
                isVictory ? "images/wallpapers/exit" : "images/wallpapers/death",
                true,
                false);
        wallpaper.scaleAsWallpaper();

        new GuiPage(labels, buttons, wallpaper).open();
        Game.setPause(true);
    }

    /* Getters */

    public boolean isRemoved() {
        return false;
    }

}
