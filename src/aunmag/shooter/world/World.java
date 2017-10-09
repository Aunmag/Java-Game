package aunmag.shooter.world;

import aunmag.shooter.client.Game;
import aunmag.shooter.client.graphics.WorldGrid;
import aunmag.shooter.factories.FactoryActor;
import aunmag.shooter.factories.FactoryWeapon;
import aunmag.shooter.managers.SoundManager;
import aunmag.shooter.sprites.Actor;
import aunmag.shooter.ai.Ai;
import aunmag.shooter.sprites.Bullet;
import aunmag.shooter.sprites.Object;
import aunmag.nightingale.Application;
import aunmag.nightingale.basics.BaseWorld;
import aunmag.nightingale.structures.Texture;
import aunmag.nightingale.utilities.UtilsBaseOperative;
import aunmag.nightingale.utilities.UtilsGraphics;
import aunmag.nightingale.utilities.UtilsMath;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class World extends BaseWorld {

    private static final int groundQuantity = 48;
    private static final int groundBlockSize = 128;
    private static final SoundManager soundAmbiance;
    private static final SoundManager soundAtmosphere;
    private WorldGrid worldGrid = new WorldGrid();

    public static List<Ai> ais = new ArrayList<>();
    public static List<Object> terrains = new ArrayList<>();
    public static List<Object> decorations = new ArrayList<>();
    public static List<Actor> actors = new ArrayList<>();
    public static List<Bullet> bullets = new ArrayList<>();
    public static List<Object> trees = new ArrayList<>();

    static {
        soundAmbiance = new SoundManager("/sounds/ambiance/birds.wav");
        soundAmbiance.setVolume(-8);
        soundAtmosphere = new SoundManager("/sounds/music/gameplay_atmosphere.wav");
        soundAtmosphere.setVolume(-24);
    }

    public World() {
        initializePlayer();
        initializeGround();
        initializeBluffs();
        initializeTrees();
    }

    private void initializePlayer() {
        // TODO: World should not know about client's player
        Actor player = FactoryActor.human();
        player.setRadians((float) -UtilsMath.PIx0_5);
        player.setWeapon(FactoryWeapon.mp27());

        Actor.setPlayer(player);
        actors.add(player);

        Application.getCamera().setTarget(player);
    }

    private void initializeGround() {
        Texture texture = Texture.getOrCreate("images/objects/ground/grass");

        int step = groundBlockSize;
        int size = step * groundQuantity;
        float first = (size / -2f) + (step / 2f);
        float last = first + size;

        for (float x = first; x < last; x += step) {
            for (float y = first; y < last; y += step) {
                terrains.add(new Object(x, y, 0, texture));
            }
        }
    }

    private void initializeBluffs() {
        Texture texture = Texture.getOrCreate("images/objects/ground/bluff");
        int quantity = 17;
        int step = groundBlockSize;
        int length = step * quantity;
        float first = (length / -2f) + (step / 2f);
        float last = first + length - step;

        for (float i = first + step; i <= last - step; i += step) {
            decorations.add(new Object(i, first, (float) Math.PI, texture));
            decorations.add(new Object(i, last, 0, texture));
            decorations.add(new Object(first, i, (float) UtilsMath.PIx0_5, texture));
            decorations.add(new Object(last, i, (float) UtilsMath.PIx1_5, texture));
        }

        texture = Texture.getOrCreate("images/objects/ground/bluff_corner");
        decorations.add(new Object(first, first, (float) Math.PI, texture));
        decorations.add(new Object(first, last, (float) UtilsMath.PIx0_5, texture));
        decorations.add(new Object(last, last, 0, texture));
        decorations.add(new Object(last, first, (float) UtilsMath.PIx1_5, texture));
    }

    private void initializeTrees() {
        int quantity = (groundQuantity * groundQuantity) / 2;
        int spreading = (groundQuantity * groundBlockSize) / 2;

        positionChoosing: for (int i = 0; i < quantity; i++) {
            int x = UtilsMath.randomizeBetween(-spreading, spreading);
            int y = UtilsMath.randomizeBetween(-spreading, spreading);

            for (Object air: trees) {
                if (Math.abs(x - air.getX()) < 128 && Math.abs(y - air.getY()) < 128) {
                    continue positionChoosing;
                }
            }

            int number = UtilsMath.random.nextInt(3) + 1;
            Texture texture = Texture.getOrCreate("images/objects/air/tree_" + number);
            Object tree = new Object(x, y, 0, texture);
            trees.add(tree);
        }
    }

    public void update() {
        super.update();
        UtilsBaseOperative.updateAll(ais);
        UtilsBaseOperative.updateAll(actors);
        UtilsBaseOperative.updateAll(bullets);
    }

    public void render() {
        if (Game.isVirtualMode()) {
            UtilsGraphics.drawPrepare();

            GL11.glColor3f(1, 0, 0);
            GL11.glLineWidth(2);
            float n = groundBlockSize * 8;
            UtilsGraphics.drawLine(-n, -n, +n, -n, true);
            UtilsGraphics.drawLine(+n, -n, +n, +n, true);
            UtilsGraphics.drawLine(+n, +n, -n, +n, true);
            UtilsGraphics.drawLine(-n, +n, -n, -n, true);

            GL11.glColor4f(1, 1, 1, 0.2f);
            GL11.glLineWidth(1);
            worldGrid.render();

            UtilsGraphics.drawFinish();
        } else {
            UtilsBaseOperative.renderAll(terrains);
            UtilsBaseOperative.renderAll(decorations);
        }

        UtilsBaseOperative.renderAll(actors);

        UtilsGraphics.drawPrepare();
        UtilsBaseOperative.renderAll(bullets);
        UtilsGraphics.drawFinish();

        if (!Game.isVirtualMode()) {
            UtilsBaseOperative.renderAll(trees);
        }
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
        UtilsBaseOperative.removeAll(ais);
        UtilsBaseOperative.removeAll(terrains);
        UtilsBaseOperative.removeAll(decorations);
        UtilsBaseOperative.removeAll(actors);
        UtilsBaseOperative.removeAll(bullets);
        UtilsBaseOperative.removeAll(trees);
        stop();
    }

}
