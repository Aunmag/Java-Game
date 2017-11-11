package aunmag.shooter.world;

import aunmag.nightingale.audio.AudioSource;
import aunmag.nightingale.utilities.UtilsAudio;
import aunmag.shooter.actor.ActorType;
import aunmag.shooter.client.graphics.WorldGrid;
import aunmag.shooter.weapon.WeaponFactory;
import aunmag.shooter.actor.Actor;
import aunmag.shooter.ai.Ai;
import aunmag.shooter.weapon.Projectile;
import aunmag.nightingale.structures.Texture;
import aunmag.nightingale.utilities.UtilsBaseOperative;
import aunmag.nightingale.utilities.UtilsGraphics;
import aunmag.nightingale.utilities.UtilsMath;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class World {

    private static final int groundQuantity = 48;
    private static final int groundBlockSize = 4;
    private static final AudioSource soundAmbiance;
    private static final AudioSource soundAtmosphere;

    private WorldGrid worldGrid = new WorldGrid();

    public static List<Ai> ais = new ArrayList<>();
    public static List<Object> terrains = new ArrayList<>();
    public static List<Actor> actors = new ArrayList<>();
    public static List<Projectile> projectiles = new ArrayList<>();

    static {
        soundAmbiance = UtilsAudio.getOrCreateSoundOgg("sounds/ambiance/birds");
        soundAmbiance.setVolume(0.4f);
        soundAmbiance.setIsLooped(true);

        soundAtmosphere = UtilsAudio.getOrCreateSoundOgg("sounds/music/gameplay_atmosphere");
        soundAtmosphere.setVolume(0.06f);
        soundAtmosphere.setIsLooped(true);
    }

    public World() {
        WorldTime.reset();
        initializePlayer();
        initializeGround();
    }

    private void initializePlayer() {
        // TODO: World should not know about client's player
        Actor player = new Actor(ActorType.human);
        player.setRadians((float) -UtilsMath.PIx0_5);
        player.setWeapon(WeaponFactory.mp27());

        Actor.setPlayer(player);
        actors.add(player);
    }

    private void initializeGround() {
        Texture texture = Texture.getOrCreateAsSprite("images/objects/ground/grass");

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

    public void update() {
        WorldTime.update();
        UtilsBaseOperative.updateAll(ais);
        UtilsBaseOperative.updateAll(actors);
        Actor.finalizeUpdate();
        UtilsBaseOperative.updateAll(projectiles);
    }

    public void render() {
        UtilsBaseOperative.renderAll(terrains);
        renderGrid();
        UtilsBaseOperative.renderAll(actors);

        UtilsGraphics.drawPrepare();
        UtilsBaseOperative.renderAll(projectiles);
        GL11.glLineWidth(1);
        UtilsGraphics.drawFinish();
    }

    private void renderGrid() {
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
    }

    public void play() {
        soundAmbiance.play();
        soundAtmosphere.play();
    }

    public void stop() {
        soundAmbiance.stop();
        soundAtmosphere.stop();
    }

    public void remove() {
        UtilsBaseOperative.removeAll(ais);
        UtilsBaseOperative.removeAll(terrains);
        UtilsBaseOperative.removeAll(actors);
        UtilsBaseOperative.removeAll(projectiles);
        stop();
    }

}
