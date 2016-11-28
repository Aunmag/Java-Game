package scripts;

import client.Client;

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

    public double valuePrevious = 0;
    public double valueCurrent = 0;
    public double valueTarget = 0;
    public double valueRound = 0;
    public double valueUnit = 0;
    public double bendDegree = 1;
    private double tDuration = 0;

    private boolean isProcessing = false;
    private double valueTestA = 0;
    private double valueTestB = 0;

    public Inertia(double tDuration) {

        this.tDuration = tDuration;

    }

    private void calculateValueUnit(double valueTarget) {

        this.valueTarget = valueTarget;

        valueUnit = (valueTarget - valuePrevious) / (tDuration * Client.getFpsLimit());

        double valueTest = Math.abs(valueUnit);
        valueTestA = valueTarget - valueTest;
        valueTestB = valueTarget + valueTest;

    }

    public double update(double timeDelta, double valueTarget) {

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

    public void setTDuration(double tDuration) {

        this.tDuration = tDuration;
        calculateValueUnit(valueTarget);

    }

    // Getters:

    public double getValueTarget() {

        return valueTarget;

    }

    public double getTDuration() {

        return tDuration;

    }

    public boolean getState() {

        return isProcessing;

    }

}
