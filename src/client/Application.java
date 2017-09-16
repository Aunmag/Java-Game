package client;

import gui.menus.MenuManager;
import client.input.Input;
import managers.PerformanceManager;
import nightingale.utilities.UtilsMath;
import sprites.Actor;

import java.awt.event.*;

/**
 * Created by Aunmag on 2016.09.24.
 */

public class Application implements Runnable {

    public static boolean isRunning = false;
    private static final int fpsLimit = 75;
    private static final float timeFrameDuration = 1000f / fpsLimit;
    private static float timeDelta = 0;
    private static long timeCurrent = 0;
    private Thread thread;

    public synchronized void start() {
        if (isRunning) {
            return;
        }

        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        Actor.loadSounds();

        long timeLast = System.currentTimeMillis();
        while (isRunning) {
            timeCurrent = System.currentTimeMillis();
            long timePassed = timeCurrent - timeLast;
            timeDelta = timePassed / timeFrameDuration;

            if (timeDelta >= 1) {
                timeLast = timeCurrent;
                update();
                render();
                cleanUp();
            }
        }

        stop();
    }

    private void update() {
        if (PerformanceManager.isMonitoring) {
            PerformanceManager.timerUpdating.makeStart();
        }

        Input.update();

        if (GamePlay.getIsActive()) {
            GamePlay.update();
        } else {
            MenuManager.update();
        }

        if (PerformanceManager.isMonitoring) {
            PerformanceManager.timerUpdating.makeStop();
        }
    }

    private void render() {
        if (PerformanceManager.isMonitoring) {
            PerformanceManager.timerRendering.makeStart();
        }

        Display.prepareBufferStrategy();
        Display.prepareGraphicsHud();

        if (GamePlay.getIsActive()) {
            Display.prepareGraphics();
            scaleGraphics();
            rotateGraphics();
            GamePlay.render();
            Camera.render();
            Display.getGraphics().dispose();
        } else {
            MenuManager.render();
        }

        Display.getGraphicsHud().dispose();
        Display.getBufferStrategy().show();

        if (PerformanceManager.isMonitoring) {
            PerformanceManager.timerRendering.makeStop();
        }
    }

    private void scaleGraphics() {
        float zoom = Camera.getZoom();
        Display.getGraphics().scale(zoom, zoom);
    }

    private void rotateGraphics() {
        float radians = Camera.getRadians() + (float) UtilsMath.PIx0_5;
        float x = Camera.getCenterX();
        float y = Camera.getCenterY();
        Display.getGraphics().rotate(-radians, x, y);
    }

    private void cleanUp() {
        if (PerformanceManager.isMonitoring) {
            PerformanceManager.timerFinishing.makeStart();
        }

        if (GamePlay.getIsActive()) {
            GamePlay.cleanUp();
        }

        Input.cleanUp();

        if (PerformanceManager.isMonitoring) {
            PerformanceManager.timerFinishing.makeStop();
        }
    }

    public synchronized void stop() {
        isRunning = false;
        Display.getFrame().dispatchEvent(new WindowEvent(
                Display.getFrame(),
                WindowEvent.WINDOW_CLOSING
        ));
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* Getters */

    public static int getFpsLimit() {
        return fpsLimit;
    }

    public static float getTimeDelta() {
        return timeDelta;
    }

    public static long getTimeCurrent() {
        return timeCurrent;
    }

}
