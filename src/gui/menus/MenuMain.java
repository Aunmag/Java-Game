package gui.menus;

// Created by Aunmag on 17.11.2016.

import client.Constants;
import client.Display;
import gui.components.GuiButton;
import gui.components.GuiText;
import client.GamePlay;
import managers.SoundManager;

public class MenuMain extends Menu {

    private GuiButton buttonContinue;

    private SoundManager soundscape;

    public MenuMain() {

        super("/images/wallpapers/main_menu.png");

        int x;
        int y;
        int height12Fold = Display.getHeight() / 12;

        int width = 400;
        int height = 50;
        int margin = 5;

        x = (Display.getWidth() - width) / 2;
        y = height + margin;

        int quantity = 4;
        int offsetY = height12Fold * 8 - (y * quantity) / 2;

        GuiText labelA = new GuiText(Display.getWidth() / 2, height12Fold * 4, 48, true, Constants.TITLE);
        guiTexts.add(labelA);
        GuiText labelB = new GuiText(Display.getWidth() / 2, height12Fold * 5, 16, false, "v" + Constants.VERSION);
        guiTexts.add(labelB);

        {
            Runnable action = () -> {
                soundscape.stop();
                GamePlay.setIsActive(true);
            };
            buttonContinue = new GuiButton(x, offsetY + y, width, height, "Continue", action);
            buttonContinue.setAvailable(false);
            allButtons.add(buttonContinue);
        }

        {
            Runnable action = () -> {
                soundscape.stop();
                GamePlay.initialize();
                GamePlay.setIsActive(true);
            };
            GuiButton buttonNewGame = new GuiButton(x, offsetY + y * 2, width, height, "New game", action);
            allButtons.add(buttonNewGame);
        }

        {
            Runnable action = () -> MenuManager.getMenuHelp().activate();
            GuiButton buttonHelp = new GuiButton(x, offsetY + y * 3, width, height, "Help", action);
            allButtons.add(buttonHelp);
        }

        {
            Runnable action = () -> MenuManager.getMenuExit().activate();
            GuiButton buttonExit = new GuiButton(x, offsetY + y * 4, width, height, "Exit", action);
            allButtons.add(buttonExit);
        }

        soundscape = new SoundManager("/sounds/music/menu.wav");
        soundscape.loop();

    }

    public void update() {
        buttonContinue.setAvailable(GamePlay.getIsWorldCreated());
        super.update();
    }

    public void deactivate() {}

    // Getters:

    public SoundManager getSoundscape() {

        return soundscape;

    }

}
