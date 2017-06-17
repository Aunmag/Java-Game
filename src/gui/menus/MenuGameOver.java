package gui.menus;

// Created by Aunmag on 19.11.2016.

import client.Display;
import gui.components.GuiButton;
import gui.components.GuiText;
import client.GamePlay;
import managers.SoundManager;

public class MenuGameOver extends Menu {

    private GuiText labelMessage;

    private SoundManager sound;

    public MenuGameOver() {
        super("/images/wallpapers/death.png");

        int x;
        int y;
        int height12Fold = Display.getHeight() / 12;

        x = Display.getWidth() / 2;
        y = height12Fold * 4;
        GuiText labelTitle = new GuiText(x, y, 48, true, "You have died");
        guiTexts.add(labelTitle);

        y = height12Fold * 5;
        labelMessage = new GuiText(x, y, 16, false, "You have killed 0 zombies.");
        guiTexts.add(labelMessage);

        int width = 400;
        int height = 50;

        x = (Display.getWidth() - width) / 2;
        y = height / 2 + height12Fold * 8;

        Runnable action = () -> MenuManager.getMenuMain().activate();
        GuiButton buttonBack = new GuiButton(x, y, 400, 50, "Back to main menu", action);
        allButtons.add(buttonBack);

        sound = new SoundManager("/sounds/music/death.wav");
        sound.setVolume(-4);
    }

    public void activate() {
        GamePlay.terminate();
        sound.play();
        super.activate();
    }

    public void deactivate() {
        sound.stop();
    }

    /* Setters */

    public void setMessage(String message) {

        labelMessage.setText(message);

    }

    /* Getters */

    public SoundManager getSound() {

        return sound;

    }

}
