package modes;

// Created by Aunmag on 21.11.2016.

import ai.AI;
import client.Client;
import sprites.Actor;

import java.util.Random;

public class TestAI extends ModeAbstract {

    private long tLastSpawn = Client.getT();
    private long tSpawn = 800;

    private Random random = new Random();

    private void spawn() {

        int spawnDistance = 600;

        double direction = Math.toRadians(random.nextInt(360));
        double spawnX = Client.getPlayerX() - spawnDistance * Math.cos(direction);
        double spawnY = Client.getPlayerY() - spawnDistance * Math.sin(direction);

        Actor spawnedZombie = new Actor(spawnX, spawnY, 0, "actors/zombie.png");
        Actor.allActors.add(spawnedZombie);
        AI.allAIs.add(new AI(spawnedZombie));

    }

    // Updaters:

    public void tick() {

        if (Actor.allActors.size() >= 28) {
            return;
        }

        if (Client.getT() - tLastSpawn >= tSpawn) {
            tLastSpawn = Client.getT();
            spawn();
        }

    }

    public void render() {}

}
