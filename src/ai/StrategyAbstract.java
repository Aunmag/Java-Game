package ai;

// Created by Aunmag on 21.11.2016.

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

        double subjectX = ai.subject.x;
        double subjectY = ai.subject.y;
        double targetX = ai.target.x;
        double targetY = ai.target.y;

        ai.targetDistance = Math.sqrt(Math.pow(targetX - subjectX, 2) + Math.pow(targetY - subjectY, 2));
        ai.targetDirection = Math.atan2(targetY - subjectY, targetX - subjectX);

        double x = ai.subject.meleeX;
        double y = ai.subject.meleeY;
        double radius = ai.subject.meleeRadius / 2;
        ai.isTargetReached = IsIntersection.circleCircle(x, y, radius, targetX, targetY, ai.target.bodyRadius);

        // Radians between subject and target and their difference:
        double radiansDifference = ai.targetDirection - ai.target.getRadians();

        // Tweak radians value:
        if (radiansDifference > Math.PI) {
            radiansDifference -= Math.PI * 2;
        } else if (radiansDifference < -Math.PI) {
            radiansDifference += Math.PI * 2;
        }

        ai.targetFlankReal = radiansDifference;
        ai.targetFlankAbs = Math.abs(radiansDifference);

    }

    // Updaters:

    abstract void tick();

}
