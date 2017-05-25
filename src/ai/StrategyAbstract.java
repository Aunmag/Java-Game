package ai;

// Created by Aunmag on 21.11.2016.

import client.Constants;
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

        for (Actor actor: Actor.allActors) {
            if (actor.type.equals("human") && actor.isAlive) {
                ai.target = actor;
                break;
            }
        }

    }

    void updateTargetPosition() {

        if (ai.target == null) {
            return;
        }

        float subjectX = ai.subject.x;
        float subjectY = ai.subject.y;
        float targetX = ai.target.x;
        float targetY = ai.target.y;

        ai.targetDistance = (float) (Math.sqrt(Math.pow(targetX - subjectX, 2) + Math.pow(targetY - subjectY, 2)));
        ai.targetDirection = (float) (Math.atan2(targetY - subjectY, targetX - subjectX));

        float x = ai.subject.meleeX;
        float y = ai.subject.meleeY;
        float radius = ai.subject.meleeRadius / 2;
        ai.isTargetReached = IsIntersection.circleCircle(x, y, radius, targetX, targetY, ai.target.bodyRadius);

        // Radians between subject and target and their difference:
        float radiansDifference = ai.targetDirection - ai.target.getRadians();

        // Tweak radians value:
        if (radiansDifference > Math.PI) {
            radiansDifference -= Constants.PI_2_0;
        } else if (radiansDifference < -Math.PI) {
            radiansDifference += Constants.PI_2_0;
        }

        ai.targetFlankReal = radiansDifference;
        ai.targetFlankAbs = Math.abs(radiansDifference);

    }

    // Updaters:

    abstract void tick();

}
