package sprites.components;

import nightingale.collision.Collision;
import nightingale.collision.CollisionCircle;
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
        super(owner.getX(), owner.getY(), radius);
        this.owner = owner;
        updatePosition();
    }

    public void update() {
        updatePosition();

        if (owner.isAttacking && !owner.getHasWeapon() && System.currentTimeMillis() >= timeAttackNext) {
            attack();
        }
    }

    private void updatePosition() {
        setPosition(
                owner.getX() + distance * (float) Math.cos(owner.getRadians()),
                owner.getY() + distance * (float) Math.sin(owner.getRadians())
        );
    }

    private void attack() {
        for (Actor actor: Actor.all) {
            if (actor.type.equals(owner.type) || actor == owner) {
                continue;
            }

            if (Collision.calculateIsCollision(this, actor.getCollision())) {
                actor.hit(damage * owner.getHealth(), radius, owner);
            }
        }

        timeAttackNext = System.currentTimeMillis() + timeAttackPace;
    }

    public void render() {
        super.render(renderColor);
    }

}
