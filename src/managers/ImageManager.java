package managers;

import client.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the image manager which find, load, store and handle images.
 *
 * Created by Aunmag on 2016.09.24.
 */

public class ImageManager {

    private static Map<String, BufferedImage> imagesCached = new HashMap<>();

    private BufferedImage image;
    private final BufferedImage imageOriginal;
    private float radians;
    private float centerX;
    private float centerY;

    static {
        cacheImages();
    }

    private static void cacheImages() {
        String pathPrefix = "/images/";
        String pathPostfix = ".png";

        String[] paths = {
            "actors/human",
            "actors/zombie",
            "objects/ground/grass",
            "objects/ground/bluff_0",
            "objects/ground/bluff_90",
            "objects/ground/bluff_180",
            "objects/ground/bluff_270",
            "objects/ground/bluff_a0",
            "objects/ground/bluff_a90",
            "objects/ground/bluff_a180",
            "objects/ground/bluff_a270",
            "objects/air/tree_1",
            "objects/air/tree_2",
            "objects/air/tree_3",
            "weapons/mp_27",
        };

        for (String path: paths) {
            try {
                String pathFull = pathPrefix + path + pathPostfix;
                URL url = ImageManager.class.getResource(pathFull);
                BufferedImage image = ImageIO.read(url);
                imagesCached.put(path, image);
            } catch (IOException | IllegalArgumentException e) {
                String message = String.format("Image manager can't cache image at \"%s\".", path);
                Log.log("Error", message, e.toString());
            }
        }
    }

    public ImageManager(String path) {
        imageOriginal = imagesCached.get(path);
        setRadiansDirectly(0);
    }

    private void rotate() {
        int width = imageOriginal.getWidth() * 2;
        int height = imageOriginal.getHeight() * 2;

        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();

        image = graphicsConfiguration.createCompatibleImage(
                width,
                height,
                Transparency.TRANSLUCENT
        );

        Graphics2D graphics = image.createGraphics();
        Settings.applyGraphicsSettings(graphics);

        AffineTransform affineTransform = AffineTransform.getTranslateInstance(
                (width - imageOriginal.getWidth()) / 2,
                (height - imageOriginal.getHeight()) / 2
        );

        float imageOriginalCenterX = imageOriginal.getWidth() / 2;
        float imageOriginalCenterY = imageOriginal.getHeight() / 2;
        affineTransform.rotate(radians, imageOriginalCenterX, imageOriginalCenterY);
        graphics.drawRenderedImage(imageOriginal, affineTransform);
        graphics.dispose();

        centerX = image.getWidth() / 2;
        centerY = image.getHeight() / 2;
    }

    private void setRadiansDirectly(float radians) {
        this.radians = radians;

        if (imageOriginal != null) {
            rotate();
        }
    }

    public void setRadians(float radians) {
        if (this.radians != radians) {
            setRadiansDirectly(radians);
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

}
