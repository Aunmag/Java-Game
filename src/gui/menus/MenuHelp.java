package gui.menus;

// Created by Aunmag on 20.11.2016.

import client.Display;
import gui.components.GuiButton;
import gui.components.GuiText;

public class MenuHelp extends Menu {

    public MenuHelp() {

        super("/images/wallpapers/help.png");

        int x;
        int y;
        int height12Fold = Display.getHeight() / 12;

        // Title:
        GuiText labelTitle = new GuiText(Display.getWidth() / 2, height12Fold * 2, 36, true, "Help");
        guiTexts.add(labelTitle);

        // Help mark:
        int x1 = Display.getWidth() / 3;
        int x2 = x1 * 2;
        y = Display.getHeight() / 16;
        int helpSize = 16;

        // Help:

        GuiText labelMovementA = new GuiText(x1, y * 5, helpSize, false, "Movement");
        guiTexts.add(labelMovementA);
        GuiText labelMovementB = new GuiText(x2, y * 5, helpSize, false, "W, A, S, D");
        guiTexts.add(labelMovementB);

        GuiText labelRotationA = new GuiText(x1, y * 6, helpSize, false, "Rotation");
        guiTexts.add(labelRotationA);
        GuiText labelRotationB = new GuiText(x2, y * 6, helpSize, false, "Mouse");
        guiTexts.add(labelRotationB);

        GuiText labelToRunA = new GuiText(x1, y * 7, helpSize, false, "Sprint");
        guiTexts.add(labelToRunA);
        GuiText labelToRunB = new GuiText(x2, y * 7, helpSize, false, "Shift");
        guiTexts.add(labelToRunB);

        GuiText labelAttackA = new GuiText(x1, y * 8, helpSize, false, "Attack");
        guiTexts.add(labelAttackA);
        GuiText labelAttackB = new GuiText(x2, y * 8, helpSize, false, "LMB");
        guiTexts.add(labelAttackB);

        GuiText labelZoomA = new GuiText(x1, y * 9, helpSize, false, "Zoom in/out");
        guiTexts.add(labelZoomA);
        GuiText labelZoomB = new GuiText(x2, y * 9, helpSize, false, "+/- or Wheel Up/Down");
        guiTexts.add(labelZoomB);

        GuiText labelMenuA = new GuiText(x1, y * 10, helpSize, false, "Menu");
        guiTexts.add(labelMenuA);
        GuiText labelMenuB = new GuiText(x2, y * 10, helpSize, false, "Escape");
        guiTexts.add(labelMenuB);

        // Button:
        int width = 400;
        int height = 50;
        x = (Display.getWidth() - width) / 2;
        y = (height12Fold * 8 - height / 2) + height * 2;
        Runnable action = () -> MenuManager.getMenuMain().activate();
        GuiButton buttonBack = new GuiButton(x, y, width, height, "Back", action);
        allButtons.add(buttonBack);

    }

    public void deactivate() {}


}
