package aunmag.shooter.ai;

import aunmag.nightingale.collision.Collision;
import aunmag.nightingale.utilities.UtilsMath;
import aunmag.shooter.sprites.Actor;
import aunmag.nightingale.basics.BaseOperative;
import aunmag.shooter.world.World;

public class Ai implements BaseOperative {

    private Actor subject;
    private boolean isRemoved = false;
    private int timeReaction = 300;
    private long timeReactionNext = 0;
    private boolean isReactionNow = false;
    protected static final int timeSearchTarget = 2_000;
    protected long timeSearchTargetNext = 0;
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
        if (System.currentTimeMillis() > timeReactionNext) {
            isReactionNow = true;
            timeReactionNext = System.currentTimeMillis() + timeReaction;
        }

        if (subject.isRemoved() || !subject.isAlive()) {
            remove();
            return;
        }

        if (System.currentTimeMillis() > timeSearchTargetNext) {
            searchTarget();
            timeSearchTargetNext = System.currentTimeMillis() + timeSearchTarget;
        }

        if (target == null) {
            stop();
            return;
        }

        if (isReactionNow()) {
            updateTargetData();
        }

        if (isTargetReached) {
            attackTarget();
        } else {
            chaseTarget();
        }

        isReactionNow = false;
    }

    private void updateReaction() {}

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

    boolean isReactionNow() {
        return isReactionNow;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

}
