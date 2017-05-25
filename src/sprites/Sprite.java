package sprites;

import client.Client;
import client.Constants;
import managers.ImageManager;
import managers.MathManager;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Abstract class which is base to level sprites, like objects, actors, weapons etc. It has its own position on level,
 * image, angle and body collision. An unique sprite has its own image but a non unique sprite has common image (see
 * more in ImageManager class).
 *
 * Created by Aunmag on 24.09.2016.
 */

public abstract class Sprite {

    // Sprite:
    public boolean isValid = true; // define should the sprite be valid or deleted either
    ImageManager image; // image of sprite

    // Body (uses for collision):
    boolean hasBody = false;
    public float bodyRadius; // radius of round body
    static boolean isBodyRadiusVisible = false; // use for tests to see body round on the screen
    static final Color bodyColor = new Color(255, 255, 255, 100); // color of body round (for tests)

    // Position:
    public float x;
    public float y;
    float displayX;
    float displayY;
    float radians;

    // Physics:
    float velocity = 0;

    Sprite(float x, float y, float radians, boolean isImageUnique, String imagePath) {

        // Set position:
        this.x = x;
        this.y = y;
        setRadians(radians);

        // Set image:
        setImage(imagePath, isImageUnique);

    }

    boolean IsVisible() {

        /*
         * Test is sprite close enough to the camera and isn't beyond screen.
         */

        // Distance between sprite and camera:
        float distanceBetween = (float) (Math.sqrt(Math.pow(x - Client.getCameraX(), 2) + Math.pow(y - Client.getCameraY(), 2)));

        // Return false if the distance is grater than maximal allowed:
        return distanceBetween < Client.getCameraVisibility();

    }

    // Setters:

    public void setImage(String imagePath, boolean isImageUnique) {

        /*
         * Get image from loaded images and set it as sprite's own.
         */

        if (imagePath.isEmpty()) {
            /* Disable image if there's no path. */
            image = null;
        } else if (!isImageUnique) {
            /* Get and set common image if sprite isn't unique. */
            image = ImageManager.commonImages.get(imagePath); // get common image from set of common images
            if (image == null) {
                /* Initialize new common image if it it doesn't exist yet. Then set it for sprite. */
                image = new ImageManager(imagePath); // load common image if it doesn't exist yet
                ImageManager.commonImages.put(imagePath, image); // set loaded image in common set
            }
        } else {
            /* Initialize new image for sprite if it is unique. */
            image = new ImageManager(imagePath);
        }

    }

    public void setRadians(float radians) {
        this.radians = MathManager.correctRadians(radians);
    }

    // Getters:

    public float getRadians() {

        return radians;

    }

    // Useful:

    void displayPositionPrepare() {

        // Put sprite according to camera position:
        displayX = x - Client.getGX();
        displayY = y - Client.getGY();

        // Put sprite according to its image center (see more in ImageManager class):
        if (image != null) {
            displayX -= image.rotatedOffsetX;
            displayY -= image.rotatedOffsetY;
        }

    }

    // Updaters:

    public void tick() {}

    public void render() {

        // Test has sprite image or is sprite visible:
        if (image == null || !IsVisible()) {
            /* Abort rendering if image is invisible at all. */
            return;
        }

        // Update and prepare image:
        BufferedImage imageUpdated = image.get(radians);

        // Put sprite according to camera position and its image center (see more in ImageManager class):
        int displayX = (int) (x - image.rotatedOffsetX - Client.getGX());
        int displayY = (int) (y - image.rotatedOffsetY - Client.getGY());

        // Render image:
        Client.getG().drawImage(imageUpdated, displayX, displayY, null);

    }

}
