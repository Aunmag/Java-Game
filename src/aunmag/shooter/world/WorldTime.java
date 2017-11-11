package aunmag.shooter.world;

import aunmag.nightingale.data.DataTime;

public class WorldTime {

    private static long currentMilliseconds = 0L;
    private static double current = 0d;
    private static double delta = 0d;
    private static double speed = 1d;

    void update() {
        delta = DataTime.getTimeDelta() * speed;
        current += delta;
        currentMilliseconds = (long) (current * 1000);
    }

    /* Getters */

    public long getCurrentMilliseconds() {
        return currentMilliseconds;
    }

    public double getCurrent() {
        return current;
    }

    public double getDelta() {
        return delta;
    }

    public double getSpeed() {
        return speed;
    }

    /* Setters */

    public void setSpeed(double speed) {
        WorldTime.speed = speed;
    }

}
