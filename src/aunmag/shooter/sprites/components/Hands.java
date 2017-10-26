package aunmag.shooter.sprites.components;

import aunmag.nightingale.utilities.TimerNext;
import aunmag.shooter.sprites.Actor;
import aunmag.shooter.world.World;
import aunmag.nightingale.collision.Collision;
import aunmag.nightingale.collision.CollisionCircle;

import java.awt.*;

public class Hands extends CollisionCircle {

    protected static final Color renderColor = new Color(255, 0, 0, 128);
    private static final float radius = 0.34f;
    private static final float distance = radius;
    private static final float damage = 4f;
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
        for (Actor opponent: World.actors) {
            if (!opponent.isAlive() || opponent.isRemoved()) {
                continue;
            }

            if (opponent.type.equals(owner.type) || opponent == owner) {
                continue;
            }

            if (Collision.calculateIsCollision(this, opponent.getCollision())) {
                opponent.hit(damage * owner.getHealth(), owner);
            }
        }
    }

    public void render() {
        super.render(renderColor);
    }

}
