package client.graphics.gui;

// Created by Aunmag on 17.11.2016.

import client.DataManager;
import client.Constants;
import client.states.GamePlay;
import managers.SoundManager;

public class MenuMain extends MenuAbstract {

    private GuiButton buttonContinue;
    private GuiButton buttonNewGame;
    private GuiButton buttonHelp;
    private GuiButton buttonExit;

    private GuiLabel labelA;
    private GuiLabel labelB;

    private SoundManager soundscape;

    public MenuMain() {

        super("/images/wallpapers/main_menu.png");

        int x;
        int y;
        int height12Fold = DataManager.getDisplayHeight() / 12;

        int width = 400;
        int height = 50;
        int margin = 5;

        x = (DataManager.getDisplayWidth() - width) / 2;
        y = height + margin;

        int quantity = 4;
        int offsetY = height12Fold * 8 - (y * quantity) / 2;

        labelA = new GuiLabel(DataManager.getDisplayWidth() / 2, height12Fold * 4, 48, true, Constants.TITLE);
        labelB = new GuiLabel(DataManager.getDisplayWidth() / 2, height12Fold * 5, 16, false, "v" + Constants.VERSION);

        buttonContinue = new GuiButton(x, offsetY + y, width, height, "Continue");
        buttonContinue.setAvailable(false);
        buttonNewGame = new GuiButton(x, offsetY + y * 2, width, height, "New game");
        buttonHelp = new GuiButton(x, offsetY + y * 3, width, height, "Help");
        buttonExit = new GuiButton(x, offsetY + y * 4, width, height, "Exit");

        soundscape = new SoundManager("/sounds/music/menu.wav");
        soundscape.loop();

    }

    public void tick() {

        // Continue button:
        buttonContinue.setAvailable(DataManager.isGameStarted());
        buttonContinue.tick();
        if (buttonContinue.isPressed()) {
            soundscape.stop();
            GamePlay.activate();
        }

        // New game button:
        buttonNewGame.tick();
        if (buttonNewGame.isPressed()) {
            soundscape.stop();
            GamePlay.initialize();
            GamePlay.activate();
        }

        // Help button:
        buttonHelp.tick();
        if (buttonHelp.isPressed()) {
            DataManager.getGameMenu().activeMenuHelp();
        }

        // Exit button:
        buttonExit.tick();
        if (buttonExit.isPressed()) {
            DataManager.getGameMenu().activeMenuExit();
        }

    }

    public void render() {

        renderWallpaper();

        buttonContinue.render();
        buttonNewGame.render();
        buttonHelp.render();
        buttonExit.render();

        labelA.render();
        labelB.render();

    }

    // Getters:

    public SoundManager getSoundscape() {

        return soundscape;

    }

}
