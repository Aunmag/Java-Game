package managers;

import nightingale.utilities.UtilsLog;

/**
 * Created by Aunmag on 2016.10.27.
 */

public class ArrayAverage {

    private float[] values;
    private int indexUpdateNext = 0;
    private float averageValue;

    public ArrayAverage(int depth) {
        if (depth < 1) {
            String message = String.format(
                    "Got wrong ArrayAverage depth value (%s). Replaced with 2.", depth
            );
            UtilsLog.log("ArrayAverage", message);
            depth = 2;
        }

        values = new float[depth];
    }

    public void addValue(float value) {
        values[indexUpdateNext] = value;
        indexUpdateNext++;
        indexUpdateNext %= values.length;

        updateAverageValue();
    }

    private void updateAverageValue() {
        float valuesSum = 0;

        for (float value: values) {
            valuesSum += value;
        }

        averageValue = valuesSum / values.length;
    }

    public float getAverageValue() {
        return averageValue;
    }

}
