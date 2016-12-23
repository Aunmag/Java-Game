package client.graphics.gui;

// Created by Aunmag on 19.11.2016.

import client.Client;

import java.awt.*;

public class GuiLabel {

    // Position:
    private int x;
    private int y;
    private int width;
    private int height;

    // Text:
    private int size;
    private boolean isBold;
    private static String fontName = "Arial";
    private Font font;
    private String text;

    private Color color = Color.white;

    public GuiLabel(int x, int y, int size, boolean isBold, String text) {

        this.x = x;
        this.y = y;

        this.size = size;
        this.isBold = isBold;

        if (isBold) {
            font = new Font(fontName, Font.BOLD, size);
        } else {
            font = new Font(fontName, Font.PLAIN, size);
        }

        setText(text);

    }

    public void setText(String text) {

        this.text = text;

        Client.getHud().setFont(font);
        width = Client.getHud().getFontMetrics().stringWidth(text);
        height = (int) (size / 2.5);

    }

    public void render() {

        Client.getHud().setFont(font);
        Client.getHud().setColor(color);
        Client.getHud().drawString(text, x - width / 2, y + height);

    }

    public void setColor(Color color) {

        this.color = color;

    }

}