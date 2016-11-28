package client.states;

// Created by Aunmag on 09.11.2016.

import ai.AI;
import client.graphics.Hud;
import client.graphics.effects.Blackout;
import client.Client;
import modes.*;
import managers.SoundManager;
import scripts.PutTrees;
import sprites.Actor;
import sprites.Bullet;
import sprites.Object;
import sprites.Weapon;

import java.awt.*;

public class GamePlay {

    private static ModeAbstract mode;

    private static SoundManager ambiance;
    private static SoundManager atmosphere;

    public static void initialize() {

        if (Client.isGameStarted()) {
            terminate();
        }

        // Create player:
        Actor player = new Actor(0, 0, 0, "actors/human.png");
        Client.setPlayer(player);
        Actor.allActors.add(player);
        Weapon.allWeapons.add(new Weapon(player));

        // Create level:
        int groundNumber = 48;
        int groundSizeBlock = 128;
        int groundSize = groundSizeBlock * groundNumber;
        int groundStart = groundNumber / 2 * groundSizeBlock - groundSizeBlock / 2;
        for (int x = -groundStart; x < groundSize - groundStart; x += groundSizeBlock) {

            for (int y = -groundStart; y < groundSize - groundStart; y += groundSizeBlock) {

                Object.allGroundObjects.add(new Object("ground/grass", false, x, y, 0));

                int confine = 960 + 8;
                int zone = 960;

                if (y == -zone && x < confine && -confine < x) {
                    Object.allDecorationObjects.add(new Object("ground/bluff_0", false, x, y - 64, 0));
                } else if (y == zone && x < confine && -confine < x) {
                    Object.allDecorationObjects.add(new Object("ground/bluff_180", false, x, y + 64, 0));
                }

                if (x == -zone && y < confine && -confine < y) {
                    Object.allDecorationObjects.add(new Object("ground/bluff_270", false, x - 64, y, 0));
                } else if (x == zone && y < confine && -confine < y) {
                    Object.allDecorationObjects.add(new Object("ground/bluff_90", false, x + 64, y, 0));
                }

                if (y == -zone && x == -zone) {
                    Object.allDecorationObjects.add(new Object("ground/bluff_a270", false, x - 64, y - 64, 0));
                } else if (y == -zone && x == zone) {
                    Object.allDecorationObjects.add(new Object("ground/bluff_a0", false, x + 64, y - 64, 0));
                } else if (y == zone && x == zone) {
                    Object.allDecorationObjects.add(new Object("ground/bluff_a90", false, x + 64, y + 64, 0));
                } else if (y == zone && x == -zone) {
                    Object.allDecorationObjects.add(new Object("ground/bluff_a180", false, x - 64, y + 64, 0));
                }

            }
        }

        PutTrees.put((groundNumber * groundNumber) / 2, groundNumber * groundSizeBlock);

        // Game mode:
//        mode = new TestAI();
        mode = new Encircling();
//        mode = new Benchmark();
        Client.setIsGameStarted(true);

        ambiance = new SoundManager("/sounds/ambiance/birds.wav");
        ambiance.setVolume(-8);
        atmosphere = new SoundManager("/sounds/music/gameplay_atmosphere.wav");
        atmosphere.setVolume(-24);

        Actor.vZombieForward = 0.63;

    }

    public static void terminate() {

        // Clear all data:
        AI.allAIs.clear();
        Actor.allActors.clear();
        Weapon.allWeapons.clear();
        Object.allGroundObjects.clear();
        Object.allDecorationObjects.clear();
        mode = null;

        ambiance.stop();
        atmosphere.stop();

        Client.setIsGameStarted(false);

    }

    public static void activate() {

        Client.setIsCursorVisible(false);
        Client.setIsGamePlay(true);
        Client.setIsGameMenu(false);

        ambiance.loop();
        atmosphere.loop();

    }

    // Updaters:

    public static void tick() {

        if (!Client.getPlayer().isAlive) {
            Client.getGameMenu().activeMenuDeath();
            return;
        }

        // Tick game mode:
        if (mode != null) {
            mode.tick();
        }

        // Tick AIs:
        for (int i = AI.allAIs.size() - 1; i >= 0; i--) {
            AI.allAIs.get(i).tick();
        }

        // Tick actors:
        for (int i = Actor.allActors.size() - 1; i >= 0; i--) {
            Actor.allActors.get(i).tick();
        }

        // Tick weapons:
        for (int i = Weapon.allWeapons.size() - 1; i >= 0; i--) {
            Weapon.allWeapons.get(i).tick();
        }

        // Tick bullets:
        for (int i = Bullet.allBullets.size() - 1; i >= 0; i--) {
            Bullet.allBullets.get(i).tick();
        }

    }

    public static void render() {

        for (Object object: Object.allGroundObjects) {
            object.render();
        }

        for (Object decoration: Object.allDecorationObjects) {
            decoration.render();
        }

        for (Weapon weapon: Weapon.allWeapons) {
            weapon.render();
        }

        for (Actor actor: Actor.allActors) {
            actor.render();
        }

        for (Bullet bullet: Bullet.allBullets) {
            bullet.render();
        }

        for (Object air: Object.allAirObjects) {
            air.render();
        }

        Blackout.render();

        if (mode != null) {
            mode.render();
        }

        Hud.render();

    }

    // Getters:

    public static SoundManager getAmbiance() {

        return ambiance;

    }

    public static SoundManager getAtmosphere() {

        return atmosphere;

    }

}
