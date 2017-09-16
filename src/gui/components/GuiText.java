package gui.components;

import client.Constants;
import client.Display;
import nightingale.basics.BaseQuad;

import java.awt.*;

/**
 * Created by Aunmag on 2016.11.19.
 */

public class GuiText extends BaseQuad {

    private Color color = Color.WHITE;
    private Font font = Constants.FONT;
    private String message;

    public GuiText(int x, int y, int size, boolean isBold, String message) {
        super(x, y, 0, 0);

        setSize(size);
        if (isBold) {
            setStyle(Font.BOLD);
        }

        setMessage(message);
    }

    public void render() {
        Display.getGraphicsHud().setFont(font);
        Display.getGraphicsHud().setColor(color);
        Display.getGraphicsHud().drawString(message, getX() - width / 2, getY() + height);
    }

    /* Setters */

    public void setSize(int size) {
        font = new Font(font.getFamily(), font.getStyle(), size);
    }

    public void setStyle(int style) {
        font = new Font(font.getFamily(), style, font.getSize());
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setMessage(String message) {
        this.message = message;
        Display.getGraphicsHud().setFont(font);
        width = Display.getGraphicsHud().getFontMetrics().stringWidth(message);
        height = (int) (font.getSize() / 2.5);
    }

}
