package sprites.components;

import client.Application;
import sprites.Actor;

import java.awt.*;

/**
 * Created by Aunmag on 2017.05.27.
 */

public class Hands extends CollisionCircle {

    protected static final Color renderColor = new Color(255, 0, 0, 128);
    private static final float radius = 11;
    private static final float distance = radius;
    private static final float damage = 10;
    private static final long timeAttackPace = 400;
    private final Actor owner;
    private long timeAttackNext = 0;

    public Hands(Actor owner) {
        super(owner, radius);
        this.owner = owner;
        updatePosition();
    }

    public void update() {
        updatePosition();

        if (owner.isAttacking && !owner.getHasWeapon() && Application.getTimeCurrent() >= timeAttackNext) {
            attack();
        }
    }

    private void updatePosition() {
        x = owner.getX() + distance * (float) Math.cos(owner.getRadians());
        y = owner.getY() + distance * (float) Math.sin(owner.getRadians());
    }

    private void attack() {
        for (Actor actor: Actor.all) {
            if (actor.type.equals(owner.type) || actor == owner) {
                continue;
            }

            if (Collision.calculateIsCollision(this, actor.getCollision())) {
                actor.hit(damage * owner.getHealth(), radians);
            }
        }

        timeAttackNext = Application.getTimeCurrent() + timeAttackPace;
    }

    public void render() {
        super.render(renderColor);
    }

}
