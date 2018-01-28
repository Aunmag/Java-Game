package aunmag.shooter.environment.magazine;

import aunmag.nightingale.Application;
import aunmag.nightingale.utilities.Timer;
import aunmag.nightingale.utilities.UtilsGraphics;
import aunmag.shooter.environment.World;
import org.lwjgl.opengl.GL11;

public class Magazine {

    public final World world;
    public final MagazineType type;
    private int cartridgesQuantity;
    private boolean isReloading = false;
    private final Timer reloadingTimer;

    public Magazine(World world, MagazineType type) {
        this.world = world;
        this.type = type;
        cartridgesQuantity = type.getCapacity();

        float reloadingTime = type.getTimeReloading();

        if (type.isAutomatic()) {
            reloadingTime /= (float) type.getCapacity();
        }

        reloadingTimer = new Timer(world.getTime(), reloadingTime, 0.125f);
    }

    public void update() {
        if (isReloading && reloadingTimer.isDone()) {
            cartridgesQuantity++;

            if (isFull() || !type.isAutomatic()) {
                isReloading = false;
            } else {
                reloadingTimer.next();
            }
        }
    }

    public boolean takeNextCartridge() {
        boolean hasCartridge = !isEmpty();

        if (hasCartridge && !type.isUnlimited()) {
            cartridgesQuantity--;
        }

        return hasCartridge;
    }

    public void reload() {
        if (isReloading || isFull()) {
            return;
        }

        if (reloadingTimer.getDuration() == 0) {
            if (type.isAutomatic()) {
                cartridgesQuantity = type.getCapacity();
            } else {
                cartridgesQuantity++;
            }
            return;
        }

        isReloading = true;
        reloadingTimer.next();

        if (type.isAutomatic()) {
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

        if (!type.isUnlimited()) {
            widthLoaded *= cartridgesQuantity / (float) type.getCapacity();
        }

        if (isReloading && type.isAutomatic()) {
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
        return cartridgesQuantity == type.getCapacity();
    }

    public boolean isEmpty() {
        return isReloading || (cartridgesQuantity == 0 && !type.isUnlimited());
    }

}
