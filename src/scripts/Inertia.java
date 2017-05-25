package scripts;

import client.Client;
import client.Constants;

/**
 * Gradually change a number from initial value to target according to FPS.
 * In this way provide smooth transition.
 *
 * For example we want to change x=0.0 to x=0.8 smoothly per 4 seconds and
 * 1 FPS then it will look this way:
 *
 * x = 0.0  # 0 second pass (initial value)
 * x = 0.2  # 1 second pass
 * x = 0.4  # 2 second pass
 * x = 0.6  # 3 second pass
 * x = 0.8  # 4 second pass (target value)
 *
 * As well as you can see every second (because us FPS == 1) variable 'x'
 * increased by 0.2. If us FPS would equal 2 then updates will occur twice
 * as likely and 'x' will increase by 0.1 each frame.
 *
 * Created by Aunmag on 27.10.2016.
 */

public class Inertia {

    public float valuePrevious = 0;
    public float valueCurrent = 0;
    public float valueTarget = 0;
    public float valueRound = 0;
    public float valueUnit = 0;
    public float bendDegree = 1;
    private float tDuration = 0;

    private boolean isProcessing = false;
    private float valueTestA = 0;
    private float valueTestB = 0;

    public Inertia(float tDuration) {

        this.tDuration = tDuration;

    }

    private void calculateValueUnit(float valueTarget) {

        this.valueTarget = valueTarget;

        valueUnit = (valueTarget - valuePrevious) / (tDuration * Constants.FPS_LIMIT);

        float valueTest = Math.abs(valueUnit);
        valueTestA = valueTarget - valueTest;
        valueTestB = valueTarget + valueTest;

    }

    public float update(float timeDelta, float valueTarget) {

        if (isProcessing && valueTarget != this.valueTarget) {
            valuePrevious = valueCurrent;
            isProcessing = false;
        }

        if (valueTarget != this.valueTarget) {
            calculateValueUnit(valueTarget);
        }


        if (!isProcessing && valueTarget != valuePrevious) {
            isProcessing = true;
        }

        if (isProcessing) {
            valueCurrent += valueUnit * timeDelta;
            if (valueTestA <= valueCurrent && valueCurrent <= valueTestB) {
                valuePrevious = valueTarget;
                valueCurrent = valueTarget;
                isProcessing = false;
            }
        }

        return valueCurrent;

    }

    // Setters:

    public void setTDuration(float tDuration) {

        this.tDuration = tDuration;
        calculateValueUnit(valueTarget);

    }

    // Getters:

    public float getValueTarget() {

        return valueTarget;

    }

    public float getTDuration() {

        return tDuration;

    }

    public boolean getState() {

        return isProcessing;

    }

}
