package aunmag.shooter.weapon;

import aunmag.shooter.sprites.Actor;

public class WeaponTrigger {

    private boolean isAutomaticMode;
    private boolean isClicked = false;
    private boolean isPressed = false;
    private Actor shooter = null;

    public WeaponTrigger(boolean isAutomaticMode) {
        this.isAutomaticMode = isAutomaticMode;
    }

    public void pressBy(Actor shooter) {
        if (!isPressed) {
            this.shooter = shooter;
            isClicked = true;
        }

        isPressed = true;
    }

    public void release() {
        isPressed = false;
        shooter = null;
    }

    /* Getters */

    public boolean isFiring() {
        return isAutomaticMode ? isPressed : isClicked();
    }

    public boolean isClicked() {
        // TODO: Improve
        boolean wasClicked = isClicked;
        isClicked = false;
        return wasClicked;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public Actor getShooter() {
        return shooter;
    }

}
