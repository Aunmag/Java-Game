package client;

import sprites.Actor;

/**
 * Created by Aunmag on 2016.11.08.
 */

public class DataManager {

    // States:
    private static boolean isRunning = false;
    private static boolean isGameStarted = false;
    private static boolean isPerformanceData = false;

    private static Actor player;

    /* Setters */

    public static void setIsRunning(boolean isRunning) {

        DataManager.isRunning = isRunning;

    }

    public static void setIsGameStarted(boolean isGameStarted) {

        DataManager.isGameStarted = isGameStarted;

    }

    public static void setIsPerformanceData(boolean isPerformanceData) {

        DataManager.isPerformanceData = isPerformanceData;

    }

    public static void setPlayer(Actor player) {

        DataManager.player = player;

    }

    /* Getters */

    public static boolean getIsRunning() {

        return isRunning;

    }

    public static boolean getIsGameStarted() {

        return isGameStarted;

    }

    public static boolean getIsPerformanceData() {

        return isPerformanceData;

    }

    public static Actor getPlayer() {

        return player;

    }

}
