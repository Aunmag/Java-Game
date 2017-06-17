package client;

import gui.menus.MenuManager;
import client.input.Input;
import sprites.Actor;

import java.awt.event.*;

/**
 * Created by Aunmag on 2016.09.24.
 */

public class Application implements Runnable {

    public static boolean isRunning = false;
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
            long timeCurrent = System.currentTimeMillis();
            long timePassed = timeCurrent - timeLast;
            float timeDelta = timePassed / TimeManager.FRAME_DURATION;

            if (timeDelta >= 1) {
                timeLast = timeCurrent;
                TimeManager.setTimeCurrent(timeCurrent);
                TimeManager.setTimeDelta(timeDelta);
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
        float radians = Camera.getRadians() + (float) Constants.PI_0_5;
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

}
