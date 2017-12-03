package aunmag.shooter.actor;

import aunmag.nightingale.utilities.TimerNext;
import aunmag.shooter.client.Game;
import aunmag.nightingale.collision.Collision;
import aunmag.nightingale.collision.CollisionCircle;
import org.joml.Vector4f;

import java.awt.*;

public class Hands extends CollisionCircle {

    private static final float radius = 0.34f;
    private static final float distance = radius;
    private TimerNext nextAttackTime = new TimerNext(0.4f);
    private final Actor owner;

    public Hands(Actor owner) {
        super(owner.getX(), owner.getY(), radius);
        this.owner = owner;
        updatePosition();
        this.color = new Vector4f(1f, 0f, 0f, 0.5f);
    }

    public void update() {
        updatePosition();

        nextAttackTime.update(Game.getWorld().getTime().getCurrent());
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
        for (Actor opponent: Game.getWorld().getActors()) {
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
