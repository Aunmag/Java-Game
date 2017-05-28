package modes;

// Created by Aunmag on 23.10.2016.

import ai.AI;
import client.Client;
import client.Constants;
import managers.MathManager;
import sprites.Actor;

public class Encircling extends ModeAbstract {

    private long timeLastSpawn;
    private long timeSpawn = 2_000;
    private final long timeSpawnMin = 100;
    private final long timeSpawnDecrease = 20;
    private int zombiesSpawned = 0;
    private int zombiesKilled = 0;
    private final int spawnDistance = 1000;

    private void spawn() {

        float direction = MathManager.randomizeBetween(0, (float) Constants.PI_2_0);
        float spawnX = (float) (Client.getPlayer().getX() - spawnDistance * Math.cos(direction));
        float spawnY = (float) (Client.getPlayer().getY() - spawnDistance * Math.sin(direction));

        Actor spawnedZombie = new Actor(spawnX, spawnY, 0, "zombie");
        Actor.all.add(spawnedZombie);
        AI.all.add(new AI(spawnedZombie));

    }

    // Updaters:

    public void tick() {

        // Confine actor:



        int boundary = 128 * 8;

        Actor player = Client.getPlayer();
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

        // Tick:

        int newZombiesNumber = Actor.all.size() - 1;

        if (zombiesSpawned > newZombiesNumber) {
            zombiesKilled++;
            Client.getGameMenu().getMenuDeath().setMessage("You have killed " + zombiesKilled +" zombies.");
            if (timeSpawn - timeSpawnDecrease > timeSpawnMin) {
                timeSpawn -= timeSpawnDecrease;
            }
            Actor.velocityForwardZombie += 0.005;
        }

        zombiesSpawned = newZombiesNumber;

        if (Actor.all.size() >= 64) {
            return;
        }

        if (Client.getT() - timeLastSpawn >= timeSpawn) {
            timeLastSpawn = Client.getT();
            if (timeSpawn < timeSpawnMin) {
                timeSpawn = timeSpawnMin;
            }
            spawn();
        }

    }

    public void render() {

//        Client.getHud().setColor(Color.WHITE);
//        Client.getHud().drawString("Zombies killed: " + zombiesKilled, 20, 70);
//        Client.getHud().drawString("Player health: " + (int) (Actor.all.get(0).getHealth() * 100), 20, 90);

    }

}
