package client.input;

import client.*;
import gui.menus.MenuManager;
import managers.PerformanceManager;
import sprites.Actor;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by Aunmag on 2016.10.05.
 */

public class Input {

    private static Robot robot;
    private static final float mouseSensitivity = 0.005f;

    static boolean[] keys = new boolean[256];
    static boolean[] keysJustPressed = new boolean[keys.length];
    static boolean[] buttons = new boolean[8];
    static boolean[] buttonsJustPressed = new boolean[buttons.length];

    static int mouseWheelRotation = 0;
    static int mouseX;
    static int mouseY;

    static {
        new Listener();

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void update() {
        updateStates();

        if (GamePlay.getIsActive()) {
            updatePlayer();
            updateCamera();
        }
    }

    public static void updateStates() {
        if (getIsKeyJustPressed(KeyEvent.VK_ESCAPE) && GamePlay.getIsWorldCreated()) {
            // TODO: Improve this behavior
            if (GamePlay.getIsActive()) {
                GamePlay.setIsActive(false);
                MenuManager.getMenuMain().getSound().loop();
            } else {
                GamePlay.setIsActive(true);
                MenuManager.getMenuMain().getSound().stop();
            }
        }

        if (getIsKeyJustPressed(KeyEvent.VK_F1)) {
            PerformanceManager.isMonitoring = !PerformanceManager.isMonitoring;
        }
    }

    public static void updatePlayer() {
        Actor player = Actor.getPlayer();

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
        player.isAttacking = buttons[MouseEvent.BUTTON1];
    }

    public static void updateCamera() {
        float zoom = Camera.getZoom();

        if (keys[KeyEvent.VK_ADD]) {
            Camera.setZoom(zoom + zoom * 0.01f);
        }

        if (keys[KeyEvent.VK_SUBTRACT]) {
            Camera.setZoom(zoom - zoom * 0.01f);
        }
    }

    public static void cleanUp() {
        mouseWheelRotation = 0;
    }

    /* Getters */

    public static boolean getIsKeyJustPressed(int event) {
        boolean isPressed = keysJustPressed[event];
        keysJustPressed[event] = false;
        return isPressed;
    }

    public static boolean getIsButtonJustPressed(int event) {
        boolean isPressed = buttonsJustPressed[event];
        buttonsJustPressed[event] = false;
        return isPressed;
    }

    public static int getMouseWheelRotation() {
        return mouseWheelRotation;
    }

    public static int getMouseX() {
        return mouseX;
    }

    public static int getMouseY() {
        return mouseY;
    }

}
