package gui.menus;

import client.Constants;
import gui.components.GuiButton;
import gui.components.GuiLabel;
import client.GamePlay;
import managers.SoundManager;

/**
 * Created by Aunmag on 2016.11.17.
 */

public class MenuMain extends Menu {

    private GuiButton buttonContinue;
    private SoundManager sound;

    public MenuMain() {
        super("/images/wallpapers/main_menu.png");
        initializeLabelTitle();
        initializeLabelVersionAndDeveloper();
        initializeButtonContinue();
        initializeButtonNewGame();
        initializeButtonHelp();
        initializeButtonExit();
        initializeSound();
    }

    private void initializeLabelTitle() {
        allLabels.add(new GuiLabel(3, 3, 6, 1, 12, 48, true, Constants.TITLE));
    }

    private void initializeLabelVersionAndDeveloper() {
        String text = String.format("v%s by %s", Constants.VERSION, Constants.DEVELOPER);
        allLabels.add(new GuiLabel(5, 4, 2, 1, 12, 16, false, text));
    }

    private void initializeButtonContinue() {
        Runnable action = () -> {
            sound.stop();
            GamePlay.setIsActive(true);
        };

        buttonContinue = new GuiButton(4, 7, 4, 1, 12, "Continue", action);
        buttonContinue.setIsAvailable(false);
        allButtons.add(buttonContinue);
    }

    private void initializeButtonNewGame() {
        Runnable action = () -> {
            sound.stop();
            GamePlay.initialize();
            GamePlay.setIsActive(true);
        };

        allButtons.add(new GuiButton(4, 8, 4, 1, 12, "New game", action));
    }

    private void initializeButtonHelp() {
        Runnable action = () -> MenuManager.getMenuHelp().activate();
        allButtons.add(new GuiButton(4, 9, 4, 1, 12, "Help", action));
    }

    private void initializeButtonExit() {
        Runnable action = () -> MenuManager.getMenuExit().activate();
        allButtons.add(new GuiButton(4, 10, 4, 1, 12, "Exit", action));
    }

    private void initializeSound() {
        sound = new SoundManager("/sounds/music/menu.wav");
        sound.loop();
    }

    public void update() {
        buttonContinue.setIsAvailable(GamePlay.getIsWorldCreated());
        super.update();
    }

    public void deactivate() {}

    /* Getters */

    public SoundManager getSound() {
        return sound;
    }

}
