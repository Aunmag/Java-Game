package root.modes;

// Created by Aunmag on 23.10.2016.

import root.ai.AI;
import root.scripts.Spawn;
import root.sprites.Actor;

import java.awt.*;
import java.util.Random;

public class Encircling {

    private static long timeCurrent;
    private static long timeLastSpawn;
    private static long timeSpawn = 2_000_000_000L;
    private static final long timeSpawnMin = 100_000_000;
    private static int zombiesSpawned = 0;
    private static int zombiesKilled = 0;

    private static Random random = new Random();
    private static final int spawnDistance = 600;

    public static void confineActor(Actor actor, int boundary) {

        if (-boundary > actor.x) {
            actor.x = -boundary;
        } else if (actor.x > boundary) {
            actor.x = boundary;
        }
        if (-boundary > actor.y) {
            actor.y = -boundary;
        } else if (actor.y > boundary) {
            actor.y = boundary;
        }

    }

    public static void tick(double x, double y) {

        int newZombiesNumber = Actor.allActors.size() - 1;

        if (zombiesSpawned > newZombiesNumber) {
            zombiesKilled++;
            Spawn.zombieVelocityForward += 0.006;
        }

        zombiesSpawned = newZombiesNumber;

        timeCurrent = System.nanoTime();

        if (timeCurrent - timeLastSpawn >= timeSpawn) {
            timeLastSpawn = timeCurrent;
            if (timeSpawn > timeSpawnMin) {
                timeSpawn -= 20_000_000;
//                System.out.println("Spawn time: " + timeSpawn);
            } else if (timeSpawn < timeSpawnMin) {
                timeSpawn = timeSpawnMin;
            }
            if (zombiesSpawned >= 64) {
                return;
            }
            spawn(x, y);
        }

    }

    public static void renderHud(Graphics gHud) {

        gHud.setColor(Color.WHITE);
        gHud.drawString("Player health: " + (int) (Actor.allActors.get(0).getHealth() * 100), 20, 80);
        gHud.drawString("Zombies spawned: " + zombiesSpawned, 20, 100);
        gHud.drawString("Zombies killed: " + zombiesKilled, 20, 120);

    }

    private static void spawn(double x, double y) {

        double direction = Math.toRadians(random.nextInt(360));
        double spawnX = x - spawnDistance * Math.cos(direction);
        double spawnY = y - spawnDistance * Math.sin(direction);

        Actor spawnedZombie = Spawn.zombie(spawnX, spawnY, 0);
        AI.allAIs.add(new AI(spawnedZombie));

    }

}
