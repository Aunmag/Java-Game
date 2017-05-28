package client;

import client.states.GameMenu;
import client.states.GamePlay;
import managers.ArrayAverage;
import sprites.Actor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import static java.lang.Integer.max;

/**
 * Client - Central Data Table
 *
 * Created by AunmagUser on 08.11.2016.
 */

public class Client {

    // States:
    private static boolean isRunning = false;
    private static boolean isGameMenu = true;
    private static boolean isGamePlay = false;
    private static boolean isGameStarted = false;
    private static GameMenu gameMenu;
    private static GamePlay gamePlay;
    private static boolean isPerformanceData = false;

    // Screen:
    private static int displayWidth = 1280;
    private static int displayHeight = 720;
    private static int displayMax = max(displayWidth, displayHeight);
    private static float zoom = 1.4f;

    private static int cameraOffsetDefault = displayHeight - 8;
    private static float cameraVisibility; // the maximal allowed distance between sprite and camera to be visible
    private static float cameraX;
    private static float cameraY;

    // Player:
    private static Actor player;
    private static float playerX;
    private static float playerY;
    private static float playerRadians;

    // Input:
    private static Input input;
    private static boolean isMousePressed = false;
    private static int mouseX = 0;
    private static int mouseY = 0;

    // Graphics:
    private static JFrame frame;
    private static BufferStrategy bs;
    private static Graphics2D hud;
    private static Graphics2D g; // TODO: Rename
    private static float gX;
    private static float gY;
    private static boolean isCursorVisible = true;
    private static BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    private static Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), "");

    // Time:
    private static long t; // current time
    private static float tTick; // time for one frame
    private static float d = 0; // current delta time
    public static ArrayAverage tPerformanceAverage;

    static {
        initialize();
    }

    public static void initialize() {
        tTick = 1_000 / Constants.FPS_LIMIT;
        tPerformanceAverage = new ArrayAverage(Constants.FPS_LIMIT);
    }

    // Updaters:

    public static void updateCamera() {
//        float offset = cameraOffsetDefault / zoom; // TODO: RESET!
        float offset = cameraOffsetDefault / 2 / zoom;
        cameraX = (float) (player.getX() + offset * Math.cos(player.getRadians()));
        cameraY = (float) (player.getY() + offset * Math.sin(player.getRadians()));
        cameraVisibility = displayMax * 0.75f / zoom;

    }

    // Setters:

    public static void setIsRunning(boolean isRunning) {

        Client.isRunning = isRunning;

    }

    public static void setIsGameMenu(boolean isGameMenu) {

        Client.isGameMenu = isGameMenu;

    }

    public static void setIsGamePlay(boolean isGamePlay) {

        Client.isGamePlay = isGamePlay;

    }

    public static void setIsGameStarted(boolean isGameStarted) {

        Client.isGameStarted = isGameStarted;

    }

    public static void setGameMenu(GameMenu gameMenu) {

        Client.gameMenu = gameMenu;

    }

    public static void setGamePlay(GamePlay gamePlay) {

        Client.gamePlay = gamePlay;

    }

    public static void setIsPerformanceData(boolean isPerformanceData) {

        Client.isPerformanceData = isPerformanceData;

    }

    public static void setScreenResolution(int width, int height) {

        if (width < 16) {
            width = 16;
        }

        if (height < 16) {
            height = 16;
        }

        Client.displayWidth = width;
        Client.displayHeight = height;
        Client.displayMax = max(width, height);
        cameraOffsetDefault = height - 8;

    }

    public static void setZoom(float zoom) {

        if (zoom < 1) {
            zoom = 1;
        } else if (zoom > 3) {
            zoom = 3;
        }

        Client.zoom = zoom;

    }

    public static void setPlayer(Actor player) {

        Client.player = player;

    }

    public static void setPlayerPosition(float playerX, float playerY) {

        Client.playerX = playerX;
        Client.playerY = playerY;

    }

    public static void setPlayerRadians(float playerRadians) {

        Client.playerRadians = playerRadians;

    }

    public static void setFrame(JFrame frame) {

        Client.frame = frame;

    }

    public static void setBs(BufferStrategy bs) {

        Client.bs = bs;

    }

    public static void setHud(Graphics2D hud) {

        Client.hud = hud;

    }

    public static void setG(Graphics2D g) {

        Client.g = g;

    }

    public static void setGX(float gX) {

        Client.gX = gX;

    }

    public static void setGY(float gY) {

        Client.gY = gY;

    }

    public static void setIsCursorVisible(boolean isCursorVisible) {

        Client.isCursorVisible = isCursorVisible;

        if (isCursorVisible) {
            frame.getContentPane().setCursor(Cursor.getDefaultCursor());
        } else {
            frame.getContentPane().setCursor(cursor);
        }

    }

    public static void setInput(Input input) {

        Client.input = input;

    }

    public static void setIsMousePressed(boolean isMousePressed) {

        Client.isMousePressed = isMousePressed;

    }

    public static void setMousePosition(int mouseX, int mouseY) {

        Client.mouseX = mouseX;
        Client.mouseY = mouseY;

    }

    public static void setT(long t) {

        Client.t = t;

    }

    public static void setD(float d) {

        Client.d = d;

    }

    public static boolean isRunning() {

        return isRunning;

    }

    public static boolean isGamePlay() {

        return isGamePlay;

    }

    public static boolean isGameMenu() {

        return isGameMenu;

    }

    public static boolean isGameStarted() {

        return isGameStarted;

    }

    public static GameMenu getGameMenu() {

        return gameMenu;

    }

    public static GamePlay getGamePlay() {

        return gamePlay;

    }

    public static boolean isPerformanceData() {

        return isPerformanceData;

    }

    public static int getDisplayWidth() {

        return displayWidth;

    }

    public static int getDisplayHeight() {

        return displayHeight;

    }

    public static int getCameraOffsetDefault() {

        return cameraOffsetDefault;

    }

    public static float getZoom() {

        return zoom;

    }

    public static float getCameraX() {

        return cameraX;

    }

    public static float getCameraY() {

        return cameraY;

    }

    public static float getCameraVisibility() {

        return cameraVisibility;

    }

    public static Actor getPlayer() {

        return player;

    }

    public static float getPlayerX() {

        return playerX;

    }

    public static float getPlayerY() {

        return playerY;

    }

    public static float getPlayerRadians() {

        return playerRadians;

    }

    public static JFrame getFrame() {

        return frame;

    }

    public static BufferStrategy getBs() {

        return bs;

    }

    public static Graphics2D getHud() {

        return hud;

    }

    public static Graphics2D getG() {

        return g;

    }

    public static float getGX() {

        return gX;

    }

    public static float getGY() {

        return gY;

    }

    public static boolean isCursorVisible() {

        return isCursorVisible;

    }

    public static Input getInput() {

        return input;

    }

    public static boolean isMousePressed() {

        return isMousePressed;

    }

    public static int getMouseX() {

        return mouseX;

    }

    public static int getMouseY() {

        return mouseY;

    }

    public static long getT() {

        return t;

    }

    public static float getTTick() {

        return tTick;

    }

    public static float getD() {

        return d;

    }

}
