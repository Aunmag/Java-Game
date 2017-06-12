package client;

/**
 * Created by Aunmag on 2017.05.26.
 */

public class TimeManager {

    public static final int FPS_LIMIT = 75;
    public static final float FRAME_DURATION = 1000f / FPS_LIMIT;
    private static long timeCurrent = 0;
    private static float timeDelta = 0;

    /* Setters */

    static void setTimeCurrent(long timeCurrent) {
        TimeManager.timeCurrent = timeCurrent;
    }

    static void setTimeDelta(float timeDelta) {
        TimeManager.timeDelta = timeDelta;
    }

    /* Getters */

    public static long getTimeCurrent() {
        return timeCurrent;
    }

    public static float getTimeDelta() {
        return timeDelta;
    }

}
