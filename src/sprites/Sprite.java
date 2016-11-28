package sprites;

import client.Client;
import managers.ImageManager;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Abstract class which is base to level sprites, like objects, actors, weapons etc. It has its own position on level,
 * imageManager, angle and body collision. An unique sprite has its own imageManager but a non unique sprite has common imageManager (see
 * more in ImageManager class).
 *
 * Created by Aunmag on 24.09.2016.
 */

public abstract class Sprite {

    // Sprite:
    public boolean isValid = true; // define should the sprite be valid or deleted either
    ImageManager imageManager; // imageManager of sprite

    // Body (uses for collision):
    boolean hasBody = false;
    public double bodyRadius; // radius of round body
    static boolean isBodyRadiusVisible = false; // use for tests to see body round
    static final Color bodyColor = new Color(255, 255, 255, 100); // color of body round (for tests)

    // Position:
    public double x;
    public double y;
    double degrees;
    double radians;

    Sprite(double x, double y, double degrees, boolean isImageUnique, String imagePath) {

        // Set position:
        this.x = x;
        this.y = y;
        setDegrees(degrees);

        // Set imageManager:
        setImage(imagePath, isImageUnique);

//        allSprites.add(this);

    }

    // Setters:

    public void setImage(String imagePath, boolean isImageUnique) {

        if (imagePath.isEmpty()) {
            imageManager = null; // disable imageManager if there's no path
        } else if (!isImageUnique) {
            imageManager = ImageManager.commonImages.get(imagePath); // get common imageManager from set
            if (imageManager == null) {
                imageManager = new ImageManager(imagePath); // load common imageManager if it doesn't exist yet
                ImageManager.commonImages.put(imagePath, imageManager); // set loaded imageManager in common set
            }
        } else {
            imageManager = new ImageManager(imagePath);
        }

    }

    public void setDegrees(double degrees) {

        // Tweak degrees:
        if (degrees > 360) {
            degrees -= 360;
        } else if (degrees < -360) {
            degrees += 360;
        }

        // Set degrees and update radians respectively:
        this.degrees = degrees;
        this.radians = Math.toRadians(this.degrees);

    }

    public void setRadians(double radians) {

        // Tweak radians:
        if (radians > Math.PI * 2) {
            radians -= Math.PI * 2;
        } else if (radians < -Math.PI * 2) {
            radians += Math.PI * 2;
        }

        // Set radians and update degrees respectively:
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

    boolean IsVisible() {

        // The maximal allowed distance between sprite and player to be visible:
        double distanceBuffer = 192; // additional distance counts the sprite imageManager size
        double distanceMaximal = (Client.getScreenMax() + distanceBuffer) / Client.getZoom(); // count screen size and buffer with zoom

        // Get player position to refer to:
        double x2 = Client.getPlayerX();
        double y2 = Client.getPlayerY();

        // Distance between sprite and player:
        double distanceBetween = Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2));

        // Return false if the distance is grater than maximal allowed:
        if (distanceBetween > distanceMaximal) {
            return false;
        }

        // Buffer offset behind player:
        double x3 = x2 - distanceBuffer * Math.cos(Client.getPlayerRadians());
        double y3 = y2 - distanceBuffer * Math.sin(Client.getPlayerRadians());

        // Radians between sprite and player and their difference:
        double radiansBetween = Math.atan2(y - y3, x - x3);
        double radiansDifference = radiansBetween - Client.getPlayerRadians();

        // Tweak radians value:
        if (radiansDifference > Math.PI) {
            radiansDifference -= Math.PI * 2;
        } else if (radiansDifference < -Math.PI) {
            radiansDifference += Math.PI * 2;
        }

        // Return is sprite in front of player:
        return Math.abs(radiansDifference) < Math.PI / 2;

    }

    public void tick() {}

    public void render() {

        // Test if sprite has an imageManager:
        if (imageManager == null) {
            return;
        }

        // Test if imageManager is visible:
        if (!IsVisible()) {
            return;
        }

        // Update and prepare imageManager:
        BufferedImage imageUpdated = imageManager.get(radians);

        // Put sprite according to camera position and its imageManager center (see more in ImageManager class):
        int displayX = (int) (x - imageManager.rotatedOffsetX - Client.getGX());
        int displayY = (int) (y - imageManager.rotatedOffsetY - Client.getGY());

        // Render imageManager:
        Client.getG().drawImage(imageUpdated, displayX, displayY, null);

    }

}
