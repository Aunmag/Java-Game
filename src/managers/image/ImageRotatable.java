package managers.image;

import client.Settings;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by Aunmag on 2017.06.02.
 */

public class ImageRotatable {

    private BufferedImage image;
    private final BufferedImage imageOriginal;
    private float radians;
    private float centerX;
    private float centerY;

    public ImageRotatable(BufferedImage imageOriginal) {
        this.imageOriginal = imageOriginal;
        setRadians(0);
    }

    public ImageRotatable(BufferedImage imageOriginal, float radians) {
        this.imageOriginal = imageOriginal;
        setRadians(radians);
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

    public void setRadians(float radians) {
        if (this.radians == radians) {
            return;
        }

        this.radians = radians;

        if (imageOriginal != null) {
            rotate();
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
