package client;

import client.states.GameMenu;
import client.states.GamePlay;
import managers.ImageManager;
import sprites.Actor;

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
    private BufferStrategy bs;
    private Graphics2D g;
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

        bs = canvas.getBufferStrategy();

        Client.setBs(bs);

        hud = (Graphics2D) bs.getDrawGraphics();
        hud.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        hud.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        hud.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        hud.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        hud.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        hud.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        hud.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        Client.setHud(hud);

    }

    private void tick() {

        if (!Client.isRunning()) {
            return;
        }

        if (Client.isGamePlay()) {

            if (Client.getMouseX() != mouseLastX) {
                float mouseSpeed = (Client.getMouseX() - mouseLastX) / 3.8f;
                Client.getPlayer().setDegrees(Client.getPlayer().getDegrees() + (mouseSpeed % 360));
                int putMouseX = screenWidth / 2;
                int putMouseY = screenHeight / 2;
                robot.mouseMove(putMouseX, putMouseY);
                mouseLastX = width / 2;
            }

            Client.getPlayer().isMovingForward = input.keys[KeyEvent.VK_W];
            Client.getPlayer().isMovingBack = input.keys[KeyEvent.VK_S];
            Client.getPlayer().isMovingLeft = input.keys[KeyEvent.VK_A];
            Client.getPlayer().isMovingRight = input.keys[KeyEvent.VK_D];
            Client.getPlayer().isRunning = input.keys[KeyEvent.VK_SHIFT];
            Client.getPlayer().isAttacking = input.mouseButtons[MouseEvent.BUTTON1];

            if (input.keys[KeyEvent.VK_ADD]) {
                Client.setZoom(Client.getZoom() + Client.getZoom() * 0.01f);
            }
            if (input.keys[KeyEvent.VK_SUBTRACT]) {
                Client.setZoom(Client.getZoom() - Client.getZoom() * 0.01f);
            }

            GamePlay.tick();
            Client.updateCamera();

        } else {
            Client.getGameMenu().tick();
        }

        input.reset();

    }

    private void render() {

        updateGraphics();

        if (Client.isGamePlay()) {
            g = (Graphics2D) bs.getDrawGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            Client.setG(g);
            float zoom = Client.getZoom();
            Client.getG().scale(zoom, zoom);
            float xCenter = width / (zoom * 2);
            float yCenter = (Client.getCameraOffsetDefault()) / zoom;
            Client.setGX(Client.getPlayer().x - xCenter);
            Client.setGY(Client.getPlayer().y - yCenter);
            Client.getG().rotate(Math.toRadians(-Client.getPlayer().getDegrees() - 90), xCenter, yCenter);
            GamePlay.render();
            Client.getG().dispose();
        } else {
            Client.getGameMenu().render();
        }

        Client.getHud().dispose();
        bs.show();

    }

    // Runnable methods:

    @Override public void run() {

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

        Client.setScreenResolution(width, height);

        frame = new JFrame(Client.getTitle() + " v" + Client.getVersion());
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
        Client.setFrame(frame);
        canvas.createBufferStrategy(2);

        input = new Input(frame, canvas);
        Client.setInput(input);

        updateGraphics();

        Client.setGameMenu(new GameMenu());

        ImageManager.cacheImages();
        Actor.loadSounds();

        // Time:
        float d = 0;
        long tLast = System.currentTimeMillis();

        while (Client.isRunning()) {
            long tCurrent = System.currentTimeMillis();
            long tPass = tCurrent - tLast;
            tLast = tCurrent;
            d += tPass / Client.getTTick();
            if (d >= 1) {
                Client.setT(tCurrent);
                Client.setD(d);
                tick();
                render();
                d -= 1;
                if (Client.isPerformanceData()) {
                    Client.tPerformanceAverage.addValue(System.currentTimeMillis() - tCurrent);
                }
            }
        }

        stop();

    }

    public synchronized void start() {

        if (Client.isRunning()) {
            return;
        }

        Client.setIsRunning(true);
        thread = new Thread(this);
        thread.start();

    }

    public synchronized void stop() {

        Client.setIsRunning(false);

        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
