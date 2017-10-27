package aunmag.shooter.world;

import aunmag.nightingale.data.DataTime;

public class WorldTime {

    private static long currentMilliseconds;
    private static double current;
    private static double delta;
    private static double speed;

    static {
        reset();
    }

    static void update() {
        delta = DataTime.getTimeDelta() * speed;
        current += delta;
        currentMilliseconds = (long) (current * 1000);
    }

    static void reset() {
        currentMilliseconds = 0L;
        current = 0d;
        delta = 0d;
        speed = 1d;
    }

    /* Getters */

    public static long getCurrentMilliseconds() {
        return currentMilliseconds;
    }

    public static double getCurrent() {
        return current;
    }

    public static double getDelta() {
        return delta;
    }

    public static double getSpeed() {
        return speed;
    }

    /* Setters */

    public static void setSpeed(double speed) {
        WorldTime.speed = speed;
    }

}
