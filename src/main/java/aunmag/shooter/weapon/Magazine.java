package aunmag.shooter.weapon;

import aunmag.nightingale.Application;
import aunmag.nightingale.utilities.Timer;
import aunmag.nightingale.utilities.UtilsGraphics;
import aunmag.shooter.world.World;
import org.lwjgl.opengl.GL11;

public class Magazine {

    public final World world;
    public final CartridgeType cartridgeType;
    private final boolean isAutomatic;
    private final int capacity;
    private int cartridgesQuantity;
    private boolean isReloading = false;
    private final Timer timeReloading;

    public Magazine(
            World world,
            CartridgeType cartridgeType,
            boolean isAutomatic,
            int capacity,
            float reloadingTime
    ) {
        this.world = world;
        this.cartridgeType = cartridgeType;
        this.isAutomatic = isAutomatic;
        this.capacity = capacity;
        cartridgesQuantity = capacity;

        if (isUnlimited()) {
            reloadingTime = 0;
        } else if (isAutomatic) {
            reloadingTime /= (float) capacity;
        }

        timeReloading = new Timer(world.getTime(), reloadingTime, 0.125f);
    }

    void update() {
        if (isReloading && timeReloading.isDone()) {
            cartridgesQuantity++;

            if (isFull() || !isAutomatic) {
                isReloading = false;
            } else {
                timeReloading.next();
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

        if (timeReloading.getDuration() == 0) {
            if (isAutomatic) {
                cartridgesQuantity = capacity;
            } else {
                cartridgesQuantity++;
            }
            return;
        }

        isReloading = true;
        timeReloading.next();

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
