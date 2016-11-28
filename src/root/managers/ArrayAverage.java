package root.managers;

// Created by Aunmag on 27.10.2016.

public class ArrayAverage {

    private int depth;
    private double[] values;
    private int index = 0;
    private double average;

    public ArrayAverage(int depth) {

        if (depth > 1) {
            this.depth = depth;
        } else {
            this.depth = 2;
        }

        values = new double[this.depth];

    }

    public void addValue(double value) {

        values[index++] = value;
        if (index == values.length) {
            index = 0;
        }

    }

    public double getAverage() {

        average = 0;

        for (double value: values) {
            average += value;
        }

        average /= values.length;

        return average;

    }

}
