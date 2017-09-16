package gui.components;

import client.Display;
import nightingale.basics.BaseQuad;

/**
 * Created by Aunmag on 2017.06.17.
 */

public class GuiLabel extends BaseQuad {

    private static final int padding = 2;
    protected GuiText text;

    public GuiLabel(
            int x, int y, int width, int height, int grid,
            int fontSize, boolean isFontBold, String text
    ) {
        super(
                // TODO: Optimize
                x * (Display.getWidth() / grid) + padding,
                y * (Display.getHeight() / grid) + padding,
                width * (Display.getWidth() / grid) - padding * 2,
                height * (Display.getHeight() / grid) - padding * 2
        );

        initializeText(text, fontSize, isFontBold);
    }

    private void initializeText(String text, int size, boolean isBold) {
        float x = getX() + width / 2f;
        float y = getY() + height / 2f;
        this.text = new GuiText((int) x, (int) y, size, isBold, text);
    }

    public void render() {
        text.render();
    }

    /* Setters */

    public void setText(String text) {
        initializeText(text, 16, false);
    }

}
