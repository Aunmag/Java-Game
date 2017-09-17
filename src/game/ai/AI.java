package game.ai;

import game.sprites.Actor;
import nightingale.basics.BaseOperative;

public class AI implements BaseOperative {

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

        if (subject.isRemoved() || !subject.isAlive()) {
            remove();
        } else {
            strategy.update();
        }

        isReactionNow = false;
    }

    public void render() {}

    public void remove() {
        isRemoved = true;
    }

    /* Getters */

    Actor getSubject() {
        return subject;
    }

    boolean isReactionNow() {
        return isReactionNow;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

}
