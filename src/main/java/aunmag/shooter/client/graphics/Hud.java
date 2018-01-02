package aunmag.shooter.client.graphics;

import aunmag.nightingale.input.Input;
import aunmag.nightingale.Application;
import aunmag.nightingale.data.DataEngine;
import aunmag.nightingale.font.Text;
import aunmag.nightingale.font.Font;
import aunmag.nightingale.utilities.UtilsMath;
import aunmag.shooter.client.App;
import aunmag.shooter.client.states.Game;
import aunmag.shooter.world.World;
import org.lwjgl.glfw.GLFW;

public class Hud {

    private static boolean isVisible = false;
    private static Text text = createText(DataEngine.TITLE_FULL);

    private static Text createText(String message) {
        return new Text(
                10,
                10,
                Application.getWindow().getWidth(),
                message,
                1f,
                Font.fontDefault,
                false
        );
    }

    public static void render() {
        if (Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_F1)) {
            isVisible = !isVisible;
        }

        if (!isVisible) {
            return;
        }

        Game game = App.main.getGame();

        if (game == null) {
            return;
        }

        World world = game.getWorld();

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
        message += String.format("\nAIs: %s", world.getAis().size());
        message += String.format("\nActors: %s", world.getActors().size());
        message += String.format("\nBullets: %s", world.getProjectiles().size());

        if (!text.message.equals(message)) {
            text.delete();
            text = createText(message);
        }

        Font.fontDefault.renderPrepare();
        text.render();
    }

}
