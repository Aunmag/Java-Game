package managers;

import client.Application;

/**
 * Created by Aunmag on 2017.06.03.
 */

public class Timer {

    private long timeStart;
    private long timeDuration = 0;
    private ArrayAverage timeDurationAverage;
    private final boolean isAverageUsing;

    public Timer(boolean isAverageUsing) {
        this.isAverageUsing = isAverageUsing;

        if (isAverageUsing) {
            timeDurationAverage = new ArrayAverage(Application.getFpsLimit());
        }
    }

    public void makeStart() {
        timeStart = System.currentTimeMillis();
    }

    public void makeStop() {
        timeDuration = System.currentTimeMillis() - timeStart;
        timeStart = 0;

        if (isAverageUsing) {
            timeDurationAverage.addValue(timeDuration);
        }
    }

    public long getTimeDuration() {
        return timeDuration;
    }

    public float getTimeDurationAverage() {
        return timeDurationAverage.getAverageValue();
    }

}
