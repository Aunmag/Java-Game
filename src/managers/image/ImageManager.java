package managers.image;

import client.Settings;
import managers.Log;

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
 * ### Image types:
 * - Original is the dry image without any effects. It let make its copy and change them without
 * quality lose.
 * - Rotated is the wet copy of original image with rotating any some effects.
 *
 * ### Terms:
 * - Common image - the image which is used a number of times in a moment. For instance a ground
 * image is copied in several positions and there's no need to make for every position a new image.
 * That is a general image no has its copies but is put in several position. This saves performance
 * and memory. It also has two forms: original and rotated. There is the special set of general
 * images in this class.
 * - ImageManager offset - a half of image width (offset x) and height (offset y) which is usually
 * used to find the image center. It allows put sprite right in its position according to image
 * size.
 *
 * Created by Aunmag on 2016.09.24.
 */

public class ImageManager {

    private static Map<String, BufferedImage> imagesCached = new HashMap<>();
    public static Map<String, ImageManager> imagesCommon = new HashMap<>();

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

    public static ImageManager getCommonImage(String path) {
        if (path == null) {
            return null;
        }

        ImageManager imageCommon = ImageManager.imagesCommon.get(path);

        if (imageCommon == null) {
            imageCommon = new ImageManager(path);
            ImageManager.imagesCommon.put(path, imageCommon);
        }

        return imageCommon;
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
