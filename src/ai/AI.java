package ai;

// Created by Aunmag on 23.10.2016.

import sprites.Actor;
import java.util.ArrayList;
import java.util.List;

public class AI {

    public static List<AI> all = new ArrayList<>();
    public static List<AI> invalids = new ArrayList<>();

    public boolean isValid = true;

    Actor subject;

    // Target data:
    Actor target = null;
    float targetDirection;
    float targetFlankAbs;
    float targetFlankReal;
    float targetDistance;
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
        if (!subject.getIsValid() || !subject.getIsAlive()) {
            delete();
        }

        currentStrategy.tick();
    }

    public void delete() {
        isValid = false;
        invalids.add(this);
    }

}
