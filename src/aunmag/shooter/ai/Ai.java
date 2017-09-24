package aunmag.shooter.ai;

import aunmag.nightingale.collision.Collision;
import aunmag.nightingale.utilities.UtilsMath;
import aunmag.shooter.managers.NextTimer;
import aunmag.shooter.sprites.Actor;
import aunmag.nightingale.basics.BaseOperative;
import aunmag.shooter.world.World;

public class Ai implements BaseOperative {

    private boolean isRemoved = false;
    private NextTimer reactionWatch = new NextTimer(300);
    private NextTimer reactionLookAround = new NextTimer(2_000);
    private Actor subject;
    protected Actor target;
    protected float targetDistance;
    protected float targetDirection;
    protected float targetRadiansDifference;
    protected float targetRadiansDifferenceAbsolute;
    protected boolean isTargetReached;

    public Ai(Actor subject) {
        this.subject = subject;
    }

    public void update() {
        if (subject.isRemoved() || !subject.isAlive()) {
            remove();
            return;
        }

        reactionLookAround.update(System.currentTimeMillis());
        if (reactionLookAround.isNow()) {
            searchTarget();
        }

        if (target == null) {
            stop();
            return;
        }

        reactionWatch.update(System.currentTimeMillis());
        if (reactionWatch.isNow()) {
            updateTargetData();
        }

        if (isTargetReached) {
            attackTarget();
        } else {
            chaseTarget();
        }
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

        float x = subject.getX();
        float y = subject.getY();
        float targetX = target.getX();
        float targetY = target.getY();

        targetDistance = UtilsMath.calculateDistanceBetween(x, y, targetX, targetY);
        targetDirection = UtilsMath.calculateRadiansBetween(targetX, targetY, x, y);

        isTargetReached = Collision.calculateIsCollision(
                subject.getHands(),
                target.getCollision()
        );

        targetRadiansDifference = targetDirection - target.getRadians();
        targetRadiansDifference = UtilsMath.correctRadians(targetRadiansDifference);
        targetRadiansDifferenceAbsolute = Math.abs(targetRadiansDifference);
    }

    protected boolean calculateIsBehindTarget() {
        return targetRadiansDifferenceAbsolute < UtilsMath.PIx0_5;
    }

    private void chaseTarget() {
        if (target == null) {
            return;
        }

        subject.isAttacking = false;
        subject.setRadians(targetDirection);
        subject.isWalkingForward = true;
        subject.isSprinting = calculateIsBehindTarget();

        if (96 < targetDistance && targetDistance < 512 && targetRadiansDifferenceAbsolute > Math.PI / 1.4) { // TODO: Improve
            deviateRoute();
        } else if (targetDistance < 96) {
            subject.isSprinting = true;
        }
    }

    private void attackTarget() {
        subject.isAttacking = true;
        subject.isWalkingForward = false;
    }

    private void deviateRoute() {
        float radiansDeviate = 0;

        if (targetDistance > 512) {
            radiansDeviate = (float) UtilsMath.PIx0_5 / 2f;
        } else if (targetDistance > 256) {
            radiansDeviate = (float) UtilsMath.PIx0_5 / 4f;
        } else if (targetDistance > 128) {
            radiansDeviate = (float) UtilsMath.PIx0_5 / 8f;
        } else if (targetDistance > 64) {
            radiansDeviate = (float) UtilsMath.PIx0_5 / 16f;
        }

        if (targetRadiansDifference < 0) {
            subject.setRadians(subject.getRadians() - radiansDeviate);
        } else {
            subject.setRadians(subject.getRadians() + radiansDeviate);
        }
    }

    private void stop() {
        subject.isAttacking = false;
        subject.isSprinting = false;
        subject.isWalkingForward = false;
    }

    public void render() {}

    public void remove() {
        isRemoved = true;
    }

    /* Getters */

    public boolean isRemoved() {
        return isRemoved;
    }

}
