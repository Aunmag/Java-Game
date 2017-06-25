package sprites.components;

import managers.MathManager;
import sprites.Sprite;
import basics.BasePosition;

import java.awt.*;

/**
 * Created by Aunmag on 2017.05.26.
 */

public abstract class Collision extends BasePosition {

    protected static final Color renderColor = new Color(255, 255, 255, 100);
    protected Sprite target;

    protected Collision(Sprite target) {
        super(target.getX(), target.getY(), target.getRadians());
        this.target = target;
    }

    public abstract void render();

    public static boolean calculateIsCollision(CollisionCircle a, CollisionCircle b) {
        float distanceToCollision = a.getRadius() + b.getRadius();

//        // TODO: Check performance
//        if (distanceToCollision < min(a.getRadius(), b.getRadius())) {
//            return true;
//        }

        float distanceBetween = MathManager.calculateDistanceBetween(a, b);
        a.setLastDistanceBetween(distanceBetween);
        b.setLastDistanceBetween(distanceBetween);
        return distanceBetween < distanceToCollision;
    }

    public static boolean calculateIsCollision(CollisionCircle circle, CollisionLine line) {
        // TODO: Overwrite
        float circleX = circle.getX();
        float circleY = circle.getY();
        float circleRadius = circle.getRadius();

        float lineX1 = line.getX();
        float lineY1 = line.getY();
        float lineX2 = line.getX2();
        float lineY2 = line.getY2();

        lineX1 -= circleX;
        lineY1 -= circleY;
        lineX2 -= circleX;
        lineY2 -= circleY;

        float dx = lineX2 - lineX1;
        float dy = lineY2 - lineY1;

        // Составляем коэффициенты квадратного уравнения на пересечение прямой и окружности.
        // Если на отрезке [0..1] есть отрицательные значения, значит отрезок пересекает окружность
        float a = dx * dx + dy * dy;
        float b = 2f * (lineX1 * dx + lineY1 * dy);
        float c = lineX1 * lineX1 + lineY1 * lineY1 - circleRadius * circleRadius;

        // Теперь проверяем, есть ли на отрезке [0..1] решения:
        if (-b < 0) {
            return (c < 0);
        }
        if (-b < (2.*a)) {
            return ((4. * a * c - b * b) < 0);
        }

        return (a + b + c < 0);
    }

}
