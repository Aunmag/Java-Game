package root.ai;

// Created by Aunmag on 23.10.2016.

import root.scripts.IsIntersection;
import root.sprites.Actor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AI {

    public static List<AI> allAIs = new ArrayList<>();
    public static Random random = new Random();

    public boolean isValid = true;
    public Actor subject;

    // Target data:
    private Actor target = null;
    private double targetX;
    private double targetY;
    private double targetDegrees;
    private double targetDirection;
    private double targetDistance;
    private boolean isTargetReached = false;

    private boolean hasStrategyRadiansReached = false;
    private int strategyDirection = random.nextInt(2);
    private int strategyDistance = random.nextInt(400) + 80;
    private double strategyRadians;
    private double strategyRadiansA;
    private double strategyRadiansB;

    private int timeReaction = 400000000;
    private long timeCurrent;
    private long timeLastReaction = 0;

    public AI(Actor subject) {

        this.subject = subject;

        strategyRadians = Math.toRadians(random.nextInt(360));
        double strategyRadiansDeflection = Math.PI / 3;
        strategyRadiansA = strategyRadians - strategyRadiansDeflection;
        strategyRadiansB = strategyRadians + strategyRadiansDeflection;

    }

    // AI action methods:

    private void reachTarget() {

        if (target == null) {
            return;
        }

        subject.setRadians(targetDirection);

//        if (hasStrategyRadiansReached) {
//            subject.setRadians(targetDirection);
//        } else {
//            if (strategyDirection == 1) {
//                subject.setRadians(targetDirection + Math.PI / 4);
//            } else {
//                subject.setRadians(targetDirection - Math.PI / 4);
//            }
//        }

        subject.isMovingForward = true;

    }

    // AI analysis methods:

    private void searchTarget() {

        for (Actor actor: Actor.allActors) {
            if (actor.type.equals("human") && actor.isAlive) {
                target = actor;
                break;
            }
        }

        if (target == null) {
            isTargetReached = true;
            subject.isAttacking = false;
            subject.isMovingForward = false;
            return;
        }

        targetX = target.x;
        targetY = target.y;
        targetDegrees = target.getDegrees();
        targetDistance = Math.sqrt(Math.pow(subject.x - targetX, 2) + Math.pow(subject.y - targetY, 2));

        if (IsIntersection.circleCircle(
                subject.meleeX, subject.meleeY, subject.meleeRadius / 2,
                target.x, target.y, target.bodyRadius
        )) {
            isTargetReached = true;
        } else {
            isTargetReached = false;
        }

        double x = targetX - subject.x;
        double y = targetY - subject.y;

        targetDirection = Math.atan2(y, x);

        if (strategyRadiansA <= targetDirection && targetDirection <= strategyRadiansB || targetDistance > strategyDistance) {
            hasStrategyRadiansReached = true;
//            subject.isRunning = false;
        } else {
            hasStrategyRadiansReached = false;
//            subject.isRunning = true;
        }

    }

    public void tick() {

        if (!subject.isValid || !subject.isAlive) {
            isValid = false;
        }

        timeCurrent = System.nanoTime();
        if (timeCurrent - timeLastReaction >= timeReaction) {
            timeLastReaction = timeCurrent;
//            System.out.print("nano");
            searchTarget();
        }

//        if (target != null) {
//            reachTarget();
//        }

        if (isTargetReached) {
            subject.isAttacking = true;
            subject.isMovingForward = false;
        } else {
            subject.isAttacking = false;
            reachTarget();
        }

    }

}
