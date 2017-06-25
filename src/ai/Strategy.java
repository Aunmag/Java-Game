package ai;

import client.Constants;
import managers.MathManager;
import sprites.Actor;
import sprites.components.Collision;

/**
 * Created by Aunmag on 2016.11.21.
 */

abstract class Strategy {

    protected AI ai;
    protected Actor target;
    protected float targetDistance;
    protected float targetDirection;
    protected float targetRadiansDifference;
    protected float targetRadiansDifferenceAbsolute;
    protected boolean isTargetReached;


    Strategy(AI ai) {
        this.ai = ai;
        resetTarget();
    }

    protected void resetTarget() {
        target = null;
        isTargetReached = false;
    }

    protected void searchTarget() {
        resetTarget();

        for (Actor actor: Actor.all) {
            if (actor.getIsAlive() && actor.type.equals("human")) {
                target = actor;
                break;
            }
        }
    }

    protected void updateTargetData() {
        if (target == null) {
            return;
        }

        float x = ai.getSubject().getX();
        float y = ai.getSubject().getY();
        float targetX = target.getX();
        float targetY = target.getY();

        targetDistance = MathManager.calculateDistanceBetween(x, y, targetX, targetY);
        targetDirection = MathManager.calculateRadiansBetween(targetX, targetY, x, y);

        isTargetReached = Collision.calculateIsCollision(
                ai.getSubject().getCollision(),
                target.getCollision()
        );

        targetRadiansDifference = targetDirection - target.getRadians();
        targetRadiansDifference = MathManager.correctRadians(targetRadiansDifference);
        targetRadiansDifferenceAbsolute = Math.abs(targetRadiansDifference);
    }

    protected boolean calculateIsBehindTarget() {
        return targetRadiansDifferenceAbsolute < Constants.PI_0_5;
    }

    abstract void update();

}
