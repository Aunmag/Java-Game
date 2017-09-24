package aunmag.shooter.ai;

import aunmag.shooter.sprites.Actor;
import aunmag.shooter.world.World;
import aunmag.nightingale.utilities.UtilsMath;
import aunmag.nightingale.collision.Collision;

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

        for (Actor actor: World.actors) {
            if (actor.isAlive() && actor.type.equals("human")) {
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

        targetDistance = UtilsMath.calculateDistanceBetween(x, y, targetX, targetY);
        targetDirection = UtilsMath.calculateRadiansBetween(targetX, targetY, x, y);

        isTargetReached = Collision.calculateIsCollision(
                ai.getSubject().getHands(),
                target.getCollision()
        );

        targetRadiansDifference = targetDirection - target.getRadians();
        targetRadiansDifference = UtilsMath.correctRadians(targetRadiansDifference);
        targetRadiansDifferenceAbsolute = Math.abs(targetRadiansDifference);
    }

    protected boolean calculateIsBehindTarget() {
        return targetRadiansDifferenceAbsolute < UtilsMath.PIx0_5;
    }

    abstract void update();

}
