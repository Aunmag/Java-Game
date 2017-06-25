package client;

import client.graphics.Hud;
import client.graphics.Blackout;
import gui.menus.MenuManager;
import scenarios.*;
import sprites.Actor;
import world.World;

/**
 * Created by Aunmag on 2016.11.09.
 */

public class GamePlay {

    private static boolean isActive = false;
    private static Scenario scenario;
    private static World world;

    public static void initialize() {
        if (getIsWorldCreated()) {
            terminate();
        }

        scenario = new ScenarioEncircling();
        world = new World();
    }

    public static void update() {
        if (!Actor.getPlayer().getIsAlive()) {
            MenuManager.getMenuGameOver().activate();
            return;
        }

        if (scenario != null) {
            scenario.update();
        }
        world.update();
        Camera.update();
    }

    public static void render() {
        world.render();

        Blackout.render();

        if (scenario != null) {
            scenario.render();
        }

        Hud.render();
    }

    public static void cleanUp() {
        world.cleanUp();
    }

    public static void terminate() {
        world.terminate();
        world = null;
        scenario = null;
    }

    /* Setters */

    public static void setIsActive(boolean isActive) {
        if (isActive == GamePlay.isActive) {
            return;
        }

        GamePlay.isActive = isActive;
        Display.setIsCursorVisible(!isActive);

        if (getIsWorldCreated()) {
            if (isActive) {
                world.play();
            } else {
                world.stop();
            }
        }
    }

    /* Getters */

    public static boolean getIsActive() {
        return isActive;
    }

    public static boolean getIsWorldCreated() {
        return world != null;
    }

}
