package client;

import client.states.GameMenu;
import client.states.GamePlay;
import sprites.Actor;
import sprites.components.Camera;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import javax.swing.*;

// Created by Aunmag on 24.09.2016.

public class Application implements Runnable {

    private JFrame frame;
    private Canvas canvas;
    private Robot robot;
    private int width;
    private int height;
    private int screenWidth;
    private int screenHeight;
    private Thread thread;
    private Input input;
    private BufferStrategy bufferStrategy;
    private Graphics2D graphics;
    private Graphics2D hud;
    private boolean isFullscreenMode = true;
    private int mouseLastX;

    public Application() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void updateGraphics() {

        bufferStrategy = canvas.getBufferStrategy();

        DataManager.setBufferStrategy(bufferStrategy);

        hud = (Graphics2D) bufferStrategy.getDrawGraphics();
        hud.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        hud.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        hud.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        hud.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        hud.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        hud.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        hud.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        DataManager.setHud(hud);

    }

    private void update() {

        if (!DataManager.isRunning()) {
            return;
        }

        if (DataManager.isPerformanceData()) {
            PerformanceManager.timerUpdating.makeStart();
        }

        if (DataManager.isGamePlay()) {
            Actor player = DataManager.getPlayer();

            if (DataManager.getMouseX() != mouseLastX) {
                float mouseSensitivity = 0.005f;
                float mouseVelocity = DataManager.getMouseX() - mouseLastX;
                float rotatePlayerRadians = player.getRadians() + mouseVelocity * mouseSensitivity;
                player.setRadians(rotatePlayerRadians);
                robot.mouseMove(screenWidth / 2, screenHeight / 2);
                mouseLastX = width / 2;
            }

            player.isWalkingForward = input.keys[KeyEvent.VK_W];
            player.isWalkingBack = input.keys[KeyEvent.VK_S];
            player.isWalkingLeft = input.keys[KeyEvent.VK_A];
            player.isWalkingRight = input.keys[KeyEvent.VK_D];
            player.isSprinting = input.keys[KeyEvent.VK_SHIFT];
            player.isAttacking = input.mouseButtons[MouseEvent.BUTTON1];

            Camera camera = DataManager.getCamera();
            float zoom = camera.getZoom();
            if (input.keys[KeyEvent.VK_ADD]) {
                camera.setZoom(zoom + zoom * 0.01f);
            }
            if (input.keys[KeyEvent.VK_SUBTRACT]) {
                camera.setZoom(zoom - zoom * 0.01f);
            }

            GamePlay.tick();
            DataManager.getCamera().update();

        } else {
            DataManager.getGameMenu().tick();
        }

        input.reset();

        if (DataManager.isPerformanceData()) {
            PerformanceManager.timerUpdating.makeStop();
        }
    }

    private void render() {
        if (DataManager.isPerformanceData()) {
            PerformanceManager.timerRendering.makeStart();
        }

        updateGraphics();

        if (DataManager.isGamePlay()) {
            renderPrepareGraphics();
            renderZoomGraphics();
            renderRotateGraphics();
            GamePlay.render();
            DataManager.getCamera().render();
            DataManager.getGraphics().dispose();
        } else {
            DataManager.getGameMenu().render();
        }

        DataManager.getHud().dispose();
        bufferStrategy.show();

        if (DataManager.isPerformanceData()) {
            PerformanceManager.timerRendering.makeStop();
        }
    }

    private void renderPrepareGraphics() {
        graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF
        );
        graphics.setRenderingHint(
                RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED
        );
        graphics.setRenderingHint(
                RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_SPEED
        );
        graphics.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED
        );
        DataManager.setGraphics(graphics);
    }

    private void renderZoomGraphics() {
        float zoom = DataManager.getCamera().getZoom();
        DataManager.getGraphics().scale(zoom, zoom);
    }

    private void renderRotateGraphics() {
        float radians = DataManager.getCamera().getRadians() + (float) Constants.PI_0_5;
        float x = DataManager.getCamera().getCenterX();
        float y = DataManager.getCamera().getCenterY();
        DataManager.getGraphics().rotate(-radians, x, y);
    }

    private void finish() {
        if (DataManager.isPerformanceData()) {
            PerformanceManager.timerFinishing.makeStart();
        }

        if (DataManager.isGamePlay()) {
            GamePlay.deleteInvalids();
        }

        if (DataManager.isPerformanceData()) {
            PerformanceManager.timerFinishing.makeStop();
        }
    }

    // Runnable methods:

    public void run() {

        // Create display:

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // get display size
        screenWidth = (int) screenSize.getWidth();
        screenHeight = (int) screenSize.getHeight();

        if (isFullscreenMode) {
            width = screenWidth;
            height = screenHeight;
        } else {
            width = 1280;
            height = 720;
        }

        DataManager.setScreenResolution(width, height);

        frame = new JFrame(Constants.TITLE + " v" + Constants.VERSION);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setUndecorated(true); // disable OS windowed shell
        frame.setLocationRelativeTo(null);
        if (isFullscreenMode) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // fullscreen mode
        }
        frame.setVisible(true);
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setFocusable(false);
        frame.add(canvas);
        frame.pack();
        DataManager.setFrame(frame);
        canvas.createBufferStrategy(2);

        input = new Input(frame, canvas);
        DataManager.setInput(input);

        updateGraphics();

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
                finish();
            }
        }

        stop();

    }

    public synchronized void start() {
        if (DataManager.isRunning()) {
            return;
        }

        DataManager.setIsRunning(true);
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        DataManager.setIsRunning(false);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
