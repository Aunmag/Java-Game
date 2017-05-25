package scripts;

// Created by Aunmag on 26.10.2016.

public class IsIntersection {

    public static boolean circleCircle (float aX, float aY, float aR, float bX, float bY, float bR) {

        float intersectionDistance = aR + bR;

        float realDistance = (float) (Math.sqrt(Math.pow(aX - bX, 2) + Math.pow(aY - bY, 2)));

        return (realDistance < intersectionDistance);

    }

    public static boolean circleLine(float x1, float y1, float x2, float y2, float xC, float yC, float R) {

        x1 -= xC;
        y1 -= yC;
        x2 -= xC;
        y2 -= yC;

        float dx = x2 - x1;
        float dy = y2 - y1;

        // Составляем коэффициенты квадратного уравнения на пересечение прямой и окружности.
        // Если на отрезке [0..1] есть отрицательные значения, значит отрезок пересекает окружность
        float a = dx * dx + dy * dy;
        float b = 2f * (x1 * dx + y1 * dy);
        float c = x1 * x1 + y1 * y1 - R * R;

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
