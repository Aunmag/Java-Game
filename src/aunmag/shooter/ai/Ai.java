package aunmag.shooter.ai;

import aunmag.nightingale.basics.BaseOperative;
import aunmag.nightingale.collision.Collision;
import aunmag.nightingale.utilities.UtilsMath;
import aunmag.nightingale.utilities.TimerNext;
import aunmag.shooter.actor.Actor;
import aunmag.shooter.actor.ActorType;
import aunmag.shooter.client.Game;

public class Ai implements BaseOperative {

    private boolean isRemoved = false;
    private TimerNext reactionWatch = new TimerNext(300);
    private TimerNext reactionLookAround = new TimerNext(2_000);
    private TimerNext reactionChangeStrategy = new TimerNext(30_000);
    private int strategyDeviationWay = 0;
    private Actor subject;
    private Actor target;
    private float targetDistance;
    private float targetDirection;
    private float targetRadiansDifference;
    private float targetRadiansDifferenceAbsolute;
    private boolean isTargetReached;

    public Ai(Actor subject) {
        this.subject = subject;
        changeStrategy();
    }

    public void update() {
        if (subject.isRemoved() || !subject.isAlive()) {
            remove();
            return;
        }

        reactionChangeStrategy.update(Game.getWorld().getTime().getCurrentMilliseconds());
        if (reactionChangeStrategy.isNow()) {
            changeStrategy();
        }

        reactionLookAround.update(Game.getWorld().getTime().getCurrentMilliseconds());
        if (reactionLookAround.isNow()) {
            searchTarget();
        }

        if (hasTarget()) {
            reactionWatch.update(Game.getWorld().getTime().getCurrentMilliseconds());
            if (reactionWatch.isNow()) {
                updateTargetData();
            }

            if (isTargetReached) {
                doAttack();
            } else {
                chaseTarget();
            }
        } else {
            doNothing();
        }
    }

    private void changeStrategy() {
        strategyDeviationWay = UtilsMath.randomizeBetween(-1, +1);
    }

    private void searchTarget() {
        target = null;
        isTargetReached = false;

        for (Actor actor: Game.getWorld().getActors()) {
            if (actor.isAlive() && !actor.isRemoved() && actor.type == ActorType.human) {
                target = actor;
                break;
            }
        }
    }

    private void updateTargetData() {
        if (!hasTarget()) {
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

    private boolean calculateIsBehindTarget() {
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

        if (targetRadiansDifferenceAbsolute > UtilsMath.PIx0_5) {
            deviateRoute();
        } else if (targetDistance < 3) {
            subject.isSprinting = true;
        }
    }

    private void deviateRoute() {
        int distanceMin = 3;
        int distanceMax = 20;

        if (distanceMin < targetDistance && targetDistance < distanceMax) {
            float intensity = targetDistance / distanceMax;
            float radians = (float) (UtilsMath.PIx0_5) * intensity;
            subject.addRadiansCarefully(radians * strategyDeviationWay);
        }
    }

    private void doAttack() {
        subject.isAttacking = true;
        subject.isWalkingForward = false;
    }

    private void doNothing() {
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

    public boolean hasTarget() {
        return target != null;
    }

}
