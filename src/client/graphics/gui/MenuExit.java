package client.graphics.gui;

// Created by Aunmag on 20.11.2016.

import client.DataManager;

public class MenuExit extends MenuAbstract {

    private GuiButton buttonBack;
    private GuiButton buttonExit;
    private GuiLabel labelQuestion;

    public MenuExit() {

        super("/images/wallpapers/exit.png");

        int x;
        int y;
        int height12Fold = DataManager.getDisplayHeight() / 12;

        int width = 400;
        int height = 50;
        int margin = 5;

        x = (DataManager.getDisplayWidth() - width) / 2;
        y = height + margin;

        int quantity = 2;
        int offsetY = height12Fold * 8 - (y * quantity) / 2;

        labelQuestion = new GuiLabel(DataManager.getDisplayWidth() / 2, height12Fold * 4, 36, true, "Are you sure you want to exit?");


        buttonExit = new GuiButton(x, offsetY + y, width, height, "Yes");
        buttonBack = new GuiButton(x, offsetY + y * 2, width, height, "No");

    }

    public void tick() {

        // Back button:
        buttonBack.tick();
        if (buttonBack.isPressed()) {
            DataManager.getGameMenu().activeMenuMain();
        }

        // Exit button:
        buttonExit.tick();
        if (buttonExit.isPressed()) {
            DataManager.setIsRunning(false);
        }

    }

    public void render() {

        renderWallpaper();
        labelQuestion.render();
        buttonBack.render();
        buttonExit.render();

    }

}
