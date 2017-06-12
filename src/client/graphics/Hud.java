package client.graphics;

import ai.AI;
import client.DataManager;
import client.Constants;
import client.Display;
import client.PerformanceManager;
import managers.MathManager;
import sprites.Actor;
import sprites.Bullet;
import sprites.Weapon;
import sprites.Object;
import java.awt.*;

// Created by Aunmag on 13.11.2016.

public class Hud {

    public static void render() {
        int y = 20;
        int y_step = 20;

        Display.getGraphicsHud().setColor(Color.WHITE);
        Display.getGraphicsHud().drawString(Constants.TITLE + " v" + Constants.VERSION, 20, y);
        y += y_step;
        Display.getGraphicsHud().drawString("Performance [F1]", 20, y);

        if (!DataManager.isPerformanceData()) {
            return;
        }

        y += y_step * 2;

        float timeSpentUpdate = PerformanceManager.timerUpdating.getTimeDurationAverage();
        float timeSpentRender = PerformanceManager.timerRendering.getTimeDurationAverage();
        float timeSpentFinish = PerformanceManager.timerFinishing.getTimeDurationAverage();
        float timeSpentTotal = timeSpentUpdate + timeSpentRender + timeSpentFinish;

        float round = 100f;

        String[] performanceMessages = {
                String.format(
                        "Spent time on updating: %s ms",
                        MathManager.calculateRoundValue(timeSpentUpdate, round)
                ),
                String.format(
                        "Spent time on rendering: %s ms",
                        MathManager.calculateRoundValue(timeSpentRender, round)
                ),
                String.format(
                        "Spent time on finishing: %s ms",
                        MathManager.calculateRoundValue(timeSpentFinish, round)
                ),
                String.format(
                        "Spent time total: %s ms",
                        MathManager.calculateRoundValue(timeSpentTotal, round)
                ),
        };

        for (String message: performanceMessages) {
            Display.getGraphicsHud().drawString(message, 20, y);
            y += y_step;
        }

        y += y_step;
        Display.getGraphicsHud().drawString("AIs: " + AI.all.size(), 20, y);
        y += y_step;
        Display.getGraphicsHud().drawString("Actors: " + Actor.all.size(), 20, y);
        y += y_step;
        Display.getGraphicsHud().drawString("Weapons: " + Weapon.all.size(), 20, y);
        y += y_step;
        Display.getGraphicsHud().drawString("Bullets: " + Bullet.all.size(), 20, y);
        y += y_step;
        Display.getGraphicsHud().drawString("Objects: " + Object.allGround.size(), 20, y);
    }

}
