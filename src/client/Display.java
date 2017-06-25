package client;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * Created by Aunmag on 2017.06.12.
 */

public class Display {

    private static final boolean isFullscreen = true;
    private static int width;
    private static int height;
    private static int maximal;
    private static float halfWidth;
    private static float halfHeight;
    private static float halfMaximal;
    private static boolean isCursorVisible = true;

    private static JFrame frame;
    private static Canvas canvas;
    private static BufferStrategy bufferStrategy;
    private static Graphics2D graphics;
    private static Graphics2D graphicsHud;
    private static Cursor cursorBlank;

    static {
        initialize();
        initializeCursorBlank();
        prepareBufferStrategy();
        prepareGraphicsHud();
    }

    private static void initialize() {
        Dimension resolution;

        if (isFullscreen) {
            resolution = Toolkit.getDefaultToolkit().getScreenSize();
        } else {
            resolution = new Dimension(1280, 720);
        }

        setResolution((int) resolution.getWidth(), (int) resolution.getHeight());

        String title = String.format(
                "%s v%s by %s",
                Constants.TITLE,
                Constants.VERSION,
                Constants.DEVELOPER
        );
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        if (isFullscreen) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        frame.setVisible(true);

        canvas = new Canvas();
        canvas.setPreferredSize(resolution);
        canvas.setMaximumSize(resolution);
        canvas.setMinimumSize(resolution);
        canvas.setFocusable(false);
        frame.add(canvas);
        frame.pack();
        canvas.createBufferStrategy(2);
    }

    private static void initializeCursorBlank() {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        cursorBlank = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "");
    }

    static void prepareBufferStrategy() {
        bufferStrategy = canvas.getBufferStrategy();
    }

    static void prepareGraphics() {
        graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
    }

    static void prepareGraphicsHud() {
        graphicsHud = (Graphics2D) bufferStrategy.getDrawGraphics();
        graphicsHud.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphicsHud.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        graphicsHud.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        graphicsHud.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        graphicsHud.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        graphicsHud.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        graphicsHud.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }

    /* Setters */

    static void setResolution(int width, int height) {
        if (width < 1) {
            // TODO: Log
            width = 1;
        } else if (height < 1) {
            // TODO: Log
            height = 1;
        }

        Display.width = width;
        Display.height = height;
        maximal = Math.max(width, height);
        halfWidth = width / 2f;
        halfHeight = height / 2f;
        halfMaximal = maximal / 2f;
    }

    public static void setIsCursorVisible(boolean isCursorVisible) {
        if (isCursorVisible == Display.isCursorVisible) {
            return;
        } else {
            Display.isCursorVisible = isCursorVisible;
        }

        if (isCursorVisible) {
            frame.getContentPane().setCursor(Cursor.getDefaultCursor());
        } else {
            frame.getContentPane().setCursor(cursorBlank);
        }
    }

    /* Getters */

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static int getMaximal() {
        return maximal;
    }

    public static float getHalfWidth() {
        return halfWidth;
    }

    public static float getHalfHeight() {
        return halfHeight;
    }

    public static float getHalfMaximal() {
        return halfMaximal;
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static Canvas getCanvas() {
        return canvas;
    }

    public static BufferStrategy getBufferStrategy() {
        return bufferStrategy;
    }

    public static Graphics2D getGraphics() {
        return graphics;
    }

    public static Graphics2D getGraphicsHud() {
        return graphicsHud;
    }

}
