package aunmag.shooter.sprites.components;

import aunmag.nightingale.utilities.TimerNext;
import aunmag.shooter.sprites.Actor;
import aunmag.shooter.world.World;
import aunmag.nightingale.collision.Collision;
import aunmag.nightingale.collision.CollisionCircle;

import java.awt.*;

public class Hands extends CollisionCircle {

    protected static final Color renderColor = new Color(255, 0, 0, 128);
    private static final float radius = 11;
    private static final float distance = radius;
    private static final float damage = 750;
    private TimerNext nextAttackTime = new TimerNext(400);
    private final Actor owner;

    public Hands(Actor owner) {
        super(owner.getX(), owner.getY(), radius);
        this.owner = owner;
        updatePosition();
    }

    public void update() {
        updatePosition();

        nextAttackTime.update(System.currentTimeMillis());
        if (owner.isAttacking && !owner.getHasWeapon() && nextAttackTime.isNow()) {
            attack();
        }
    }

    public void updatePosition() {
        float x = owner.getX() + distance * (float) Math.cos(owner.getRadians());
        float y = owner.getY() + distance * (float) Math.sin(owner.getRadians());
        setPosition(x, y);
    }

    private void attack() {
        for (Actor actor: World.actors) {
            if (actor.type.equals(owner.type) || actor == owner) {
                continue;
            }

            if (Collision.calculateIsCollision(this, actor.getCollision())) {
                actor.hit(damage * owner.getHealth(), owner.getRadians(), owner);
            }
        }
    }

    public void render() {
        super.render(renderColor);
    }

}
