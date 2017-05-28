package sprites.components;

import client.Client;
import sprites.Actor;

import java.awt.*;

/**
 * Created by Aunmag on 2017.05.27.
 */

public class Hands extends CollisionCircle {

    private static boolean isVisible = false;
    protected static final Color renderColor = new Color(255, 0, 0, 128);

    private static final float radius = 14;
    private static final float distance = radius * 1.5f;
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

        if (owner.isAttacking && !owner.getHasWeapon() && Client.getT() >= timeAttackNext) {
            attack();
        }
    }

    private void updatePosition() {
        x = owner.getX() + distance * (float) Math.cos(owner.getRadians());
        y = owner.getY() + distance * (float) Math.sin(owner.getRadians());
    }

    private void attack() {
        for (Actor actor: Actor.all) {
            if (actor.group.equals(owner.group) || actor == owner) {
                continue;
            }

            if (Collision.calculateIsCollision(this, actor.getCollision())) {
                actor.hit(damage * owner.getHealth(), radians);
            }
        }

        timeAttackNext = Client.getT() + timeAttackPace;
    }

    public void render() {
        if (!isVisible) {
            return;
        }

        Client.getG().setColor(renderColor);

        int onScreenX = Math.round(x - Client.getGX() - radius);
        int onScreenY = Math.round(y - Client.getGY() - radius);
        int onScreenDiameter = Math.round(diameter);

        Client.getG().fillRoundRect(
                onScreenX,
                onScreenY,
                onScreenDiameter,
                onScreenDiameter,
                onScreenDiameter,
                onScreenDiameter
        );
    }

}
