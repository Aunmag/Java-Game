package ai;

// Created by Aunmag on 21.11.2016.

import managers.MathManager;
import scripts.IsIntersection;
import sprites.Actor;

abstract class StrategyAbstract {

    AI ai;

    StrategyAbstract(AI ai) {

        this.ai = ai;

    }

    // Analysis methods:

    void searchTarget() {

        ai.target = null;

        for (Actor actor: Actor.all) {
            if (actor.group.equals("human") && actor.getIsAlive()) {
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

        float x = ai.subject.getHands().getX();
        float y = ai.subject.getHands().getY();
        float radius = ai.subject.getHands().getRadius() / 2;
        ai.isTargetReached = IsIntersection.circleCircle(x, y, radius, targetX, targetY, ai.target.getCollision().getRadius());

        // Radians between subject and target and their difference:
        float radiansDifference = ai.targetDirection - ai.target.getRadians();
        radiansDifference = MathManager.correctRadians(radiansDifference);

        ai.targetFlankReal = radiansDifference;
        ai.targetFlankAbs = Math.abs(radiansDifference);

    }

    // Updaters:

    abstract void tick();

}
