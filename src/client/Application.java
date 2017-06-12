package client;

import client.input.Input;
import client.states.GameMenu;
import client.states.GamePlay;
import sprites.Actor;

import java.awt.event.*;

/**
 * Created by Aunmag on 2016.09.24.
 */

public class Application implements Runnable {

    private Thread thread;

    public synchronized void start() {
        if (DataManager.isRunning()) {
            return;
        }

        DataManager.setIsRunning(true);
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        DataManager.setGameMenu(new GameMenu());
        Actor.loadSounds();

        long timeLast = System.currentTimeMillis();
        while (DataManager.isRunning()) {
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
        if (DataManager.isPerformanceData()) {
            PerformanceManager.timerUpdating.makeStart();
        }

        Input.update();

        if (DataManager.isGamePlay()) {
            GamePlay.update();
        } else {
            DataManager.getGameMenu().update();
        }

        if (DataManager.isPerformanceData()) {
            PerformanceManager.timerUpdating.makeStop();
        }
    }

    private void render() {
        if (DataManager.isPerformanceData()) {
            PerformanceManager.timerRendering.makeStart();
        }

        Display.prepareBufferStrategy();
        Display.prepareGraphicsHud();

        if (DataManager.isGamePlay()) {
            Display.prepareGraphics();
            scaleGraphics();
            rotateGraphics();
            GamePlay.render();
            Camera.render();
            Display.getGraphics().dispose();
        } else {
            DataManager.getGameMenu().render();
        }

        Display.getGraphicsHud().dispose();
        Display.getBufferStrategy().show();

        if (DataManager.isPerformanceData()) {
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
        if (DataManager.isPerformanceData()) {
            PerformanceManager.timerFinishing.makeStart();
        }

        if (DataManager.isGamePlay()) {
            GamePlay.cleanUp();
        }

        Input.cleanUp();

        if (DataManager.isPerformanceData()) {
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
