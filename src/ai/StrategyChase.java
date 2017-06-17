package ai;

// Created by Aunmag on 21.11.2016.

import client.Constants;
import client.TimeManager;

class StrategyChase extends StrategyAbstract {

    StrategyChase(AI ai) {

        super(ai);

    }

    private void reachTarget() {

        if (ai.target == null) {
            return;
        }

        ai.subject.isAttacking = false;
        ai.subject.setRadians(ai.targetDirection);
        ai.subject.isWalkingForward = true;

        // If subject is behind the target:
        ai.subject.isSprinting = ai.targetFlankAbs < Constants.PI_0_5;

        if (96 < ai.targetDistance && ai.targetDistance < 512 && ai.targetFlankAbs > Math.PI / 1.4) {
            float radiansOffset = 0;

            if (ai.targetDistance > 512) {
                radiansOffset = (float) Constants.PI_0_5 / 2f;
            } else if (ai.targetDistance > 256) {
                radiansOffset = (float) Constants.PI_0_5 / 4f;
            } else if (ai.targetDistance > 128) {
                radiansOffset = (float) Constants.PI_0_5 / 8f;
            } else if (ai.targetDistance > 64) {
                radiansOffset = (float) Constants.PI_0_5 / 16f;
            }

            if (ai.targetFlankReal < 0) {
                ai.subject.setRadians(ai.subject.getRadians() - radiansOffset);
            } else {
                ai.subject.setRadians(ai.subject.getRadians() + radiansOffset);
            }
        }

        // If the subject is close enough to the target:
        if (ai.targetDistance < 96) {
            ai.subject.isSprinting = true;
        }

    }

    @Override public void tick() {

        if (TimeManager.getTimeCurrent() - ai.tSearchTargetLast >= ai.tSearchTarget) {
            ai.tSearchTargetLast = TimeManager.getTimeCurrent();
            searchTarget();
        }

        if (ai.target == null) {
            ai.subject.isWalkingForward = false;
            ai.subject.isAttacking = false;
            return;
        }

        if (TimeManager.getTimeCurrent() - ai.tReactionLast >= ai.tReaction) {
            ai.tReactionLast = TimeManager.getTimeCurrent();
            updateTargetPosition();
        }

        if (ai.isTargetReached) {
            ai.subject.isAttacking = true;
            ai.subject.isWalkingForward = false;
        } else {
            reachTarget();
        }

    }

}
