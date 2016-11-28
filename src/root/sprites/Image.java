package root.sprites;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

// Created by Aunmag on 24.09.2016.

/**
 * ### Learn
 *
 * - ImageIO
 * - AffineTransform (java.awt.geom)
 * - BufferedImage (java.awt.image)
 * - HashMap (java.util)
 * - Map (java.util)
 *
 * */

public class Image {

    static Map<String, Image> globalImages = new HashMap<>();
    public static boolean highQuality = true;

    private BufferedImage original;
    private final int originalWidth;
    private final int originalHeight;
    private final double originalOffsetX;
    private final double originalOffsetY;

    private BufferedImage rotated;
    public double rotatedOffsetX;
    public double rotatedOffsetY;

    private double radians;

    Image(String path) {

        try {

            String url = "images/" + path;
            original = ImageIO.read(getClass().getClassLoader().getResource(url));

//            URL url = Image.class.getResource("/images/" + path);
//            original = ImageIO.read(url);

//            original = ImageIO.read(Image.class.getResource("/images/" + path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        originalWidth = original.getWidth();
        originalHeight = original.getHeight();
        originalOffsetX = originalWidth / 2;
        originalOffsetY = originalHeight / 2;

        update(0);

    }

    private BufferedImage rotate() {

        double newWidth = originalWidth * 2;
        double newHeight = originalHeight * 2;

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        BufferedImage result = gc.createCompatibleImage((int) newWidth, (int) newHeight, Transparency.TRANSLUCENT);

        Graphics2D g = result.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON) ;
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC) ;
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY) ;

        AffineTransform at = AffineTransform.getTranslateInstance((newWidth - originalWidth) / 2, (newHeight - originalHeight) / 2);
        at.rotate(radians, originalWidth / 2, originalHeight / 2);

        g.drawRenderedImage(original, at);
        g.dispose();

        return result;

    }

    private void update(double newRadians) {

        radians = newRadians;

        if (highQuality) {
            rotated = rotate();
        } else {
            rotated = new BufferedImage(originalWidth * 2, originalHeight * 2, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) rotated.getGraphics();
            g2.rotate(radians, originalWidth, originalHeight);
            g2.drawImage(original, (int) originalOffsetX, (int) originalOffsetY, null);
        }

        rotatedOffsetX = rotated.getWidth() / 2;
        rotatedOffsetY = rotated.getHeight() / 2;

    }

    public BufferedImage get(double newRadians) {

        if (radians != newRadians) {
            update(newRadians);
        }

        return rotated;

    }

}
