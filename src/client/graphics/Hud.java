package client.graphics;

import ai.AI;
import client.Client;
import client.Constants;
import sprites.Actor;
import sprites.Bullet;
import sprites.Weapon;
import sprites.Object;
import java.awt.*;

// Created by Aunmag on 13.11.2016.

public class Hud {

    public static void render() {

        Client.getHud().setColor(Color.WHITE);

        Client.getHud().drawString(Constants.TITLE + " v" + Constants.VERSION, 20, 20);
        Client.getHud().drawString("Performance [F1]", 20, 40);

        if (!Client.isPerformanceData()) {
            return;
        }

        float t = Math.round(Client.tPerformanceAverage.getAverage() * 10.0f) / 10.0f;

        if (t >= 8) {
            Client.getHud().setColor(Color.red);
        } else if (t >= 4) {
            Client.getHud().setColor(Color.orange);
        } else if (t >= 2) {
            Client.getHud().setColor(Color.yellow);
        } else if (t >= 1) {
            Client.getHud().setColor(Color.green);
        }

        Client.getHud().drawString("Lags time: " + t + " ms", 20, 60);

        Client.getHud().setColor(Color.WHITE);

        Client.getHud().drawString("AIs: " + AI.allAIs.size(), 20, 80);
        Client.getHud().drawString("Actors: " + Actor.all.size(), 20, 100);
        Client.getHud().drawString("Weapons: " + Weapon.all.size(), 20, 120);
        Client.getHud().drawString("Bullets: " + Bullet.all.size(), 20, 140);
        Client.getHud().drawString("Objects: " + Object.allGround.size(), 20, 160);

    }

}
