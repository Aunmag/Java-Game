package sprites;

import client.Client;
import managers.image.ImageManager;
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

    Sprite(float x, float y, float radians, ImageManager image) {
        this.x = x;
        this.y = y;
        this.radians = radians;
        this.image = image;
    }

    public void update() {}

    public void render() {
        if (image == null || !calculateIsVisible()) {
            return;
        }

        image.setRadians(radians);

        int onScreenX = (int) (x - image.getCenterX() - Client.getGX());
        int onScreenY = (int) (y - image.getCenterY() - Client.getGY());
        Client.getG().drawImage(image.getImage(), onScreenX, onScreenY, null);
    }

    public abstract void delete();

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
