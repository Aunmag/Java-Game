package client.graphics.gui;

// Created by Aunmag on 17.11.2016.

import client.Client;
import client.states.GamePlay;
import managers.SoundManager;

public class MenuMain extends MenuAbstract {

    private GuiButton buttonContinue;
    private GuiButton buttonNewGame;
    private GuiButton buttonHelp;
    private GuiButton buttonExit;

//    private String labelText = Client.getTitle() + " v" + Client.getVersion();
    private GuiLabel labelA;
    private GuiLabel labelB;

    private SoundManager soundscape;

    public MenuMain() {

        super("/images/wallpapers/main_menu.png");

        int x;
        int y;
        int height12Fold = Client.getHeight() / 12;

        int width = 400;
        int height = 50;
        int margin = 5;

        x = (Client.getWidth() - width) / 2;
        y = height + margin;

        int quantity = 4;
        int offsetY = height12Fold * 8 - (y * quantity) / 2;

        labelA = new GuiLabel(Client.getWidth() / 2, height12Fold * 4, 48, true, Client.getTitle());
        labelB = new GuiLabel(Client.getWidth() / 2, height12Fold * 5, 16, false, "v" + Client.getVersion());

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
        buttonContinue.setAvailable(Client.isGameStarted());
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
            Client.getGameMenu().activeMenuHelp();
        }

        // Exit button:
        buttonExit.tick();
        if (buttonExit.isPressed()) {
            Client.getGameMenu().activeMenuExit();
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
