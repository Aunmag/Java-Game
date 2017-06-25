package client;

import managers.Log;

import java.awt.*;

/**
 * Created by Aunmag on 2017.06.02.
 */

public class Settings {

    private static SettingsEnum levelGraphics = SettingsEnum.MEDIUM;
    private static Object valueGraphicsAntialiasing;
    private static Object valueGraphicsAlphaInterpolation;
    private static Object valueGraphicsColorRendering;
    private static Object valueGraphicsFractionalmetrics;
    private static Object valueGraphicsRendering;
    private static Object valueGraphicsInterpolation;

    public static void applyGraphicsSettings(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, valueGraphicsAntialiasing);
        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, valueGraphicsAlphaInterpolation);
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, valueGraphicsColorRendering);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, valueGraphicsFractionalmetrics);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, valueGraphicsRendering);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, valueGraphicsInterpolation);
    }

    static {
        initializeGraphics();
    }

    private static void initializeGraphics() {
        valueGraphicsAntialiasing = RenderingHints.VALUE_ANTIALIAS_OFF;
        valueGraphicsAlphaInterpolation = RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED;
        valueGraphicsColorRendering = RenderingHints.VALUE_COLOR_RENDER_SPEED;
        valueGraphicsFractionalmetrics = RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
        valueGraphicsRendering = RenderingHints.VALUE_RENDER_SPEED;

        if (levelGraphics == SettingsEnum.LOW) {
            valueGraphicsInterpolation = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
        } else if (levelGraphics == SettingsEnum.MEDIUM) {
            valueGraphicsInterpolation = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
            valueGraphicsRendering = RenderingHints.VALUE_RENDER_QUALITY;
        } else if (levelGraphics == SettingsEnum.HIGH) {
            valueGraphicsInterpolation = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        } else {
            String message = String.format(
                    "Got unknown level graphics enum as %s. Used default Java settings.",
                    levelGraphics
            );
            Log.log("initializeGraphics", message);
        }
    }

}
