package gui.menus;

import client.Display;
import client.GamePlay;

import java.awt.*;

/**
 * Created by Aunmag on 2017.06.15.
 */

public class MenuManager {

    private static final MenuMain menuMain = new MenuMain();
    private static final MenuHelp menuHelp = new MenuHelp();
    private static final MenuExit menuExit = new MenuExit();
    private static final MenuGameOver menuGameOver = new MenuGameOver();
    private static Menu currentMenu = menuMain;
    private static final Color backgroundColor = new Color(51, 51, 51);

    public static void activateMenu(Menu menu) {
        GamePlay.setIsActive(false);
        currentMenu = menu;
    }

    public static void update() {
        if (currentMenu != null) {
            currentMenu.update();
        }
    }

    public static void render() {
        Display.getGraphicsHud().setColor(backgroundColor);
        Display.getGraphicsHud().fillRect(0, 0, Display.getWidth(), Display.getHeight());

        if (currentMenu != null) {
            currentMenu.render();
        }
    }

    /* Getters */

    public static MenuMain getMenuMain() {
        return menuMain;
    }

    public static MenuGameOver getMenuGameOver() {
        return menuGameOver;
    }

    public static MenuHelp getMenuHelp() {
        return menuHelp;
    }

    public static MenuExit getMenuExit() {
        return menuExit;
    }

}
