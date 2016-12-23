package ai;

// Created by Aunmag on 23.10.2016.

import client.Client;
import scripts.IsIntersection;
import sprites.Actor;
import java.util.ArrayList;
import java.util.List;

public class AI {

    public static List<AI> allAIs = new ArrayList<>();

    public boolean isValid = true;

    Actor subject;

    // Target data:
    Actor target = null;
    double targetDirection;
    double targetFlankAbs;
    double targetFlankReal;
    double targetDistance;
    boolean isTargetReached = false;

    static final long tSearchTarget = 2_000;
    static final long tReaction = 300;
    long tSearchTargetLast = 0;
    long tReactionLast = 0;

    // Strategies:
    private StrategyAbstract currentStrategy;
    private StrategyChase strategyChase;

    public AI(Actor subject) {

        this.subject = subject;

        strategyChase = new StrategyChase(this);
        currentStrategy = strategyChase;

    }

    public void tick() {

        if (!isValid || (!subject.isValid || !subject.isAlive)) {
            isValid = false;
            allAIs.remove(this);
            return;
        }

        currentStrategy.tick();

    }

}
