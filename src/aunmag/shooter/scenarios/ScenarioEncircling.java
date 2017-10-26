package aunmag.shooter.scenarios;

import aunmag.nightingale.Application;
import aunmag.nightingale.audio.AudioSource;
import aunmag.nightingale.font.Font;
import aunmag.nightingale.utilities.TimerDone;
import aunmag.nightingale.utilities.UtilsAudio;
import aunmag.shooter.ai.Ai;
import aunmag.shooter.client.Game;
import aunmag.shooter.factories.FactoryActor;
import aunmag.nightingale.utilities.TimerNext;
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

    private static final AudioSource sound;
    private TimerNext timeSpawn = new TimerNext(500);

    private int wave = 0;
    private int waveFinal = 8;
    private final int zombiesQuantityInitial = 32;
    private int zombiesQuantityToSpawn = zombiesQuantityInitial;
    private final float zombiesVelocityIncrease = 0.00156f;
    private float zombiesSpawnDirection = 0f;

    private TimerDone timeNotification = new TimerDone(5_000);
    private GuiLabel notificationWave = null;
    private GuiLabel notificationKills = null;

    static {
        sound = UtilsAudio.getOrCreateSoundOgg("sounds/music/death");
        sound.setVolume(0.6f);
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
        int boundary = 4 * 8;
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
        zombiesSpawnDirection = UtilsMath.randomizeBetween(0, (float) UtilsMath.PIx2);
        zombiesQuantityToSpawn = zombiesQuantityInitial * wave;
        createNotifications();
    }

    private void spawnZombie() {
        float done = wave / (float) waveFinal;
        float directionSpread = (float) UtilsMath.PIx2 * (done * done);
        float direction = UtilsMath.randomizeBetween(0, directionSpread);
        direction += UtilsMath.correctRadians(direction + zombiesSpawnDirection);

        float distance = Application.getCamera().getDistanceView() + 0.625f;
        float x = Actor.getPlayer().getX() - distance * (float) Math.cos(direction);
        float y = Actor.getPlayer().getY() - distance * (float) Math.sin(direction);

        Actor zombie = FactoryActor.zombie();
        zombie.setPosition(x, y);
        zombie.setRadians(-direction);
        zombie.setVelocity(zombie.getVelocity() + zombiesVelocityIncrease * (wave - 1));
        World.actors.add(zombie);
        World.ais.add(new Ai(zombie));

        zombiesQuantityToSpawn--;
    }

    private void createNotifications() {
        removeNotifications();

        String messageWave = String.format("Wave %s/%s", wave, waveFinal);
        notificationWave = new GuiLabel(5, 4, 2, 1, messageWave);

        String messageKills = String.format("Kill %s zombies", zombiesQuantityToSpawn);
        notificationKills = new GuiLabel(5, 5, 2, 1, messageKills, Font.fontDefault, 1);

        timeNotification.setTimeInitial(System.currentTimeMillis());
    }

    private int countAliveZombies() {
        return World.actors.size() - 1;
    }

    public void render() {
        if (notificationWave == null || notificationKills == null) {
            return;
        }

        if (!timeNotification.calculateIsDone(System.currentTimeMillis())) {
            notificationWave.render();
            notificationKills.render();
        } else {
            removeNotifications();
        }
    }

    public void remove() {
        removeNotifications();
    }

    private void removeNotifications() {
        if (notificationWave != null) {
            notificationWave.delete();
        }

        if (notificationKills != null) {
            notificationKills.delete();
        }

        notificationWave = null;
        notificationKills = null;
    }

    private void gameOver(boolean isVictory) {
        createGameOverPage(isVictory);
        Game.deleteWorld();

        if (!isVictory) {
            sound.play();
        }
    }

    private void createGameOverPage(boolean isVictory) {
        int kills = Actor.getPlayer().getKills();
        int wavesSurvived = isVictory ? wave : wave - 1;
        String title = isVictory ? "Well done!" : "You have died";
        String score = String.format(
                "You killed %s zombies and survived %s/%s waves.",
                kills,
                wavesSurvived,
                waveFinal
        );

        GuiLabel[] labels = new GuiLabel[] {
                new GuiLabel(4, 3, 4, 1, title),
                new GuiLabel(4, 4, 4, 1, score, Font.fontDefault, 1),
        };

        GuiButton[] buttons = new GuiButton[] {
                new GuiButtonBack(4, 9, 4, 1, "Back to main menu"),
        };

        Texture wallpaper = Texture.getOrCreate(
                isVictory ? "images/wallpapers/victory" : "images/wallpapers/death",
                true,
                false
        );
        wallpaper.scaleAsWallpaper();

        new GuiPage(labels, buttons, wallpaper).open();
        Game.setPause(true);
    }

    /* Getters */

    public boolean isRemoved() {
        return false;
    }

}
