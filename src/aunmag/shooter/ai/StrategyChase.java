package aunmag.shooter.ai;

import aunmag.nightingale.utilities.UtilsMath;

class StrategyChase extends Strategy {

    protected static final int timeSearchTarget = 2_000;
    protected long timeSearchTargetNext = 0;

    StrategyChase(AI ai) {
        super(ai);
    }

    private void chaseTarget() {
        if (target == null) {
            return;
        }

        ai.getSubject().isAttacking = false;
        ai.getSubject().setRadians(targetDirection);
        ai.getSubject().isWalkingForward = true;
        ai.getSubject().isSprinting = calculateIsBehindTarget();

        if (96 < targetDistance && targetDistance < 512 && targetRadiansDifferenceAbsolute > Math.PI / 1.4) { // TODO: Improve
            deviateRoute();
        } else if (targetDistance < 96) {
            ai.getSubject().isSprinting = true;
        }
    }

    private void attackTarget() {
        ai.getSubject().isAttacking = true;
        ai.getSubject().isWalkingForward = false;
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
            ai.getSubject().setRadians(ai.getSubject().getRadians() - radiansDeviate);
        } else {
            ai.getSubject().setRadians(ai.getSubject().getRadians() + radiansDeviate);
        }
    }

    private void stop() {
        ai.getSubject().isAttacking = false;
        ai.getSubject().isSprinting = false;
        ai.getSubject().isWalkingForward = false;
    }

    public void update() {
        if (System.currentTimeMillis() > timeSearchTargetNext) {
            searchTarget();
            timeSearchTargetNext = System.currentTimeMillis() + timeSearchTarget;
        }

        if (target == null) {
            stop();
            return;
        }

        if (ai.isReactionNow()) {
            updateTargetData();
        }

        if (isTargetReached) {
            attackTarget();
        } else {
            chaseTarget();
        }
    }

}
