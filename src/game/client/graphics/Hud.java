package game.client.graphics;

import game.world.World;
import nightingale.Application;
import nightingale.data.DataEngine;
import nightingale.font.Text;
import nightingale.gui.GuiLabel;
import nightingale.utilities.UtilsMath;
import org.lwjgl.glfw.GLFW;

public class Hud {

    private static boolean isVisible = false;
    private static Text text = createText(DataEngine.titleFull);

    private static Text createText(String message) {
        return new Text(
                0,
                0,
                Application.getWindow().getWidth(),
                message,
                1.2f,
                GuiLabel.font,
                false
        );
    }

    public static void render() {
        if (Application.getInput().isKeyPressed(GLFW.GLFW_KEY_F1)) {
            isVisible = !isVisible;
        }

        if (!isVisible) {
            return;
        }

        float timeSpentUpdate = 0;
        float timeSpentRender = 0;
        float timeSpentTotal = timeSpentUpdate + timeSpentRender;
        float round = 100f;
        timeSpentUpdate = UtilsMath.calculateRoundValue(timeSpentUpdate, round);
        timeSpentRender = UtilsMath.calculateRoundValue(timeSpentRender, round);
        timeSpentTotal = UtilsMath.calculateRoundValue(timeSpentTotal, round);

        String message = "";
        message += String.format("Spent time on updating: %s ms\n", timeSpentUpdate);
        message += String.format("Spent time on rendering: %s ms\n", timeSpentRender);
        message += String.format("Spent time total: %s ms \n", timeSpentTotal);
        message += String.format("\nAIs: %s", World.ais.size());
        message += String.format("\nActors: %s", World.actors.size());
        message += String.format("\nBullets: %s", World.bullets.size());
        message += String.format("\nTerrains: %s", World.terrains.size());
        message += String.format("\nDecorations: %s", World.decorations.size());
        message += String.format("\nTrees: %s", World.trees.size());

        if (!text.message.equals(message)) {
            text.delete();
            text = createText(message);
        }

        GuiLabel.font.renderPrepare();
        text.render();
    }

}
