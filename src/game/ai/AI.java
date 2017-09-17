package game.ai;

import game.sprites.Actor;
import java.util.ArrayList;
import java.util.List;

public class AI {

    public static List<AI> all = new ArrayList<>();

    public static void updateAll() {
        List<AI> toDelete = new ArrayList<>();

        for (AI ai: all) {
            ai.update();

            if (ai.isRemoved) {
                toDelete.add(ai);
            }
        }

        all.removeAll(toDelete);
    }

    public static void removeAll() {
        for (AI ai: all) {
            ai.remove();
        }

        all.clear();
    }

    private Actor subject;
    private boolean isRemoved = false;
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
            remove();
        } else {
            strategy.update();
        }

        isReactionNow = false;
    }

    public void remove() {
        isRemoved = true;
    }

    /* Getters */

    Actor getSubject() {
        return subject;
    }

    boolean getIsReactionNow() {
        return isReactionNow;
    }

}
