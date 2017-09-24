package aunmag.shooter.managers;

public class NextTimer {

    private int timeDuration = 0;
    private long timeNext = 0;
    private boolean isNow = false;

    public NextTimer(int timeDuration) {
        setTimeDuration(timeDuration);
    }

    public void update(long timeCurrent) {
        isNow = false;

        if (timeCurrent > timeNext) {
            timeNext = timeCurrent + timeDuration;
            isNow = true;
        }
    }

    /* Setters */

    public void setTimeDuration(int timeDuration) {
        timeNext -= this.timeDuration;
        this.timeDuration = timeDuration;
        timeNext += this.timeDuration;
    }

    /* Getters */

    public boolean isNow() {
        return isNow;
    }

}
