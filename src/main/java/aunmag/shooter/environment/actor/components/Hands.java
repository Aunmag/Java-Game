package aunmag.shooter.environment.actor.components;

import aunmag.nightingale.math.BodyCircle;
import aunmag.nightingale.math.CollisionCC;
import aunmag.nightingale.utilities.Timer;
import aunmag.shooter.environment.actor.Actor;

public class Hands {

    private static final float radius = 0.34f;
    private static final float distance = radius;
    private final Timer nextAttackTime;
    private final Actor owner;
    public final BodyCircle coverage;

    public Hands(Actor owner) {
        this.owner = owner;
        coverage = new BodyCircle(0, 0, 0, radius);
        coverage.color.set(1f, 0f, 0f, 0.5f);

        nextAttackTime = new Timer(owner.world.getTime(), 0.4f, 0.125f);
        updatePosition();
    }

    public void update() {
        updatePosition();

        if (owner.isAttacking && !owner.getHasWeapon() && nextAttackTime.isDone()) {
            attack();
            nextAttackTime.next();
        }
    }

    public void updatePosition() {
        float radians = owner.body.radians;
        float x = owner.body.position.x + distance * (float) Math.cos(radians);
        float y = owner.body.position.y + distance * (float) Math.sin(radians);
        coverage.position.set(x, y);
    }

    private void attack() {
        for (Actor opponent: owner.world.getActors().all) {
            if (owner.type == opponent.type || opponent == owner) {
                continue;
            }

            if (new CollisionCC(coverage, opponent.body).isTrue()) {
                opponent.hit(owner.type.damage * owner.getHealth(), owner);
            }
        }
    }

}
