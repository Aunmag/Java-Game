package ai;

// Created by Aunmag on 21.11.2016.

import managers.MathManager;
import sprites.Actor;
import sprites.components.Collision;

abstract class StrategyAbstract {

    AI ai;

    StrategyAbstract(AI ai) {

        this.ai = ai;

    }

    // Analysis methods:

    void searchTarget() {

        ai.target = null;

        for (Actor actor: Actor.all) {
            if (actor.type.equals("human") && actor.getIsAlive()) {
                ai.target = actor;
                break;
            }
        }

    }

    void updateTargetPosition() {

        if (ai.target == null) {
            return;
        }

        float subjectX = ai.subject.getX();
        float subjectY = ai.subject.getY();
        float targetX = ai.target.getX();
        float targetY = ai.target.getY();

        ai.targetDistance = (float) (Math.sqrt(Math.pow(targetX - subjectX, 2) + Math.pow(targetY - subjectY, 2)));
        ai.targetDirection = (float) (Math.atan2(targetY - subjectY, targetX - subjectX));

        ai.isTargetReached = Collision.calculateIsCollision(
                ai.subject.getCollision(),
                ai.target.getCollision()
        );

        // Radians between subject and target and their difference:
        float radiansDifference = ai.targetDirection - ai.target.getRadians();
        radiansDifference = MathManager.correctRadians(radiansDifference);

        ai.targetFlankReal = radiansDifference;
        ai.targetFlankAbs = Math.abs(radiansDifference);

    }

    // Updaters:

    abstract void tick();

}
