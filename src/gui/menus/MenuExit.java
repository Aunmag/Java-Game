package gui.menus;

import client.Application;
import gui.components.GuiButton;
import gui.components.GuiLabel;

/**
 * Created by Aunmag on 2016.11.20.
 */

public class MenuExit extends Menu {

    public MenuExit() {
        super("/images/wallpapers/exit.png");
        initializeLabelQuestion();
        initializeButtonYes();
        initializeButtonNo();
    }

    private void initializeLabelQuestion() {
        allLabels.add(new GuiLabel(3, 5, 6, 1, 12, 48, true, "Are you sure you want to exit?"));
    }

    private void initializeButtonYes() {
        Runnable action = () -> Application.isRunning = false;
        allButtons.add(new GuiButton(6, 9, 3, 1, 12, "Yes", action));
    }

    private void initializeButtonNo() {
        Runnable action = () -> MenuManager.getMenuMain().activate();
        allButtons.add(new GuiButton(3, 9, 3, 1, 12, "No", action));
    }

    public void deactivate() {}

}
