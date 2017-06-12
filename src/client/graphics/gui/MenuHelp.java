package client.graphics.gui;

// Created by Aunmag on 20.11.2016.

import client.DataManager;

public class MenuHelp extends MenuAbstract {

    private GuiButton buttonBack;

    private GuiLabel labelTitle;

    private GuiLabel labelMovementA, labelMovementB;
    private GuiLabel labelRotationA, labelRotationB;
    private GuiLabel labelToRunA, labelToRunB;
    private GuiLabel labelAttackA, labelAttackB;
    private GuiLabel labelZoomA, labelZoomB;
    private GuiLabel labelMenuA, labelMenuB;

    public MenuHelp() {

        super("/images/wallpapers/help.png");

        int x;
        int y;
        int height12Fold = DataManager.getDisplayHeight() / 12;

        // Title:
        labelTitle = new GuiLabel(DataManager.getDisplayWidth() / 2, height12Fold * 2, 36, true, "Help");

        // Help mark:
        int x1 = DataManager.getDisplayWidth() / 3;
        int x2 = x1 * 2;
        y = DataManager.getDisplayHeight() / 16;
        int helpSize = 16;

        // Help:

        labelMovementA = new GuiLabel(x1, y * 5, helpSize, false, "Movement");
        labelMovementB = new GuiLabel(x2, y * 5, helpSize, false, "W, A, S, D");

        labelRotationA = new GuiLabel(x1, y * 6, helpSize, false, "Rotation");
        labelRotationB = new GuiLabel(x2, y * 6, helpSize, false, "Mouse");

        labelToRunA = new GuiLabel(x1, y * 7, helpSize, false, "Sprint");
        labelToRunB = new GuiLabel(x2, y * 7, helpSize, false, "Shift");

        labelAttackA = new GuiLabel(x1, y * 8, helpSize, false, "Attack");
        labelAttackB = new GuiLabel(x2, y * 8, helpSize, false, "LMB");

        labelZoomA = new GuiLabel(x1, y * 9, helpSize, false, "Zoom in/out");
        labelZoomB = new GuiLabel(x2, y * 9, helpSize, false, "+/- or Wheel Up/Down");

        labelMenuA = new GuiLabel(x1, y * 10, helpSize, false, "Menu");
        labelMenuB = new GuiLabel(x2, y * 10, helpSize, false, "Escape");

        // Button:
        int width = 400;
        int height = 50;
        x = (DataManager.getDisplayWidth() - width) / 2;
        y = (height12Fold * 8 - height / 2) + height * 2;
        buttonBack = new GuiButton(x, y, width, height, "Back");

    }

    public void tick() {

        // Back button:
        buttonBack.tick();
        if (buttonBack.isPressed()) {
            DataManager.getGameMenu().activeMenuMain();
        }

    }

    public void render() {

        renderWallpaper();
        labelTitle.render();

        labelMovementA.render();
        labelMovementB.render();
        labelRotationA.render();
        labelRotationB.render();
        labelRotationA.render();
        labelRotationB.render();
        labelToRunA.render();
        labelToRunB.render();
        labelAttackA.render();
        labelAttackB.render();
        labelZoomA.render();
        labelZoomB.render();
        labelMenuA.render();
        labelMenuB.render();

        buttonBack.render();

    }


}
