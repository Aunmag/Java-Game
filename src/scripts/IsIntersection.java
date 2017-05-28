package scripts;

// Created by Aunmag on 26.10.2016.

public class IsIntersection {

    public static boolean circleCircle (float aX, float aY, float aR, float bX, float bY, float bR) {
        float intersectionDistance = aR + bR;
        float realDistance = (float) (Math.sqrt(Math.pow(aX - bX, 2) + Math.pow(aY - bY, 2)));
        return (realDistance < intersectionDistance);
    }

}
