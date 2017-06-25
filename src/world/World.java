package world;

import ai.AI;
import client.Camera;
import managers.ImageManager;
import managers.SoundManager;
import sprites.Actor;
import sprites.Bullet;
import sprites.Object;
import sprites.Weapon;
import utilities.UtilsWorld;

/**
 * Created by Aunmag on 2017.06.24.
 */

public class World {

    private static final int groundQuantity = 48;
    private static final int groundBlockSize = 128;
    private static final SoundManager soundAmbiance;
    private static final SoundManager soundAtmosphere;

    static {
        soundAmbiance = new SoundManager("/sounds/ambiance/birds.wav");
        soundAmbiance.setVolume(-8);
        soundAtmosphere = new SoundManager("/sounds/music/gameplay_atmosphere.wav");
        soundAtmosphere.setVolume(-24);
    }

    public World() {
        initializePlayer();
        initializeGround();
        initializeTrees();
        Actor.velocityForwardZombie = 0.63f; // TODO: Get ride off this
    }

    private void initializePlayer() {
        // TODO: World should not know about client's player
        Actor player = new Actor(0, 0, 0, "human");
        Actor.setPlayer(player);
        Actor.all.add(player);
        Weapon.all.add(new Weapon(player));
        Camera.setTarget(player);
    }

    private void initializeGround() {
        // TODO: Improve all:

        ImageManager imageGrass = new ImageManager("objects/ground/grass");
        ImageManager imageBluff0 = new ImageManager("objects/ground/bluff_0");
        ImageManager imageBluff90 = new ImageManager("objects/ground/bluff_90");
        ImageManager imageBluff180 = new ImageManager("objects/ground/bluff_180");
        ImageManager imageBluff270 = new ImageManager("objects/ground/bluff_270");
        ImageManager imageBluffA0 = new ImageManager("objects/ground/bluff_a0");
        ImageManager imageBluffA90 = new ImageManager("objects/ground/bluff_a90");
        ImageManager imageBluffA180 = new ImageManager("objects/ground/bluff_a180");
        ImageManager imageBluffA270 = new ImageManager("objects/ground/bluff_a270");

        int groundSize = groundBlockSize * groundQuantity;
        int groundStart = groundQuantity / 2 * groundBlockSize - groundBlockSize / 2;

        for (int x = -groundStart; x < groundSize - groundStart; x += groundBlockSize) {
            for (int y = -groundStart; y < groundSize - groundStart; y += groundBlockSize) {
                Object.allGround.add(new Object(x, y, imageGrass));

                int confine = 960 + 8;
                int zone = 960;

                if (y == -zone && x < confine && -confine < x) {
                    Object.allDecoration.add(new Object(x, y - 64, imageBluff0));
                } else if (y == zone && x < confine && -confine < x) {
                    Object.allDecoration.add(new Object(x, y + 64, imageBluff180));
                }

                if (x == -zone && y < confine && -confine < y) {
                    Object.allDecoration.add(new Object(x - 64, y, imageBluff270));
                } else if (x == zone && y < confine && -confine < y) {
                    Object.allDecoration.add(new Object(x + 64, y, imageBluff90));
                }

                if (y == -zone && x == -zone) {
                    Object.allDecoration.add(new Object(x - 64, y - 64, imageBluffA270));
                } else if (y == -zone && x == zone) {
                    Object.allDecoration.add(new Object(x + 64, y - 64, imageBluffA0));
                } else if (y == zone && x == zone) {
                    Object.allDecoration.add(new Object(x + 64, y + 64, imageBluffA90));
                } else if (y == zone && x == -zone) {
                    Object.allDecoration.add(new Object(x - 64, y + 64, imageBluffA180));
                }
            }
        }
    }

    private void initializeTrees() {
        int treesQuantity = (groundQuantity * groundQuantity) / 2;
        int treesSpreading = (groundQuantity * groundBlockSize) / 2;
        UtilsWorld.putTrees(treesQuantity, treesSpreading);
    }

    public void update() {
        for (AI ai: AI.all) {
            ai.update();
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

    public void render() {
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
    }

    public void cleanUp() {
        Bullet.all.removeAll(Bullet.invalids);
        Bullet.invalids.clear();

        Weapon.all.removeAll(Weapon.invalids);
        Weapon.invalids.clear();

        Actor.all.removeAll(Actor.invalids);
        Actor.invalids.clear();

        AI.all.removeAll(AI.invalids);
        AI.invalids.clear();
    }

    public void play() {
        soundAmbiance.loop();
        soundAtmosphere.loop();
    }

    public void stop() {
        soundAmbiance.stop();
        soundAtmosphere.stop();
    }

    public void terminate() {
        AI.all.clear();
        Actor.all.clear();
        Weapon.all.clear();
        Object.allGround.clear();
        Object.allDecoration.clear();
        stop();
    }

}
