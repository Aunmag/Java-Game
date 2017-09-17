package world;

import ai.AI;
import managers.SoundManager;
import nightingale.Application;
import nightingale.basics.BaseSprite;
import nightingale.basics.BaseWorld;
import nightingale.structures.Texture;
import nightingale.utilities.UtilsGraphics;
import nightingale.utilities.UtilsMath;
import sprites.Actor;
import sprites.Bullet;
import sprites.Object;
import sprites.Weapon;
import utilities.UtilsWorld;

/**
 * Created by Aunmag on 2017.06.24.
 */

public class World extends BaseWorld {

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
        Actor player = new Actor(0, 0, (float) -UtilsMath.PIx0_5, "human");
        Weapon.all.add(new Weapon(player));

        Actor.setPlayer(player);
        Actor.all.add(player);

        Application.getCamera().setTarget(player);
    }

    private void initializeGround() {
        // TODO: Improve all:

        Texture imageGrass = Texture.getOrCreate("images/objects/ground/grass");
        Texture imageBluff0 = Texture.getOrCreate("images/objects/ground/bluff_0");
        Texture imageBluff90 = Texture.getOrCreate("images/objects/ground/bluff_90");
        Texture imageBluff180 = Texture.getOrCreate("images/objects/ground/bluff_180");
        Texture imageBluff270 = Texture.getOrCreate("images/objects/ground/bluff_270");
        Texture imageBluffA0 = Texture.getOrCreate("images/objects/ground/bluff_a0");
        Texture imageBluffA90 = Texture.getOrCreate("images/objects/ground/bluff_a90");
        Texture imageBluffA180 = Texture.getOrCreate("images/objects/ground/bluff_a180");
        Texture imageBluffA270 = Texture.getOrCreate("images/objects/ground/bluff_a270");

        int groundSize = groundBlockSize * groundQuantity;
        int groundStart = groundQuantity / 2 * groundBlockSize - groundBlockSize / 2;

        for (int x = -groundStart; x < groundSize - groundStart; x += groundBlockSize) {
            for (int y = -groundStart; y < groundSize - groundStart; y += groundBlockSize) {
                Object.allGround.add(new Object(x, y, 0, imageGrass));

                int confine = 960 + 8;
                int zone = 960;

                if (y == -zone && x < confine && -confine < x) {
                    Object.allDecoration.add(new Object(x, y - 64, 0, imageBluff0));
                } else if (y == zone && x < confine && -confine < x) {
                    Object.allDecoration.add(new Object(x, y + 64, 0, imageBluff180));
                }

                if (x == -zone && y < confine && -confine < y) {
                    Object.allDecoration.add(new Object(x - 64, y, 0, imageBluff270));
                } else if (x == zone && y < confine && -confine < y) {
                    Object.allDecoration.add(new Object(x + 64, y, 0, imageBluff90));
                }

                if (y == -zone && x == -zone) {
                    Object.allDecoration.add(new Object(x - 64, y - 64, 0, imageBluffA270));
                } else if (y == -zone && x == zone) {
                    Object.allDecoration.add(new Object(x + 64, y - 64, 0, imageBluffA0));
                } else if (y == zone && x == zone) {
                    Object.allDecoration.add(new Object(x + 64, y + 64, 0, imageBluffA90));
                } else if (y == zone && x == -zone) {
                    Object.allDecoration.add(new Object(x - 64, y + 64, 0, imageBluffA180));
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
        super.update();

        for (AI ai: AI.all) {
            ai.update();
        }

        AI.all.removeAll(AI.invalids);
        AI.invalids.clear();

        BaseSprite.updateAll(Actor.all);
        BaseSprite.updateAll(Weapon.all);
        BaseSprite.updateAll(Bullet.all);
    }

    public void render() {
        BaseSprite.renderAll(Object.allGround);
        BaseSprite.renderAll(Object.allDecoration);
        BaseSprite.renderAll(Weapon.all);
        BaseSprite.renderAll(Actor.all);

        UtilsGraphics.drawPrepare();
        BaseSprite.renderAll(Bullet.all);
        UtilsGraphics.drawFinish();

        BaseSprite.renderAll(Object.allAir);
    }

    public void play() {
        soundAmbiance.loop();
        soundAtmosphere.loop();
    }

    public void stop() {
        soundAmbiance.stop();
        soundAtmosphere.stop();
    }

    public void remove() {
        AI.all.clear();
        AI.invalids.clear();
        BaseSprite.removeAll(Actor.all);
        BaseSprite.removeAll(Weapon.all);
        BaseSprite.removeAll(Bullet.all);
        BaseSprite.removeAll(Object.allGround);
        BaseSprite.removeAll(Object.allDecoration);
        BaseSprite.removeAll(Object.allAir);
        stop();
    }

}
