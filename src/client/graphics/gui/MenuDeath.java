package client.graphics.gui;

// Created by Aunmag on 19.11.2016.

import client.Client;
import managers.SoundManager;

public class MenuDeath extends MenuAbstract {

    private GuiButton buttonBack;
    private GuiLabel labelTitle;
    private GuiLabel labelMessage;

    private SoundManager sound;

    public MenuDeath() {

        super("/images/wallpapers/death.png");

        int x;
        int y;
        int height12Fold = Client.getHeight() / 12;

        x = Client.getWidth() / 2;
        y = height12Fold * 4;
        labelTitle = new GuiLabel(x, y, 48, true, "You have died");

        y = height12Fold * 5;
        labelMessage = new GuiLabel(x, y, 16, false, "You have killed 0 zombies.");

        int width = 400;
        int height = 50;

        x = (Client.getWidth() - width) / 2;
        y = height / 2 + height12Fold * 8;

        buttonBack = new GuiButton(x, y, 400, 50, "Back to main menu");

        sound = new SoundManager("/sounds/music/death.wav");
        sound.setVolume(-4);

    }

    public void tick() {

        buttonBack.tick();

        if (buttonBack.isPressed()) {
            sound.stop();
            Client.getGameMenu().getMenuMain().getSoundscape().loop();
            Client.getGameMenu().activeMenuMain();
        }

    }

    public void render() {

        renderWallpaper();

        buttonBack.render();

        labelTitle.render();
        labelMessage.render();

    }

    // Setters:

    public void setTitle(String title) {

        labelMessage.setText(title);

    }

    public void setMessage(String message) {

        labelMessage.setText(message);

    }

    // Getters:

    public SoundManager getSound() {

        return sound;

    }

}
