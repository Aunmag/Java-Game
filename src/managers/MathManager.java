package managers;

import client.Constants;

import java.util.Random;

/**
 * Created by Aunmag on 2017.05.25.
 */

public class MathManager {

    public static Random random = new Random();

    public static float correctRadians(double value) {
        double result = value % Constants.PI_2_0;
        return (float) result;
    }

    public static float randomizeBetween(double min, double max) {
        if (min > max) {
            // TODO: log
            double min_copy = min;
            min = max;
            max = min_copy;
        } else if (min == max) {
            // TODO: log
            return (float) min;
        }

        double difference = max - min;
        double result = random.nextDouble() * difference + min;
        return (float) result;
    }

}
