package gui.components;

import client.Display;
import client.input.Input;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Aunmag on 2016.11.17.
 */

public class GuiButton extends GuiLabel {

    private static final Color colorDefault = new Color(128, 128, 128, 140);
    private static final Color colorTouched = new Color(166, 166, 166, 180);
    private static final Color fontColorUnavailable = new Color(204, 204, 204, 128);
    private boolean isAvailable = true;
    private boolean isTouched = false;
    private boolean isPressed = false;
    private Runnable action;

    public GuiButton(int x, int y, int width, int height, int grid, String text, Runnable action) {
        super(x, y, width, height, grid, 24, true, text);
        this.action = action;
    }

    public void update() {
        if (!isAvailable) {
            return;
        }

        int mouseX = Input.getMouseX();
        int mouseY = Input.getMouseY();
        isTouched = (getX() < mouseX && mouseX < getX() + width) && (getY() < mouseY && mouseY < getY() + height);
        isPressed = isTouched && Input.getIsButtonJustPressed(MouseEvent.BUTTON1);

        if (isPressed && action != null) {
            try {
                action.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void render() {
        if (isTouched) {
            Display.getGraphicsHud().setColor(colorTouched);
        } else {
            Display.getGraphicsHud().setColor(colorDefault);
        }

        Display.getGraphicsHud().fillRect((int) getX(), (int) getY(), (int) width, (int) height);
        super.render();
    }

    /* Setters */

    public void setIsAvailable(boolean isAvailable) {
        if (isAvailable == this.isAvailable) {
            return;
        } else {
            this.isAvailable = isAvailable;
        }

        if (!isAvailable) {
            isTouched = false;
            isPressed = false;
            text.setColor(fontColorUnavailable);
        } else {
            text.setColor(Color.WHITE);
        }
    }

}
