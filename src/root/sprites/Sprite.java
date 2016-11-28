package root.sprites;

// Created by Aunmag on 25.09.2016.

import java.awt.*;

public abstract class Sprite {

    public boolean isValid = true;
    protected final boolean isUnique;
    protected boolean isDenoted;

    protected static final Color bodyColor = new Color(255, 255, 255, 100);
    public int bodyRadius = 6;
    public boolean isBodyRadiusVisible = false;

    public double x;
    public double y;
    protected double degrees;
    protected double radians;

    protected final Image image;
    protected final String imagePath;

    public Sprite(double x, double y, double degrees, boolean isUnique, String imagePath) {

        this.x = x;
        this.y = y;
        setDegrees(degrees);
        this.isUnique = isUnique;
        this.imagePath = imagePath;

        if (imagePath.isEmpty()) {
            image = null;
        } else if (isUnique) {
            image = new Image(imagePath);
        } else {
            Image.globalImages.put(imagePath, new Image(imagePath));
            image = Image.globalImages.get(imagePath);
        }

        isDenoted = false;

    }

    // Setters:

    public void setDegrees(double degrees) {

        this.degrees = degrees;
        this.radians = Math.toRadians(this.degrees);

    }

    public void setRadians(double radians) {

        this.radians = radians;
        this.degrees = Math.toDegrees(this.radians);

    }

    // Getters:

    public double getDegrees() {

        return degrees;

    }

    public double getRadians() {

        return radians;

    }

    // Updaters:

    public abstract void tick();

    public void render(Graphics g, double xOffset, double yOffset) {

        if (image == null) {
            return;
        }

        int displayX = (int) (x - image.rotatedOffsetX - xOffset);
        int displayY = (int) (y - image.rotatedOffsetY - yOffset);
        g.drawImage(image.get(radians), displayX, displayY, null);

    }

}
