package client;

import client.states.GameMenu;
import client.states.GamePlay;
import sprites.Actor;
import sprites.components.Camera;

/**
 * Client - Central Data Table
 *
 * Created by AunmagUser on 08.11.2016.
 */

public class DataManager {

    // States:
    private static boolean isRunning = false;
    private static boolean isGameMenu = true; // TODO: создать едниый world class. использовать isPause
    private static boolean isGamePlay = false;
    private static boolean isGameStarted = false;
    private static GameMenu gameMenu;
    private static GamePlay gamePlay;
    private static boolean isPerformanceData = false;

    private static Input input; // TODO: Make static
    private static Actor player;
    private static Camera camera; // TODO: Make static

    /* Setters */

    public static void setIsRunning(boolean isRunning) {

        DataManager.isRunning = isRunning;

    }

    public static void setIsGameMenu(boolean isGameMenu) {

        DataManager.isGameMenu = isGameMenu;

    }

    public static void setIsGamePlay(boolean isGamePlay) {

        DataManager.isGamePlay = isGamePlay;

    }

    public static void setIsGameStarted(boolean isGameStarted) {

        DataManager.isGameStarted = isGameStarted;

    }

    public static void setGameMenu(GameMenu gameMenu) {

        DataManager.gameMenu = gameMenu;

    }

    public static void setGamePlay(GamePlay gamePlay) {

        DataManager.gamePlay = gamePlay;

    }

    public static void setIsPerformanceData(boolean isPerformanceData) {

        DataManager.isPerformanceData = isPerformanceData;

    }

    public static void setPlayer(Actor player) {

        DataManager.player = player;

    }

    public static void setInput(Input input) {

        DataManager.input = input;

    }

    public static void setCamera(Camera camera) {
        DataManager.camera = camera;
    }

    /* Getters */

    public static boolean isRunning() {

        return isRunning;

    }

    public static boolean isGamePlay() {

        return isGamePlay;

    }

    public static boolean isGameStarted() {

        return isGameStarted;

    }

    public static GameMenu getGameMenu() {

        return gameMenu;

    }

    public static GamePlay getGamePlay() {

        return gamePlay;

    }

    public static boolean isPerformanceData() {

        return isPerformanceData;

    }

    public static Actor getPlayer() {

        return player;

    }

    public static Input getInput() {

        return input;

    }

    public static Camera getCamera() {
        return camera;
    }

}
