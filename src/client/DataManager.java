package client;

import client.states.GameMenu;
import client.states.GamePlay;
import sprites.Actor;
import sprites.components.Camera;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * Client - Central Data Table
 *
 * Created by AunmagUser on 08.11.2016.
 */

public class DataManager {

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
    private static int displayMax = Math.max(displayWidth, displayHeight);
    private static float displayHalfWidth = displayWidth / 2f;
    private static float displayHalfHeight = displayHeight / 2f;
    private static float displayHalfMax = displayMax / 2f;

    private static Actor player;
    private static Camera camera;

    // Input:
    private static Input input;
    private static boolean isMousePressed = false;
    private static int mouseX = 0;
    private static int mouseY = 0;

    // Graphics:
    private static JFrame frame;
    private static BufferStrategy bufferStrategy;
    private static Graphics2D hud;
    private static Graphics2D graphics; // TODO: Rename
    private static boolean isCursorVisible = true;
    private static BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    private static Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), "");

    // Time:
    private static long time; // current time
    private static float tTick; // time for one frame
    private static float d = 0; // current delta time

    static {
        initialize();
    }

    public static void initialize() {
        tTick = 1_000 / Constants.FPS_LIMIT;
    }

    // Setters:

    public static void setIsRunning(boolean isRunning) {

        DataManager.isRunning = isRunning;

    }

    public static void setIsGameMenu(boolean isGameMenu) {

        DataManager.isGameMenu = isGameMenu;

    }

    public static void setIsGamePlay(boolean isGamePlay) {

        DataManager.isGamePlay = isGamePlay;

    }

    public static void setIsGameStarted(boolean isGameStarted) {

        DataManager.isGameStarted = isGameStarted;

    }

    public static void setGameMenu(GameMenu gameMenu) {

        DataManager.gameMenu = gameMenu;

    }

    public static void setGamePlay(GamePlay gamePlay) {

        DataManager.gamePlay = gamePlay;

    }

    public static void setIsPerformanceData(boolean isPerformanceData) {

        DataManager.isPerformanceData = isPerformanceData;

    }

    public static void setScreenResolution(int width, int height) {
        displayWidth = width;
        displayHeight = height;
        displayMax = Math.max(displayWidth, displayHeight);
        displayHalfWidth = displayWidth / 2f;
        displayHalfHeight = displayHeight / 2f;
        displayHalfMax = displayMax / 2f;
    }

    public static void setPlayer(Actor player) {

        DataManager.player = player;

    }

    public static void setFrame(JFrame frame) {

        DataManager.frame = frame;

    }

    public static void setBufferStrategy(BufferStrategy bufferStrategy) {

        DataManager.bufferStrategy = bufferStrategy;

    }

    public static void setHud(Graphics2D hud) {

        DataManager.hud = hud;

    }

    public static void setGraphics(Graphics2D graphics) {

        DataManager.graphics = graphics;

    }

    public static void setIsCursorVisible(boolean isCursorVisible) {

        DataManager.isCursorVisible = isCursorVisible;

        if (isCursorVisible) {
            frame.getContentPane().setCursor(Cursor.getDefaultCursor());
        } else {
            frame.getContentPane().setCursor(cursor);
        }

    }

    public static void setInput(Input input) {

        DataManager.input = input;

    }

    public static void setIsMousePressed(boolean isMousePressed) {

        DataManager.isMousePressed = isMousePressed;

    }

    public static void setMousePosition(int mouseX, int mouseY) {

        DataManager.mouseX = mouseX;
        DataManager.mouseY = mouseY;

    }

    public static void setTime(long time) {

        DataManager.time = time;

    }

    public static void setD(float d) {

        DataManager.d = d;

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

    public static int getDisplayMax() {
        return displayMax;
    }

    public static Actor getPlayer() {

        return player;

    }

    public static JFrame getFrame() {

        return frame;

    }

    public static BufferStrategy getBufferStrategy() {

        return bufferStrategy;

    }

    public static Graphics2D getHud() {

        return hud;

    }

    public static Graphics2D getGraphics() {

        return graphics;

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

    public static long getTime() {

        return time;

    }

    public static float getTTick() {

        return tTick;

    }

    public static float getD() {

        return d;

    }

    public static float getDisplayHalfWidth() {
        return displayHalfWidth;
    }

    public static float getDisplayHalfHeight() {
        return displayHalfHeight;
    }

    public static float getDisplayHalfMax() {
        return displayHalfMax;
    }

    public static Camera getCamera() {
        return camera;
    }

    public static void setCamera(Camera camera) {
        DataManager.camera = camera;
    }

}
