package sprites;

import client.Camera;
import client.Display;
import managers.ImageManager;
import basics.BasePoint;
import basics.BasePosition;

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
        super(x, y, radians);
        this.image = image;
    }

    public void update() {}

    public void render() {
        if (image == null || !calculateIsVisible()) {
            return;
        }

        image.setRadians(radians);

        BasePoint onScreenPosition = Camera.calculateOnScreenPosition(this);
        int onScreenX = (int) (onScreenPosition.getX() - image.getCenterX());
        int onScreenY = (int) (onScreenPosition.getY() - image.getCenterY());
        Display.getGraphics().drawImage(image.getImage(), onScreenX, onScreenY, null);
    }

    public abstract void delete();

    boolean calculateIsVisible() {
        return Camera.calculateIsPointVisible(this);
    }

    /* Getters */

    public boolean getIsValid() {
        return isValid;
    }

}
