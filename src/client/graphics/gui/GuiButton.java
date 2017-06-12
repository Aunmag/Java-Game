package client.graphics.gui;

// Created by Aunmag on 17.11.2016.

import client.Display;
import client.input.Input;

import java.awt.*;
import java.awt.event.MouseEvent;

public class GuiButton {

    // Colors:
    private static final Color colorDefault = new Color(153, 153, 153);
    private static final Color colorTouched = new Color(102, 102, 102);
    private static final Color colorUnavailable = new Color(204, 204, 204);

    // Position:
    private int x;
    private int y;
    private int width;
    private int height;

    // States:
    private boolean isAvailable = true;
    private boolean isTouched = false;
    private boolean isPressed = false;

    // Text:
    GuiLabel guiLabel;
    private static final int fontSize = 24;
    private static final Font font = new Font("Arial", Font.BOLD, fontSize);
    private int textX;
    private int textY;
    private String text;

    public GuiButton(int x, int y, int width, int height, String text) {

        // Set rectangle:
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // Set text:

        guiLabel = new GuiLabel(x + width / 2, y + height / 2, 24, true, text);

        Display.getGraphicsHud().setFont(font);
        int textWidth = Display.getGraphicsHud().getFontMetrics().stringWidth(text);

        this.text = text;
        textX = (Display.getWidth() - textWidth) / 2;
        textY = y + (int) (height - fontSize / 1.5);

    }

    public void tick() {

        if (!isAvailable) {
            return;
        }

        int mouseX = Input.getMouseX();
        int mouseY = Input.getMouseY();
        boolean isTouchedX = x < mouseX && mouseX < x + width;
        boolean isTouchedY = y < mouseY && mouseY < y + height;

        isTouched = isTouchedX && isTouchedY;
        isPressed = isTouched && Input.getIsButtonJustReleased(MouseEvent.BUTTON1);

    }

    public void render() {

        // Rectangle color:
        Color color;
        if (isTouched) {
            color = colorTouched;
        } else {
            color = colorDefault;
        }

        // Render rectangle:
        Display.getGraphicsHud().setColor(color);
        Display.getGraphicsHud().fillRect(x, y, width, height);

        // Render text:
        guiLabel.render();

    }

    // Setters:

    public void setAvailable(boolean available) {

        isAvailable = available;

        if (!isAvailable) {
            isTouched = false;
            isPressed = false;
            guiLabel.setColor(new Color(204, 204, 204));
        } else {
            guiLabel.setColor(Color.WHITE);
        }

    }

    // Getters:

    public boolean isAvailable() {

        return isAvailable;

    }

    public boolean isTouched() {

        return isTouched;

    }

    public boolean isPressed() {

        return isPressed;

    }

}
