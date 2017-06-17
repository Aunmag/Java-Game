package gui.menus;

// Created by Aunmag on 20.11.2016.

import client.DataManager;
import client.Display;
import gui.components.GuiButton;
import gui.components.GuiText;

public class MenuExit extends Menu {

    public MenuExit() {

        super("/images/wallpapers/exit.png");

        int x;
        int y;
        int height12Fold = Display.getHeight() / 12;

        int width = 400;
        int height = 50;
        int margin = 5;

        x = (Display.getWidth() - width) / 2;
        y = height + margin;

        int quantity = 2;
        int offsetY = height12Fold * 8 - (y * quantity) / 2;

        GuiText labelQuestion = new GuiText(Display.getWidth() / 2, height12Fold * 4, 36, true, "Are you sure you want to exit?");
        guiTexts.add(labelQuestion);

        {
            Runnable action = () -> DataManager.setIsRunning(false);
            GuiButton buttonExit = new GuiButton(x, offsetY + y, width, height, "Yes", action);
            allButtons.add(buttonExit);
        }

        {
            Runnable action = () -> MenuManager.getMenuMain().activate();
            GuiButton buttonBack = new GuiButton(x, offsetY + y * 2, width, height, "No", action);
            allButtons.add(buttonBack);
        }
    }

    public void deactivate() {}

}
