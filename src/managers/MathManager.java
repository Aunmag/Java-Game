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

    public static int randomizeBetween(int min, int max) {
        if (min > max) {
            // TODO: log
            int min_copy = min;
            min = max;
            max = min_copy;
        } else if (min == max) {
            // TODO: log
            return min;
        }

        int difference = max - min;
        return random.nextInt(difference + 1) + min;
    }

    public static float randomizeBetween(float min, float max) {
        if (min > max) {
            // TODO: log
            float min_copy = min;
            min = max;
            max = min_copy;
        } else if (min == max) {
            // TODO: log
            return min;
        }

        float difference = max - min;
        return random.nextFloat() * difference + min;
    }

    public static float randomizeFlexibly(float middle, float offset) {
        return randomizeFlexibly(middle, offset, 0.5f);
    }

    public static float randomizeFlexibly(float middle, float offset, float flex) {
        if (flex <= 0 || flex > 1) {
            // TODO: log
            flex = 0.5f;
        }

        // Randomize offset to flexed result
        float offsetMin = offset * randomizeBetween(0, flex);
        float offsetMax = offset * randomizeBetween(flex, 1);
        float offsetRandom = randomizeBetween(offsetMin, offsetMax);

        // Set minimal and maximal result value
        float resultMin = middle - offsetRandom;
        float resultMax = middle + offsetRandom;

        // Randomize and return result
        return randomizeBetween(resultMin, resultMax);
    }

}
