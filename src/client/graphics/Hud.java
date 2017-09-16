package client.graphics;

import ai.AI;
import client.Constants;
import client.Display;
import managers.PerformanceManager;
import nightingale.utilities.UtilsMath;
import sprites.Actor;
import sprites.Bullet;
import sprites.Weapon;
import sprites.Object;
import java.awt.*;

/**
 * Created by Aunmag on 2016.11.13.
 */

public class Hud {

    public static void render() {
        int indentation = 20;
        int y = indentation;

        String[] messages = {
                String.format("%s v%s", Constants.TITLE, Constants.VERSION),
                "Performance [F1]"
        };

        Display.getGraphicsHud().setColor(Color.WHITE);

        for (String message: messages) {
            Display.getGraphicsHud().drawString(message, indentation, y);
            y += indentation;
        }

        if (!PerformanceManager.isMonitoring) {
            return;
        }

        y += indentation;

        float timeSpentUpdate = PerformanceManager.timerUpdating.getTimeDurationAverage();
        float timeSpentRender = PerformanceManager.timerRendering.getTimeDurationAverage();
        float timeSpentFinish = PerformanceManager.timerFinishing.getTimeDurationAverage();
        float timeSpentTotal = timeSpentUpdate + timeSpentRender + timeSpentFinish;
        float round = 100f;
        timeSpentUpdate = UtilsMath.calculateRoundValue(timeSpentUpdate, round);
        timeSpentRender = UtilsMath.calculateRoundValue(timeSpentRender, round);
        timeSpentFinish = UtilsMath.calculateRoundValue(timeSpentFinish, round);
        timeSpentTotal = UtilsMath.calculateRoundValue(timeSpentTotal, round);

        String[] messagesPerformance = {
                String.format("Spent time on updating: %s ms", timeSpentUpdate),
                String.format("Spent time on rendering: %s ms", timeSpentRender),
                String.format("Spent time on finishing: %s ms", timeSpentFinish),
                String.format("Spent time total: %s ms", timeSpentTotal),
                "",
                String.format("AIs: %s", AI.all.size()),
                String.format("Actors: %s", Actor.all.size()),
                String.format("Weapons: %s", Weapon.all.size()),
                String.format("Bullets: %s", Bullet.all.size()),
                String.format("Objects: %s", Object.allGround.size()),
        };

        for (String message: messagesPerformance) {
            Display.getGraphicsHud().drawString(message, indentation, y);
            y += indentation;
        }
    }

}
