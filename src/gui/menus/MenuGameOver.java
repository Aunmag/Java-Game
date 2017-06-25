package gui.menus;

import gui.components.GuiButton;
import gui.components.GuiLabel;
import client.GamePlay;
import managers.SoundManager;

/**
 * Created by Aunmag on 2016.11.19.
 */

public class MenuGameOver extends Menu {

    private static final String defaultMessage = "You have killed 0 zombies.";
    private GuiLabel labelScore;
    private SoundManager sound;

    public MenuGameOver() {
        super("/images/wallpapers/death.png");
        initializeLabelYouHaveDied();
        initializeLabelScore();
        initializeButtonBack();
        initializeSound();
    }

    private void initializeLabelYouHaveDied() {
        allLabels.add(new GuiLabel(4, 4, 4, 1, 12, 48, true, "You have died"));
    }

    private void initializeLabelScore() {
        labelScore = new GuiLabel(4, 5, 4, 1, 12, 16, false, defaultMessage);
        allLabels.add(labelScore);
    }

    private void initializeButtonBack() {
        Runnable action = () -> {
            MenuManager.getMenuGameOver().deactivate();
            MenuManager.getMenuMain().activate();
        };
        allButtons.add(new GuiButton(4, 8, 4, 1, 12, "Back to main menu", action));
    }

    private void initializeSound() {
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
        setMessage(defaultMessage);
    }

    /* Setters */

    public void setMessage(String message) {
        labelScore.setText(message);
    }

}
