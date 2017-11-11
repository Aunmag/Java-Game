package aunmag.shooter.world;

import aunmag.nightingale.audio.AudioSource;
import aunmag.nightingale.utilities.UtilsAudio;
import aunmag.shooter.actor.ActorType;
import aunmag.shooter.client.graphics.WorldGrid;
import aunmag.shooter.weapon.WeaponFactory;
import aunmag.shooter.actor.Actor;
import aunmag.shooter.ai.Ai;
import aunmag.shooter.weapon.Projectile;
import aunmag.nightingale.utilities.UtilsBaseOperative;
import aunmag.nightingale.utilities.UtilsGraphics;
import aunmag.nightingale.utilities.UtilsMath;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class World {

    private static final AudioSource soundAmbiance;
    private static final AudioSource soundAtmosphere;

    private final WorldGrid grid = new WorldGrid();
    public final WorldTime time = new WorldTime();
    public final List<Ai> ais = new ArrayList<>();
    public final List<Actor> actors = new ArrayList<>();
    public final List<Projectile> projectiles = new ArrayList<>();

    static {
        soundAmbiance = UtilsAudio.getOrCreateSoundOgg("sounds/ambiance/birds");
        soundAmbiance.setVolume(0.4f);
        soundAmbiance.setIsLooped(true);

        soundAtmosphere = UtilsAudio.getOrCreateSoundOgg("sounds/music/gameplay_atmosphere");
        soundAtmosphere.setVolume(0.06f);
        soundAtmosphere.setIsLooped(true);
    }

    public World() {
        initializePlayer();
    }

    private void initializePlayer() {
        // TODO: World should not know about client's player
        Actor player = new Actor(ActorType.human);
        player.setRadians((float) -UtilsMath.PIx0_5);
        player.setWeapon(WeaponFactory.mp27());

        Actor.setPlayer(player);
        actors.add(player);
    }

    public void update() {
        time.update();
        UtilsBaseOperative.updateAll(ais);
        UtilsBaseOperative.updateAll(actors);
        Actor.finalizeUpdate();
        UtilsBaseOperative.updateAll(projectiles);
    }

    public void render() {
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
        float n = 32f;
        UtilsGraphics.drawLine(-n, -n, +n, -n, true);
        UtilsGraphics.drawLine(+n, -n, +n, +n, true);
        UtilsGraphics.drawLine(+n, +n, -n, +n, true);
        UtilsGraphics.drawLine(-n, +n, -n, -n, true);

        GL11.glColor4f(1, 1, 1, 0.2f);
        GL11.glLineWidth(1);
        grid.render();

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
        UtilsBaseOperative.removeAll(actors);
        UtilsBaseOperative.removeAll(projectiles);
        stop();
    }

}
