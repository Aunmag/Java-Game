package client;

import client.states.GamePlay;
import sprites.Actor;
import sprites.components.Camera;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Aunmag on 2016.10.05.
 */

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    public boolean[] keys = new boolean[256];
    public boolean[] mouseButtons = new boolean[8];
    private int mouseWheelRotation = 0;
    public boolean isMouseReleased = false;
    private int mouseX;
    private int mouseY;
    private static final float mouseSensitivity = 0.005f;
    private Robot robot;

    Input(JFrame frame, Canvas canvas) {
        frame.addKeyListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addMouseWheelListener(this);

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (DataManager.isGamePlay()) {
            updatePlayer();
            updateCamera();
        }
    }

    public void updatePlayer() {
        Actor player = DataManager.getPlayer();

        if (player == null) {
            return;
        }

        if (mouseX != Display.getHalfWidth()) {
            float mouseVelocityX = mouseX - Display.getHalfWidth();
            float setPlayerRadians = player.getRadians() + mouseVelocityX * mouseSensitivity;
            player.setRadians(setPlayerRadians);

            mouseX = (int) Display.getHalfWidth();
            mouseY = (int) Display.getHalfHeight();
            robot.mouseMove(mouseX, mouseY);
        }

        player.isWalkingForward = keys[KeyEvent.VK_W];
        player.isWalkingBack = keys[KeyEvent.VK_S];
        player.isWalkingLeft = keys[KeyEvent.VK_A];
        player.isWalkingRight = keys[KeyEvent.VK_D];
        player.isSprinting = keys[KeyEvent.VK_SHIFT];
        player.isAttacking = mouseButtons[MouseEvent.BUTTON1];
    }

    public void updateCamera() {
        Camera camera = DataManager.getCamera();

        if (camera == null) {
            return;
        }

        float zoom = camera.getZoom();

        if (keys[KeyEvent.VK_ADD]) {
            camera.setZoom(zoom + zoom * 0.01f);
        }

        if (keys[KeyEvent.VK_SUBTRACT]) {
            camera.setZoom(zoom - zoom * 0.01f);
        }
    }

    public void cleanUp() {
        isMouseReleased = false;
        mouseWheelRotation = 0;
    }

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && DataManager.isGameStarted()) {
            if (DataManager.isGamePlay()) {
                DataManager.getGameMenu().activeMenuMain();
                DataManager.getGameMenu().getMenuMain().getSoundscape().loop();
                DataManager.getGamePlay().getAmbiance().stop();
                DataManager.getGamePlay().getAtmosphere().stop();
            } else {
                GamePlay.activate();
                DataManager.getGameMenu().getMenuMain().getSoundscape().stop();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_F1) {
            DataManager.setIsPerformanceData(!DataManager.isPerformanceData());
        }
    }

    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    public void keyTyped(KeyEvent e) {}

    public void mousePressed(MouseEvent e) {
        mouseButtons[e.getButton()] = true;

        if (e.getButton() == MouseEvent.BUTTON1) {
            isMouseReleased = false;
        }
    }

    public void mouseReleased(MouseEvent e) {
        mouseButtons[e.getButton()] = false;

        if (e.getButton() == MouseEvent.BUTTON1) {
            isMouseReleased = true;
        }
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        e.consume();
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        e.consume();
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseWheelRotation = e.getWheelRotation();
    }

    /* Getters */

    public int getMouseWheelRotation() {
        return mouseWheelRotation;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

}
