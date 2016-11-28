package root.scripts;

// Created by Aunmag on 30.10.2016.

import root.sprites.Actor;

public class Spawn {

    public static double zombieVelocityForward = 0.63;

    public static Actor human(double x, double y, double degrees) {

        Actor actor = new Actor(x, y, degrees, "/actors/humans/human_1.png");

        actor.velocityForward = 1.38;
        actor.velocityAside = actor.velocityForward * 0.6;
        actor.velocityBack = actor.velocityForward * 0.8;
        actor.runningAcceleration = 3.02;
        actor.type = "human";
        actor.hasWeapon = true;

        Actor.allActors.add(actor);

        return actor;

    }

    public static Actor zombie(double x, double y, double degrees) {

        Actor actor = new Actor(x, y, degrees, "/actors/zombies/zombie_2.png");

        actor.velocityForward = zombieVelocityForward;
        actor.velocityAside = actor.velocityForward * 0.6;
        actor.velocityBack = actor.velocityForward * 0.8;
        actor.runningAcceleration = 1.63;
        actor.type = "zombie";
        actor.hasWeapon = false;

        Actor.allActors.add(actor);

        return actor;

    }

}
