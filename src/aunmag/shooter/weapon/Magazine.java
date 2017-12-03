package aunmag.shooter.weapon;

import aunmag.nightingale.Application;
import aunmag.nightingale.utilities.TimerDone;
import aunmag.nightingale.utilities.UtilsGraphics;
import aunmag.shooter.client.Game;
import org.lwjgl.opengl.GL11;

public class Magazine {

    public final CartridgeType cartridgeType;
    private final boolean isAutomatic;
    private final int capacity;
    private int cartridgesQuantity;
    private boolean isReloading = false;
    private TimerDone timeReloading;

    public Magazine(
            CartridgeType cartridgeType,
            boolean isAutomatic,
            int capacity,
            float reloadingTime
    ) {
        this.cartridgeType = cartridgeType;
        this.isAutomatic = isAutomatic;
        this.capacity = capacity;
        cartridgesQuantity = capacity;

        if (isUnlimited()) {
            reloadingTime = 0;
        } else if (isAutomatic) {
            reloadingTime /= (float) capacity;
        }

        timeReloading = new TimerDone(reloadingTime);
    }

    void update() {
        if (isReloading && timeReloading.calculateIsDone(Game.getWorld().getTime().getCurrent())) {
            cartridgesQuantity++;

            if (isFull() || !isAutomatic) {
                isReloading = false;
            } else {
                timeReloading.setTimeInitial(Game.getWorld().getTime().getCurrent());
            }
        }
    }

    boolean takeNextCartridge() {
        boolean hasCartridge = !isEmpty();

        if (hasCartridge && !isUnlimited()) {
            cartridgesQuantity--;
        }

        return hasCartridge;
    }

    public void reload() {
        if (isReloading || isFull()) {
            return;
        }

        if (timeReloading.getTimeDuration() == 0) {
            if (isAutomatic) {
                cartridgesQuantity = capacity;
            } else {
                cartridgesQuantity++;
            }
            return;
        }

        isReloading = true;
        timeReloading.setTimeInitial(Game.getWorld().getTime().getCurrent());

        if (isAutomatic) {
            cartridgesQuantity = 0;
        }
    }

    public void renderHud() {
        float alpha = 0.75f;
        float height = 10f;
        float width = Application.getWindow().getCenterX() / 4;
        float widthLoaded = width;
        float x = (Application.getWindow().getWidth() - width) / 2;
        float y = Application.getWindow().getHeight() - height * 1.5f;

        if (!isUnlimited()) {
            widthLoaded *= cartridgesQuantity / (float) capacity;
        }

        if (isReloading && isAutomatic) {
            alpha = 1 - alpha;
        }

        UtilsGraphics.drawPrepare();
        GL11.glLineWidth(height);

        GL11.glColor4f(1, 1, 1, alpha);
        UtilsGraphics.drawLine(x, y, x + widthLoaded, y, false);

        GL11.glColor4f(1, 1, 1, 1 - alpha);
        UtilsGraphics.drawLine(x + widthLoaded, y, x + width, y, false);

        UtilsGraphics.drawFinish();
        GL11.glLineWidth(1);
    }

    /* Getters */

    public boolean isFull() {
        return cartridgesQuantity == capacity;
    }

    public boolean isEmpty() {
        return isReloading || (cartridgesQuantity == 0 && !isUnlimited());
    }

    public boolean isUnlimited() {
        return capacity == 0;
    }

}
