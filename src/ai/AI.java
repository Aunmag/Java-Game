package ai;

import sprites.Actor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aunmag on 2016.10.23.
 */

public class AI {

    public static List<AI> all = new ArrayList<>();
    public static List<AI> invalids = new ArrayList<>();

    private Actor subject;
    private Strategy strategy;
    private static final int timeReaction = 300;
    private long timeReactionNext = 0;
    private boolean isReactionNow = false;

    public AI(Actor subject) {
        this.subject = subject;
        strategy = new StrategyChase(this);
    }

    public void update() {
        if (System.currentTimeMillis() > timeReactionNext) {
            isReactionNow = true;
            timeReactionNext = System.currentTimeMillis() + timeReaction;
        }

        if (subject.isRemoved() || !subject.getIsAlive()) {
            delete();
        } else {
            strategy.update();
        }

        isReactionNow = false;
    }

    public void delete() {
        invalids.add(this);
    }

    /* Getters */

    Actor getSubject() {
        return subject;
    }

    boolean getIsReactionNow() {
        return isReactionNow;
    }

}
