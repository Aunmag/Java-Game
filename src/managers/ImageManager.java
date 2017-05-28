package managers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the image manager which find, load, store and handle images.
 *
 * An image has two forms:
 * - Original is the dry image without any effects. It let make its copy and change them without quality lose.
 * - Rotated is the wet copy of original image with rotating any some effects.
 *
 * Terms:
 * - Common image - the image which is used a number of times in a moment. For instance a ground image is copied in
 * several positions and there's no need to make for every position a new image. That is a general image no has its
 * copies but is put in several position. This saves performance and memory. It also has two forms: original and rotated
 * There is the special set of general images in this class.
 * - ImageManager offset - a half of image width (offset x) and height (offset y) which is usually used to find the image
 * center. It allows put sprite right in its position according to image size.
 *
 * Created by Aunmag on 24.09.2016.
 */

public class ImageManager {

    private static Map<String, BufferedImage> cachedImages = new HashMap<>();
    public static Map<String, ImageManager> commonImages = new HashMap<>(); // common images set
    public static int renderingQuality = 1; // use more complex and high quality rotate method (0, 1, 2)
    private static final String pathPrefix = "/images/"; // paren folder of the all images

    // Original image data:
    private BufferedImage original;
    private final int originalWidth;
    private final int originalHeight;
    public final float originalOffsetX;
    public final float originalOffsetY;

    // Changed image data:
    private BufferedImage rotated;
    public float rotatedOffsetX;
    public float rotatedOffsetY;
    private float radians = 0; // rotation angle

    public ImageManager(String path) {

        // Load image:

        original = cachedImages.get(path);

        // Initialize image data:

        if (original != null) {
            originalWidth = original.getWidth();
            originalHeight = original.getHeight();
            originalOffsetX = originalWidth / 2;
            originalOffsetY = originalHeight / 2;
            rotate(); // first initialization of rotated image
        } else {
            originalWidth = 0;
            originalHeight = 0;
            originalOffsetX = 0;
            originalOffsetY = 0;
        }

    }

    public static void cacheImages() {

        String[] allPaths = {
            "actors/human.png",
            "actors/zombie.png",
            "objects/ground/grass.png",
            "objects/ground/bluff_0.png",
            "objects/ground/bluff_90.png",
            "objects/ground/bluff_180.png",
            "objects/ground/bluff_270.png",
            "objects/ground/bluff_a0.png",
            "objects/ground/bluff_a90.png",
            "objects/ground/bluff_a180.png",
            "objects/ground/bluff_a270.png",
            "objects/air/tree_1.png",
            "objects/air/tree_2.png",
            "objects/air/tree_3.png",
            "weapons/mp_27.png",
        };

        for (String path: allPaths) {
            try {
                cachedImages.put(path, ImageIO.read(ImageManager.class.getResource(pathPrefix + path)));
            } catch (IOException | IllegalArgumentException e) {
                Log.log("ImageManager error", "Can't cache \"" + pathPrefix + path + "\" image.", e);
            }
        }

    }

    private void rotate() {

        // New width and height:
        int w = originalWidth * 2;
        int h = originalHeight * 2;

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        rotated = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
        Graphics2D g = rotated.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

        if (renderingQuality == 1) {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        } else if (renderingQuality == 2) {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        } else {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        }

        AffineTransform at = AffineTransform.getTranslateInstance((w - originalWidth) / 2, (h - originalHeight) / 2);
        at.rotate(radians, originalOffsetX, originalOffsetY);
        g.drawRenderedImage(original, at);
        g.dispose();

        rotatedOffsetX = rotated.getWidth() / 2;
        rotatedOffsetY = rotated.getHeight() / 2;

    }

    /* Getters */

    public BufferedImage getRotated() {
        return rotated;
    }

    /* Setters */

    public void setRadians(float radians) {
        if (this.radians == radians) {
            return;
        }

        this.radians = radians;
        rotate();
    }
}
