package gui.menus;

import gui.components.GuiButton;
import gui.components.GuiLabel;

/**
 * Created by Aunmag on 2016.11.20.
 */

public class MenuHelp extends Menu {

    public MenuHelp() {
        super("/images/wallpapers/help.png");
        initializeLabelHelp();
        initializeLabelsMovement();
        initializeLabelsRotation();
        initializeLabelsSprint();
        initializeLabelsAttack();
        initializeLabelsZoom();
        initializeLabelsMenu();
        initializeButtonBack();
    }

    private void initializeLabelHelp() {
        allLabels.add(new GuiLabel(5, 1, 2, 1, 12, 48, true, "Help"));
    }

    private void initializeLabelsMovement() {
        allLabels.add(new GuiLabel(4, 3, 1, 1, 12, 16, false, "Rotation"));
        allLabels.add(new GuiLabel(7, 3, 1, 1, 12, 16, false, "W, A, S, D"));
    }

    private void initializeLabelsRotation() {
        allLabels.add(new GuiLabel(4, 4, 1, 1, 12, 16, false, "Movement"));
        allLabels.add(new GuiLabel(7, 4, 1, 1, 12, 16, false, "Mouse"));
    }

    private void initializeLabelsSprint() {
        allLabels.add(new GuiLabel(4, 5, 1, 1, 12, 16, false, "Sprint"));
        allLabels.add(new GuiLabel(7, 5, 1, 1, 12, 16, false, "Shift"));
    }

    private void initializeLabelsAttack() {
        allLabels.add(new GuiLabel(4, 6, 1, 1, 12, 16, false, "Attack"));
        allLabels.add(new GuiLabel(7, 6, 1, 1, 12, 16, false, "LMB"));
    }

    private void initializeLabelsZoom() {
        allLabels.add(new GuiLabel(4, 7, 1, 1, 12, 16, false, "Zoom in/out"));
        allLabels.add(new GuiLabel(7, 7, 1, 1, 12, 16, false, "+/- or Wheel Up/Down"));
    }

    private void initializeLabelsMenu() {
        allLabels.add(new GuiLabel(4, 8, 1, 1, 12, 16, false, "Menu"));
        allLabels.add(new GuiLabel(7, 8, 1, 1, 12, 16, false, "Escape"));
    }

    private void initializeButtonBack() {
        Runnable action = () -> MenuManager.getMenuMain().activate();
        allButtons.add(new GuiButton(4, 10, 4, 1, 12, "Back", action));
    }

    public void deactivate() {}

}
