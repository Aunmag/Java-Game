package client.states;

// Created by Aunmag on 19.11.2016.

import client.graphics.gui.MenuDeath;
import client.graphics.gui.MenuExit;
import client.graphics.gui.MenuHelp;
import client.graphics.gui.MenuMain;
import client.DataManager;

import java.awt.*;

public class GameMenu {

    private final MenuMain menuMain;
    private final MenuHelp menuHelp;
    private final MenuExit menuExit;
    private final MenuDeath menuDeath;
    private boolean isMenuMain = true;
    private boolean isMenuHelp = false;
    private boolean isMenuExit = false;
    private boolean isMenuDeath = false;
    private Color backgroundColor = new Color(51, 51, 51);

    // Initialization:

    public GameMenu() {

        menuMain = new MenuMain();
        menuHelp = new MenuHelp();
        menuExit = new MenuExit();
        menuDeath = new MenuDeath();

    }

    // Updaters:

    public void tick() {

        if (isMenuMain) {
            menuMain.tick();
        } else if (isMenuHelp) {
            menuHelp.tick();
        } else if (isMenuExit) {
            menuExit.tick();
        } else if (isMenuDeath) {
            menuDeath.tick();
        }

    }

    public void render() {

        DataManager.getHud().setColor(backgroundColor);
        DataManager.getHud().fillRect(0, 0, DataManager.getDisplayWidth(), DataManager.getDisplayHeight());

        if (isMenuMain) {
            menuMain.render();
        } else if (isMenuHelp) {
            menuHelp.render();
        } else if (isMenuExit) {
            menuExit.render();
        } else if (isMenuDeath) {
            menuDeath.render();
        }

    }

    // Switches:

    private void active() {

        DataManager.setIsCursorVisible(true);
        DataManager.setIsGamePlay(false);
        DataManager.setIsGameMenu(true);

        isMenuMain = true;

        isMenuHelp = false;
        isMenuExit = false;

        isMenuDeath = false;
        menuDeath.getSound().stop();

    }

    public void activeMenuMain() {

        active();

    }

    public void activeMenuHelp() {

        active();
        isMenuMain = false;
        isMenuHelp = true;

    }

    public void activeMenuExit() {

        active();
        isMenuMain = false;
        isMenuExit = true;

    }

    public void activeMenuDeath() {

        active();
        isMenuMain = false;
        isMenuDeath = true;

        GamePlay.terminate();
        menuDeath.getSound().play();

    }

    // Getters:

    public MenuMain getMenuMain() {

        return menuMain;

    }

    public MenuDeath getMenuDeath() {

        return menuDeath;

    }

}
