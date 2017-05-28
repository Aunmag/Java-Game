package sprites;

import client.Client;
import managers.ImageManager;
import managers.MathManager;
import sprites.basics.BasePosition;

/**
 * Abstract class which is base to level sprites, like objects, actors, weapons etc. It has its own
 * position on level, image, angle and body collision. An unique sprite has its own image but a non
 * unique sprite has common image (see more in ImageManager class).
 *
 * Created by Aunmag on 2016.09.24.
 */

public abstract class Sprite extends BasePosition {

    protected boolean isValid = true;
    protected ImageManager image;

    Sprite(float x, float y, float radians, boolean isImageUnique, String imagePath) {
        this.x = x;
        this.y = y;
        this.radians = radians;
        initializeImage(imagePath, isImageUnique);
    }

    private void initializeImage(String imagePath, boolean isImageUnique) {
        // TODO: Overwrite
        if (imagePath == null) {
            image = null;
        } else if (isImageUnique) {
            image = new ImageManager(imagePath);
        } else {
            // TODO: Simplify
            image = ImageManager.commonImages.get(imagePath);
            if (image == null) {
                // Initialize new common image if it it doesn't exist yet, then set it for sprite
                image = new ImageManager(imagePath);
                ImageManager.commonImages.put(imagePath, image);
            }
        }
    }

    public void update() {}

    public void render() {
        if (image == null || !calculateIsVisible()) {
            return;
        }

        image.setRadians(radians);

        int onScreenX = (int) (x - image.rotatedOffsetX - Client.getGX());
        int onScreenY = (int) (y - image.rotatedOffsetY - Client.getGY());
        Client.getG().drawImage(image.getRotated(), onScreenX, onScreenY, null);
    }

    boolean calculateIsVisible() {
        float cameraX = Client.getCameraX();
        float cameraY = Client.getCameraY();
        float distanceBetween = MathManager.calculateDistanceBetween(x, y, cameraX, cameraY); // TODO: Use BasePoint
        return distanceBetween < Client.getCameraVisibility();
    }

    /* Getters */

    public boolean getIsValid() {
        return isValid;
    }

}
