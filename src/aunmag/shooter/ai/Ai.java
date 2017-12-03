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
    private TimerNext reactionWatch = new TimerNext(0.3f);
    private TimerNext reactionLookAround = new TimerNext(2f);
    private TimerNext reactionChangeStrategy = new TimerNext(30f);
    private int strategyDeviationWay = 0;
    private Actor subject;
    private AiMemoryTarget memoryTarget = new AiMemoryTarget();

    public Ai(Actor subject) {
        this.subject = subject;
        changeStrategy();
    }

    public void update() {
        if (subject.isRemoved() || !subject.isAlive()) {
            remove();
            return;
        }

        reactionChangeStrategy.update(subject.world.getTime().getCurrent());
        if (reactionChangeStrategy.isNow()) {
            changeStrategy();
        }

        reactionLookAround.update(subject.world.getTime().getCurrent());
        if (reactionLookAround.isNow()) {
            searchTarget();
        }

        if (memoryTarget.isInMemory()) {
            reactionWatch.update(subject.world.getTime().getCurrent());
            if (reactionWatch.isNow()) {
                updateTargetData();
            }

            if (memoryTarget.isReached()) {
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
        memoryTarget.forget();

        for (Actor actor: subject.world.getActors()) {
            if (actor.isAlive() && !actor.isRemoved() && actor.type == ActorType.human) {
                memoryTarget.setActor(actor);
                break;
            }
        }
    }

    private void updateTargetData() {
        Actor targetActor = memoryTarget.getActor();

        if (targetActor == null) {
            return;
        }

        float x = subject.getX();
        float y = subject.getY();
        float targetX = targetActor.getX();
        float targetY = targetActor.getY();

        memoryTarget.setDistance(UtilsMath.calculateDistanceBetween(x, y, targetX, targetY));
        memoryTarget.setDirection(UtilsMath.calculateRadiansBetween(targetX, targetY, x, y));

        memoryTarget.setReached(Collision.calculateIsCollision(
                subject.getHands(),
                targetActor
        ));

        float radiansDifference = memoryTarget.getDirection() - targetActor.getRadians();
        radiansDifference = UtilsMath.correctRadians(radiansDifference);
        memoryTarget.setRadiansDifference(radiansDifference);
        memoryTarget.setRadiansDifferenceAbsolute(Math.abs(radiansDifference));
    }

    private boolean calculateIsBehindTarget() {
        return memoryTarget.getRadiansDifferenceAbsolute() < UtilsMath.PIx0_5;
    }

    private void chaseTarget() {
        subject.isAttacking = false;
        subject.setRadians(memoryTarget.getDirection());
        subject.isWalkingForward = true;
        subject.isSprinting = calculateIsBehindTarget();

        if (memoryTarget.getRadiansDifferenceAbsolute() > UtilsMath.PIx0_5) {
            deviateRoute();
        } else if (memoryTarget.getDistance() < 3) {
            subject.isSprinting = true;
        }
    }

    private void deviateRoute() {
        float targetDistance = memoryTarget.getDistance();
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

}
