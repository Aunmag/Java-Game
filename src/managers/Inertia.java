package managers;

import client.Constants;
import client.TimeManager;

/**
 * TODO: Overwrite
 *
 * Gradually change a number from initial value to target according to FPS. In this way provide
 * smooth transition.
 *
 * For example we want to change x=0.0 to x=0.8 smoothly per 4 seconds and 1 FPS then it will look
 * this way:
 *
 * x = 0.0  # 0 second pass (initial value)
 * x = 0.2  # 1 second pass
 * x = 0.4  # 2 second pass
 * x = 0.6  # 3 second pass
 * x = 0.8  # 4 second pass (target value)
 *
 * As well as you can see every second (because us FPS == 1) variable 'x' increased by 0.2. If us
 * FPS would equal 2 then updates will occur twice as likely and 'x' will increase by 0.1 each
 * frame.
 *
 * Created by Aunmag on 2016.10.27.
 */

public class Inertia {

    public float target = 0;
    public float current = 0;
    public float previous = 0;
    public float step = 0;

    private float timeDuration = 0;

    private boolean isProcessing = false;

    private float valueTestA = 0;
    private float valueTestB = 0;

    public Inertia(float timeDuration) {
        this.timeDuration = timeDuration;
    }

    public float update(float valueTarget) {
        if (isProcessing && valueTarget != this.target) {
            previous = current;
            isProcessing = false;
        }

        if (valueTarget != this.target) {
            setTarget(valueTarget);
        }

        if (!isProcessing && valueTarget != previous) {
            isProcessing = true;
        }

        if (isProcessing) {
            current += step;
            if (valueTestA <= current && current <= valueTestB) {
                previous = valueTarget;
                current = valueTarget;
                isProcessing = false;
            }
        }

        return current;
    }

    private void updateStep() {
        step = (target - previous) / (timeDuration * TimeManager.FPS_LIMIT);
        float valueTest = Math.abs(step);
        valueTestA = target - valueTest;
        valueTestB = target + valueTest;
    }

    /* Setters */

    public void setTimeDuration(float timeDuration) {
        this.timeDuration = timeDuration;
        updateStep();
    }

    public void setTarget(float target) {
        this.target = target;
        updateStep();
    }

    /* Getters */

    public float getTimeDuration() {
        return timeDuration;
    }

    public boolean getIsProcessing() {
        return isProcessing;
    }

}
