package aunmag.shooter.scenarios;

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
    private NextTimer timeSpawn = new NextTimer(1_000);
    private final int spawnDistance = 1000;

    private int actorsSpawnedLimit = 64;
    private int zombiesKilled = 0;
    private float zombiesVelocityAcceleration = 0.005f;

    static {
        sound.setVolume(-4);
    }

    public void update() {
        if (!Actor.getPlayer().isAlive()) {
            gameOver();
            return;
        }

        confinePlayerPosition();
        updateZombiesKilled();

        timeSpawn.update(System.currentTimeMillis());
        if (World.actors.size() < actorsSpawnedLimit && timeSpawn.isNow()) {
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
    }

    private void spawnZombie() {
        float direction = UtilsMath.randomizeBetween(0, (float) UtilsMath.PIx2);
        float x = Actor.getPlayer().getX() - spawnDistance * (float) Math.cos(direction);
        float y = Actor.getPlayer().getY() - spawnDistance * (float) Math.sin(direction);

        Actor zombie = new Actor(x, y, -direction, "zombie");
        World.actors.add(zombie);
        World.ais.add(new Ai(zombie));
    }

    public void render() {}

    public void remove() {}

    public void gameOver() {
        createGameOverPage();
        Game.deleteWorld();
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

        Texture wallpaper = Texture.getOrCreate("images/wallpapers/death", true, false);
        wallpaper.scaleAsWallpaper();

        new GuiPage(labels, buttons, wallpaper).open();
        Game.setPause(true);
    }

    /* Getters */

    public boolean isRemoved() {
        return false;
    }

}
