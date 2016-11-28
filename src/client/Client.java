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

    // Constants:
    private static final String AUTHOR = "Aunmag";
    private static final String TITLE = "A Zombie Shooter Game";
    private static final String VERSION = "0.3.0 (pre-alpha)";

    // States:
    private static boolean isRunning = false;
    private static boolean isGameMenu = true;
    private static boolean isGamePlay = false;
    private static boolean isGameStarted = false;
    private static GameMenu gameMenu;
    private static GamePlay gamePlay;
    private static boolean isPerformanceData = false;

    // Screen:
    private static int width = 800;
    private static int height = 600;
    private static int screenMax = max(width, height);
    private static double zoom = 1.2;

    // Player:
    private static Actor player;
    private static double playerX;
    private static double playerY;
    private static double playerRadians;

    // Input:
    private static Input input;
    private static boolean isMousePressed = false;
    private static int mouseX = 0;
    private static int mouseY = 0;

    // Graphics:
    private static JFrame frame;
    private static BufferStrategy bs;
    private static Graphics2D hud;
    private static Graphics2D g;
    private static double gX;
    private static double gY;
    private static boolean isCursorVisible = true;
    private static BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    private static Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), "");

    // Frames:
    private static int fpsLimit;
    private static boolean isFrame2Fold = false;
    private static boolean isFrame4Fold = false;

    // Time:
    private static long t; // current time
    private static double tTick; // time for one frame
    private static double d = 0; // current delta time
    public static ArrayAverage tPerformanceAverage;

    static {

        initialize();

    }

    public static void initialize() {

        fpsLimit = 75;
        tTick = 1_000_000_000 / fpsLimit;
        tPerformanceAverage = new ArrayAverage(fpsLimit);

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

    public static void setScreenResolution(int screenWidth, int screenHeight) {

        if (screenWidth < 1) {
            screenWidth = 1;
        }

        if (screenHeight < 1) {
            screenHeight = 1;
        }

        Client.width = screenWidth;
        Client.height = screenHeight;
        Client.screenMax = max(screenWidth, screenHeight);

    }

    public static void setZoom(double zoom) {

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

    public static void setPlayerPosition(double playerX, double playerY) {

        Client.playerX = playerX;
        Client.playerY = playerY;

    }

    public static void setPlayerRadians(double playerRadians) {

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

    public static void setGX(double gX) {

        Client.gX = gX;

    }

    public static void setGY(double gY) {

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

    public static void setFpsLimit(int fpsLimit) {

        if (fpsLimit < 1) {
            fpsLimit = 1;
        }

        Client.fpsLimit = fpsLimit;

    }

    public static void setT(long t) {

        Client.t = t;

    }

    public static void setD(double d) {

        Client.d = d;

    }

    // Getters:

    public static String getAuthor() {

        return AUTHOR;

    }

    public static String getTitle() {

        return TITLE;

    }

    public static String getVersion() {

        return VERSION;

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

    public static int getWidth() {

        return width;

    }

    public static int getHeight() {

        return height;

    }

    public static int getScreenMax() {

        return screenMax;

    }

    public static double getZoom() {

        return zoom;

    }

    public static Actor getPlayer() {

        return player;

    }

    public static double getPlayerX() {

        return playerX;

    }

    public static double getPlayerY() {

        return playerY;

    }

    public static double getPlayerRadians() {

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

    public static double getGX() {

        return gX;

    }

    public static double getGY() {

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

    public static int getFpsLimit() {

        return fpsLimit;

    }

    public static long getT() {

        return t;

    }

    public static double getTTick() {

        return tTick;

    }

    public static double getD() {

        return d;

    }

}
