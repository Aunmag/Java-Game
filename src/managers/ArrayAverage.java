package managers;

// Created by Aunmag on 27.10.2016.

public class ArrayAverage {

    private int depth;
    private float[] values;
    private int index = 0;
    private float average;

    public ArrayAverage(int depth) {

        if (depth > 1) {
            this.depth = depth;
        } else {
            this.depth = 2;
        }

        values = new float[this.depth];

    }

    public void addValue(float value) {

        values[index++] = value;
        if (index == values.length) {
            index = 0;
        }

    }

    public float getAverage() {

        average = 0;

        for (float value: values) {
            average += value;
        }

        average /= values.length;

        return average;

    }

}
