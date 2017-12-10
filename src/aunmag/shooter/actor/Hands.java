package aunmag.shooter.actor;

import aunmag.nightingale.utilities.Timer;
import aunmag.nightingale.collision.Collision;
import aunmag.nightingale.collision.CollisionCircle;
import org.joml.Vector4f;

public class Hands extends CollisionCircle {

    private static final float radius = 0.34f;
    private static final float distance = radius;
    private final Timer nextAttackTime;
    private final Actor owner;

    public Hands(Actor owner) {
        super(owner.getX(), owner.getY(), radius);
        this.owner = owner;
        nextAttackTime = new Timer(owner.world.getTime(), 0.4f, 0.125f);
        updatePosition();
        this.color = new Vector4f(1f, 0f, 0f, 0.5f);
    }

    public void update() {
        updatePosition();

        if (owner.isAttacking && !owner.getHasWeapon() && nextAttackTime.isDone()) {
            attack();
            nextAttackTime.next();
        }
    }

    public void updatePosition() {
        float x = owner.getX() + distance * (float) Math.cos(owner.getRadians());
        float y = owner.getY() + distance * (float) Math.sin(owner.getRadians());
        setPosition(x, y);
    }

    private void attack() {
        for (Actor opponent: owner.world.getActors()) {
            if (!opponent.isAlive() || opponent.isRemoved()) {
                continue;
            }

            if (owner.type == opponent.type || opponent == owner) {
                continue;
            }

            if (Collision.calculateIsCollision(this, opponent)) {
                opponent.hit(owner.type.damage * owner.getHealth(), owner);
            }
        }
    }

}
