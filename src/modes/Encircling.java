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
        float spawnX = (float) (Client.getPlayer().x - spawnDistance * Math.cos(direction));
        float spawnY = (float) (Client.getPlayer().y - spawnDistance * Math.sin(direction));

        Actor spawnedZombie = new Actor(spawnX, spawnY, 0, "actors/zombie.png");
        Actor.allActors.add(spawnedZombie);
        AI.allAIs.add(new AI(spawnedZombie));

    }

    // Updaters:

    public void tick() {

        // Confine actor:

        int boundary = 128 * 8;
        Actor player = Client.getPlayer();
        if (-boundary > player.x) {
            player.x = -boundary;
        } else if (player.x > boundary) {
            player.x = boundary;
        }
        if (-boundary > player.y) {
            player.y = -boundary;
        } else if (player.y > boundary) {
            player.y = boundary;
        }

        // Tick:

        int newZombiesNumber = Actor.allActors.size() - 1;

        if (zombiesSpawned > newZombiesNumber) {
            zombiesKilled++;
            Client.getGameMenu().getMenuDeath().setMessage("You have killed " + zombiesKilled +" zombies.");
            if (timeSpawn - timeSpawnDecrease > timeSpawnMin) {
                timeSpawn -= timeSpawnDecrease;
            }
            Actor.vZombieForward += 0.005;
        }

        zombiesSpawned = newZombiesNumber;

        if (Actor.allActors.size() >= 64) {
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
//        Client.getHud().drawString("Player health: " + (int) (Actor.allActors.get(0).getHealth() * 100), 20, 90);

    }

}
