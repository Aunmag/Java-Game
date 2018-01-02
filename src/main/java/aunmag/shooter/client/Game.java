package aunmag.shooter.client;

import aunmag.nightingale.input.Input;
import aunmag.shooter.client.graphics.CameraShaker;
import aunmag.shooter.client.graphics.Crosshair;
import aunmag.shooter.client.graphics.Hud;
import aunmag.shooter.client.graphics.Blackout;
import aunmag.nightingale.Application;
import aunmag.shooter.client.states.Pause;
import aunmag.shooter.scenarios.ScenarioEncircling;
import org.lwjgl.glfw.GLFW;
import aunmag.shooter.world.World;

public class Game extends Application {

    private static boolean isPause;
    private static ScenarioEncircling scenario = null;
    private static World world;
    private static Player player;
    private static Crosshair crosshair = null;
    private static Pause pause;

    public Game() {
        pause = new Pause();
        setPause(true);
    }

    public static void initializeWorld() {
        deleteWorld();
        world = new World();
        player = new Player(world);
        crosshair = new Crosshair(player.getActor());
        pause.getButtonContinue().setIsAvailable(true);
        scenario = new ScenarioEncircling(world);
    }

    protected void gameUpdate() {
        if (isPause) {
            pause.update();
        } else {
            player.updateInput();
            world.update();
            scenario.update();
            CameraShaker.update();
            player.updateCameraPosition();

            if (Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
                setPause(true);
            }
        }
    }

    protected void gameRender() {
        if (isPause) {
            pause.render();
        } else {
            world.render();
            Blackout.render();
            crosshair.render();

            if (player.getActor().getHasWeapon()) {
                player.getActor().getWeapon().magazine.renderHud();
            }

            scenario.render();
            Hud.render();
        }
    }

    protected void gameTerminate() {
        deleteWorld();
    }

    public static void deleteWorld() {
        player = null;

        if (isWorldCreated()) {
            world.remove();
            world = null;
        }

        pause.getButtonContinue().setIsAvailable(false);

        if (scenario != null) {
            scenario.remove();
            scenario = null;
        }
    }

    /* Setters */

    public static void setPause(boolean isPause) {
        if (Game.isPause == isPause) {
            return;
        }

        Game.isPause = isPause;
        Application.getWindow().setCursorGrabbed(!isPause);

        if (isPause) {
            pause.resume();
            if (isWorldCreated()) {
                world.stopSounds();
            }
        } else {
            pause.suspend();
            if (isWorldCreated()) {
                world.playSounds();
            }
        }
    }

    /* Getters */

    public static boolean isPause() {
        return isPause;
    }

    public static World getWorld() {
        return world;
    }

    public static Player getPlayer() {
        return player;
    }

    public static boolean isWorldCreated() {
        return world != null;
    }

}
