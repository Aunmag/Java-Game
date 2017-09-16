package scenarios;

import ai.AI;
import client.Application;
import gui.menus.MenuManager;
import nightingale.utilities.UtilsMath;
import sprites.Actor;

/**
 * Created by Aunmag on 2016.11.23.
 */

public class ScenarioEncircling extends Scenario {

    private long timeSpawnNext;
    private int timeSpawnStep = 2_000;
    private final int timeSpawnStepMin = 100;
    private final int timeSpawnDecrease = 20;

    private final int spawnDistance = 1000;

    private int actorsSpawnedLimit = 64;
    private int zombiesKilled = 0;
    private float zombiesVelocityAcceleration = 0.005f;

    public void update() {
        confinePlayerPosition();
        updateZombiesKilled();

        if (Actor.all.size() < actorsSpawnedLimit && Application.getTimeCurrent() >= timeSpawnNext) {
            timeSpawnNext = Application.getTimeCurrent() + timeSpawnStep;
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

        String gameOverMessage = String.format("You have killed %s zombies.", zombiesKilled);
        MenuManager.getMenuGameOver().setMessage(gameOverMessage);

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

}
