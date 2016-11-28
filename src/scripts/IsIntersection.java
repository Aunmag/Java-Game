package scripts;

// Created by Aunmag on 26.10.2016.

public class IsIntersection {

    public static boolean circleCircle (double aX, double aY, double aR, double bX, double bY, double bR) {

        double intersectionDistance = aR + bR;

        double realDistance = Math.sqrt(Math.pow(aX - bX, 2) + Math.pow(aY - bY, 2));

        return (realDistance < intersectionDistance);

    }

    public static boolean circleLine(double x1, double y1, double x2, double y2, double xC, double yC, double R) {

        x1 -= xC;
        y1 -= yC;
        x2 -= xC;
        y2 -= yC;

        double dx = x2 - x1;
        double dy = y2 - y1;

        // Составляем коэффициенты квадратного уравнения на пересечение прямой и окружности.
        // Если на отрезке [0..1] есть отрицательные значения, значит отрезок пересекает окружность
        double a = dx * dx + dy * dy;
        double b = 2. * (x1 * dx + y1 * dy);
        double c = x1 * x1 + y1 * y1 - R * R;

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
