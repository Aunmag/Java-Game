package client;

import gui.menus.MenuManager;
import client.input.Input;
import sprites.Actor;

import java.awt.event.*;

/**
 * Created by Aunmag on 2016.09.24.
 */

public class Application implements Runnable {

    private Thread thread;

    public synchronized void start() {
        if (DataManager.getIsRunning()) {
            return;
        }

        DataManager.setIsRunning(true);
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        Actor.loadSounds();

        long timeLast = System.currentTimeMillis();
        while (DataManager.getIsRunning()) {
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
        if (DataManager.getIsPerformanceData()) {
            PerformanceManager.timerUpdating.makeStart();
        }

        Input.update();

        if (GamePlay.getIsActive()) {
            GamePlay.update();
        } else {
            MenuManager.update();
        }

        if (DataManager.getIsPerformanceData()) {
            PerformanceManager.timerUpdating.makeStop();
        }
    }

    private void render() {
        if (DataManager.getIsPerformanceData()) {
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

        if (DataManager.getIsPerformanceData()) {
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
        if (DataManager.getIsPerformanceData()) {
            PerformanceManager.timerFinishing.makeStart();
        }

        if (GamePlay.getIsActive()) {
            GamePlay.cleanUp();
        }

        Input.cleanUp();

        if (DataManager.getIsPerformanceData()) {
            PerformanceManager.timerFinishing.makeStop();
        }
    }

    public synchronized void stop() {
        DataManager.setIsRunning(false);
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
