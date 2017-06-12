package client.input;

import client.DataManager;
import client.Display;
import client.states.GamePlay;
import sprites.Actor;
import sprites.components.Camera;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * Created by Aunmag on 2016.10.05.
 */

public class Input {

    private static Robot robot;
    private static final float mouseSensitivity = 0.005f;

    static boolean[] keys = new boolean[256];
    static boolean[] keysJustReleased = new boolean[keys.length];
    static boolean[] buttons = new boolean[8];
    static boolean[] buttonsJustReleased = new boolean[buttons.length];

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

        if (DataManager.isGamePlay()) {
            updatePlayer();
            updateCamera();
        }
    }

    public static void updateStates() {
        if (keysJustReleased[KeyEvent.VK_ESCAPE] && DataManager.isGameStarted()) {
            // TODO: Improve this behavior
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

        if (keysJustReleased[KeyEvent.VK_F1]) {
            DataManager.setIsPerformanceData(!DataManager.isPerformanceData());
        }
    }

    public static void updatePlayer() {
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
        player.isAttacking = buttons[MouseEvent.BUTTON1];
    }

    public static void updateCamera() {
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

    public static void cleanUp() {
        Arrays.fill(keysJustReleased, false);
        Arrays.fill(buttonsJustReleased, false);
        mouseWheelRotation = 0;
    }

    /* Getters */

    public static boolean getIsButtonJustReleased(int event) {
        return buttonsJustReleased[event];
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
