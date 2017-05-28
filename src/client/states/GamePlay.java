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

public class GamePlay {

    private static ModeAbstract mode;

    private static SoundManager ambiance;
    private static SoundManager atmosphere;

    public static void initialize() {

        if (Client.isGameStarted()) {
            terminate();
        }

        // Create player:
        Actor player = new Actor(0, 0, 0, "human");
        Client.setPlayer(player);
        Actor.all.add(player);
        Weapon.all.add(new Weapon(player));

        // Create level:
        int groundNumber = 48; // 48
        int groundSizeBlock = 128;
        int groundSize = groundSizeBlock * groundNumber;
        int groundStart = groundNumber / 2 * groundSizeBlock - groundSizeBlock / 2;
        for (int x = -groundStart; x < groundSize - groundStart; x += groundSizeBlock) {

            for (int y = -groundStart; y < groundSize - groundStart; y += groundSizeBlock) {

                Object.allGround.add(new Object("ground/grass", false, x, y, 0));

                int confine = 960 + 8;
                int zone = 960;

                if (y == -zone && x < confine && -confine < x) {
                    Object.allDecoration.add(new Object("ground/bluff_0", false, x, y - 64, 0));
                } else if (y == zone && x < confine && -confine < x) {
                    Object.allDecoration.add(new Object("ground/bluff_180", false, x, y + 64, 0));
                }

                if (x == -zone && y < confine && -confine < y) {
                    Object.allDecoration.add(new Object("ground/bluff_270", false, x - 64, y, 0));
                } else if (x == zone && y < confine && -confine < y) {
                    Object.allDecoration.add(new Object("ground/bluff_90", false, x + 64, y, 0));
                }

                if (y == -zone && x == -zone) {
                    Object.allDecoration.add(new Object("ground/bluff_a270", false, x - 64, y - 64, 0));
                } else if (y == -zone && x == zone) {
                    Object.allDecoration.add(new Object("ground/bluff_a0", false, x + 64, y - 64, 0));
                } else if (y == zone && x == zone) {
                    Object.allDecoration.add(new Object("ground/bluff_a90", false, x + 64, y + 64, 0));
                } else if (y == zone && x == -zone) {
                    Object.allDecoration.add(new Object("ground/bluff_a180", false, x - 64, y + 64, 0));
                }

            }
        }

        {
            int treesQuantity = (groundNumber * groundNumber) / 2;
            int treesSpreading = (groundNumber * groundSizeBlock) / 2;
            PutTrees.put(treesQuantity, treesSpreading);
        }

        // Game mode:
//        mode = new TestAI();
        mode = new Encircling();
//        mode = new Benchmark();
        Client.setIsGameStarted(true);

        ambiance = new SoundManager("/sounds/ambiance/birds.wav");
        ambiance.setVolume(-8);
        atmosphere = new SoundManager("/sounds/music/gameplay_atmosphere.wav");
        atmosphere.setVolume(-24);

        Actor.velocityForwardZombie = 0.63f;

    }

    public static void terminate() {

        // Clear all data:
        AI.all.clear();
        Actor.all.clear();
        Weapon.all.clear();
        Object.allGround.clear();
        Object.allDecoration.clear();
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

    public static void tick() {
        if (!Client.getPlayer().getIsAlive()) {
            Client.getGameMenu().activeMenuDeath();
            return;
        }

        if (mode != null) {
            mode.tick();
        }

        for (AI ai: AI.all) {
            ai.tick();
        }

        for (Actor actor: Actor.all) {
            actor.update();
        }

        for (Weapon weapon: Weapon.all) {
            weapon.update();
        }

        for (Bullet bullet: Bullet.all) {
            bullet.update();
        }
    }

    public static void render() {
        for (Object object: Object.allGround) {
            object.render();
        }

        for (Object decoration: Object.allDecoration) {
            decoration.render();
        }

        for (Weapon weapon: Weapon.all) {
            weapon.render();
        }

        for (Actor actor: Actor.all) {
            actor.render();
        }

        for (Bullet bullet: Bullet.all) {
            bullet.render();
        }

        for (Object air: Object.allAir) {
            air.render();
        }

        Blackout.render();

        if (mode != null) {
            mode.render();
        }

        Hud.render();
    }

    public static void deleteInvalids() {
        Bullet.all.removeAll(Bullet.invalids);
        Bullet.invalids.clear();

        Weapon.all.removeAll(Weapon.invalids);
        Weapon.invalids.clear();

        Actor.all.removeAll(Actor.invalids);
        Actor.invalids.clear();

        AI.all.removeAll(AI.invalids);
        AI.invalids.clear();
    }

    // Getters:

    public static SoundManager getAmbiance() {

        return ambiance;

    }

    public static SoundManager getAtmosphere() {

        return atmosphere;

    }

}
