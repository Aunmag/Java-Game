package game.scenarios;

import game.ai.AI;
import game.client.GamePlay;
import game.managers.SoundManager;
import nightingale.basics.BaseOperative;
import nightingale.gui.GuiButton;
import nightingale.gui.GuiButtonBack;
import nightingale.gui.GuiLabel;
import nightingale.gui.GuiPage;
import nightingale.structures.Texture;
import nightingale.utilities.UtilsMath;
import game.sprites.Actor;

/**
 * Created by Aunmag on 2016.11.23.
 */

public class ScenarioEncircling implements BaseOperative {

    private static SoundManager sound = new SoundManager("/sounds/music/death.wav");
    private long timeSpawnNext;
    private int timeSpawnStep = 2_000;
    private final int timeSpawnStepMin = 100;
    private final int timeSpawnDecrease = 20;

    private final int spawnDistance = 1000;

    private int actorsSpawnedLimit = 64;
    private int zombiesKilled = 0;
    private float zombiesVelocityAcceleration = 0.005f;

    static {
        sound.setVolume(-4);
    }

    public void update() {
        if (!Actor.getPlayer().getIsAlive()) {
            gameOver();
            return;
        }

        confinePlayerPosition();
        updateZombiesKilled();

        if (Actor.all.size() < actorsSpawnedLimit && System.currentTimeMillis() >= timeSpawnNext) {
            timeSpawnNext = System.currentTimeMillis() + timeSpawnStep;
            spawnZombie();
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

    public void updateZombiesKilled() {
        int zombiesKilledNow = Actor.getPlayer().getKills();

        if (zombiesKilled == zombiesKilledNow) {
            return;
        }

        int zombiesKilledDifference = zombiesKilledNow - zombiesKilled;
        Actor.velocityForwardZombie += zombiesVelocityAcceleration * zombiesKilledDifference;
        zombiesKilled = zombiesKilledNow;

        int timeSpawnDecreaseNow = timeSpawnDecrease * zombiesKilledDifference;
        if (timeSpawnStep - timeSpawnDecreaseNow > timeSpawnStepMin) {
            timeSpawnStep -= timeSpawnDecreaseNow;
        }
    }

    private void spawnZombie() {
        float direction = UtilsMath.randomizeBetween(0, (float) UtilsMath.PIx2);
        float x = Actor.getPlayer().getX() - spawnDistance * (float) Math.cos(direction);
        float y = Actor.getPlayer().getY() - spawnDistance * (float) Math.sin(direction);

        Actor zombie = new Actor(x, y, -direction, "zombie");
        Actor.all.add(zombie);
        AI.all.add(new AI(zombie));
    }

    public void render() {}

    public void remove() {}

    public void gameOver() {
        createGameOverPage();
        GamePlay.deleteWorld();
        sound.play();
    }

    private void createGameOverPage() {
        String messageScore = String.format("You have killed %s zombies.", zombiesKilled);

        GuiLabel[] labels = new GuiLabel[] {
                new GuiLabel(4, 4, 4, 1, "You have died"),
                new GuiLabel(4, 5, 4, 1, messageScore)
        };

        GuiButton[] buttons = new GuiButton[] {
                new GuiButtonBack(4, 8, 4, 1, "Back to main menu"),
        };

        Texture wallpaper = Texture.getOrCreate("images/wallpapers/death");
        wallpaper.scaleAsWallpaper();

        new GuiPage(labels, buttons, wallpaper).open();
        GamePlay.setPause(true);
    }

}
